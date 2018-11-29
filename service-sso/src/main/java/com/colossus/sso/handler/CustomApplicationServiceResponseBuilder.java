package com.colossus.sso.handler;

import com.google.common.base.Throwables;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.CasProtocolConstants;
import org.apereo.cas.CipherExecutor;
import org.apereo.cas.authentication.principal.Response;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.authentication.principal.WebApplicationServiceResponseBuilder;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.token.authentication.principal.TokenWebApplicationServiceResponseBuilder;
import org.apereo.cas.util.DateTimeUtils;
import org.apereo.cas.web.support.WebUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomApplicationServiceResponseBuilder extends TokenWebApplicationServiceResponseBuilder {

    private static final long serialVersionUID = -851233878780818494L;

    private ExpirationPolicy ticketGrantingTicketExpirationPolicy;

    private JwtGenerator jwtGenerator;
    @Autowired
    private CasConfigurationProperties casProperties;

    public CustomApplicationServiceResponseBuilder(ServicesManager servicesManager, CipherExecutor tokenCipherExecutor, ExpirationPolicy ticketGrantingTicketExpirationPolicy, JwtGenerator jwtGenerator) {
        super(servicesManager, tokenCipherExecutor, ticketGrantingTicketExpirationPolicy);
        this.ticketGrantingTicketExpirationPolicy=ticketGrantingTicketExpirationPolicy;
        this.jwtGenerator=jwtGenerator;
    }


    @Override
    protected String generateToken(Service service, Map<String, String> parameters) {
        try {
            final String ticketId = parameters.get(CasProtocolConstants.PARAMETER_TICKET);
            final Cas30ServiceTicketValidator validator = new Cas30ServiceTicketValidator(casProperties.getServer().getPrefix());
            final Assertion assertion = validator.validate(ticketId, service.getId());
            final JWTClaimsSet.Builder claims =
                    new JWTClaimsSet.Builder()
                            .audience(service.getId())
                            .issuer(casProperties.getServer().getPrefix())
                            .jwtID(ticketId)
                            .issueTime(assertion.getAuthenticationDate())
                            .subject(assertion.getPrincipal().getName());
            assertion.getAttributes().forEach(claims::claim);
            assertion.getPrincipal().getAttributes().forEach(claims::claim);

            if (assertion.getValidUntilDate() != null) {
                claims.expirationTime(assertion.getValidUntilDate());
            } else {
                final ZonedDateTime dt = ZonedDateTime.now().plusSeconds(ticketGrantingTicketExpirationPolicy.getTimeToLive());
                claims.expirationTime(DateTimeUtils.dateOf(dt));
            }
            final JWTClaimsSet claimsSet = claims.build();
            return jwtGenerator.generate(claimsSet.getClaims());
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Response build(final WebApplicationService service, final String serviceTicketId) {
        final Map<String, String> parameters = new HashMap<>();
        if (StringUtils.hasText(serviceTicketId)) {
            parameters.put(CasProtocolConstants.PARAMETER_TICKET, serviceTicketId);
        }

        final HttpServletRequest request = WebUtils.getHttpServletRequestFromRequestAttributes();
        String returnUrl=request.getParameter("returnUrl");
        if(StringUtils.hasText(returnUrl)){
            parameters.put("returnUrl", returnUrl);
        }
        final WebApplicationService finalService = buildInternal(service, parameters);

        final Response.ResponseType responseType = getWebApplicationServiceResponseType();
        if (responseType == Response.ResponseType.POST) {
            return buildPost(finalService, parameters);
        }
        if (responseType == Response.ResponseType.REDIRECT) {
            return buildRedirect(finalService, parameters);
        }

        throw new IllegalArgumentException("Response type is valid. Only POST/REDIRECT are supported");
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final WebApplicationServiceResponseBuilder rhs = (WebApplicationServiceResponseBuilder) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .toHashCode();
    }

    @Override
    public boolean supports(final WebApplicationService service) {
        return service instanceof WebApplicationService;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
