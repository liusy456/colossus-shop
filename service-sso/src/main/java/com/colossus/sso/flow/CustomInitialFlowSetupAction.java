package com.colossus.sso.flow;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceAccessStrategy;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.services.UnauthorizedServiceException;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.CookieRetrievingCookieGenerator;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CustomInitialFlowSetupAction extends AbstractAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomInitialFlowSetupAction.class);

    private final CasConfigurationProperties casProperties;
    private final ServicesManager servicesManager;
    private final CookieRetrievingCookieGenerator warnCookieGenerator;
    private final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    private final List<ArgumentExtractor> argumentExtractors;
    private final String oldriverHome;
    private final String moneyHome;

    public CustomInitialFlowSetupAction(final List<ArgumentExtractor> argumentExtractors,
                                  final ServicesManager servicesManager,
                                  final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator,
                                  final CookieRetrievingCookieGenerator warnCookieGenerator,
                                  final CasConfigurationProperties casProperties,
                                        final String oldriverHome,
                                        final String moneyHome) {
        this.argumentExtractors = argumentExtractors;
        this.servicesManager = servicesManager;
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
        this.warnCookieGenerator = warnCookieGenerator;
        this.casProperties = casProperties;
        this.oldriverHome=oldriverHome;
        this.moneyHome=moneyHome;
    }

    @Override
    protected Event doExecute(final RequestContext context) throws Exception {
        configureCookieGenerators(context);
        configureWebflowContext(context);
        configureWebflowContextForService(context);
        return success();
    }

    private void configureWebflowContextForService(final RequestContext context) {
        Service service = WebUtils.getService(this.argumentExtractors, context);
        if(service==null){
            HttpServletRequest request=WebUtils.getHttpServletRequest(context);
            String loginTo=request.getParameter("login_to");
            if(StringUtils.isNotEmpty(loginTo)){
                if(loginTo.equals("0")){
                    request.setAttribute("client", moneyHome);
                }else {
                    request.setAttribute("client",oldriverHome);
                }
                service=WebUtils.getService(this.argumentExtractors,request);
            }
        }

        if (service != null) {
            LOGGER.debug("Placing client in context scope: [{}]", service.getId());

            final RegisteredService registeredService = this.servicesManager.findServiceBy(service);
            if (registeredService != null && registeredService.getAccessStrategy().isServiceAccessAllowed()) {
                LOGGER.debug("Placing registered client [{}] with id [{}] in context scope",
                        registeredService.getServiceId(),
                        registeredService.getId());
                WebUtils.putRegisteredService(context, registeredService);

                final RegisteredServiceAccessStrategy accessStrategy = registeredService.getAccessStrategy();
                if (accessStrategy.getUnauthorizedRedirectUrl() != null) {
                    LOGGER.debug("Placing registered client's unauthorized redirect url [{}] with id [{}] in context scope",
                            accessStrategy.getUnauthorizedRedirectUrl(),
                            registeredService.getServiceId());
                    WebUtils.putUnauthorizedRedirectUrl(context, accessStrategy.getUnauthorizedRedirectUrl());
                }
            }
        } else if (!casProperties.getSso().isMissingService()) {
            LOGGER.warn("No client authentication request is available at [{}]. CAS is configured to disable the flow.",
                    WebUtils.getHttpServletRequest(context).getRequestURL());
            throw new NoSuchFlowExecutionException(context.getFlowExecutionContext().getKey(),
                    new UnauthorizedServiceException("screen.service.required.message", "Service is required"));
        }
        WebUtils.putService(context, service);
    }

    private void configureWebflowContext(final RequestContext context) {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        WebUtils.putTicketGrantingTicketInScopes(context,
                this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request));
        WebUtils.putGoogleAnalyticsTrackingIdIntoFlowScope(context, casProperties.getGoogleAnalytics().getGoogleAnalyticsTrackingId());
        WebUtils.putWarningCookie(context,
                Boolean.valueOf(this.warnCookieGenerator.retrieveCookieValue(request)));
        WebUtils.putGeoLocationTrackingIntoFlowScope(context, casProperties.getEvents().isTrackGeolocation());
        WebUtils.putRecaptchaSiteKeyIntoFlowScope(context, casProperties.getGoogleRecaptcha().getSiteKey());
        WebUtils.putStaticAuthenticationIntoFlowScope(context,
                StringUtils.isNotBlank(casProperties.getAuthn().getAccept().getUsers())
                        || StringUtils.isNotBlank(casProperties.getAuthn().getReject().getUsers()));
        WebUtils.putPasswordManagementEnabled(context, casProperties.getAuthn().getPm().isEnabled());
        WebUtils.putRememberMeAuthenticationEnabled(context, casProperties.getTicket().getTgt().getRememberMe().isEnabled());
    }

    private void configureCookieGenerators(final RequestContext context) {
        final String contextPath = context.getExternalContext().getContextPath();
        final String cookiePath = StringUtils.isNotBlank(contextPath) ? contextPath + '/' : "/";

        if (StringUtils.isBlank(this.warnCookieGenerator.getCookiePath())) {
            LOGGER.info("Setting path for cookies for warn cookie generator to: [{}] ", cookiePath);
            this.warnCookieGenerator.setCookiePath(cookiePath);
        } else {
            LOGGER.debug("Warning cookie path is set to [{}] and path [{}]", this.warnCookieGenerator.getCookieDomain(),
                    this.warnCookieGenerator.getCookiePath());
        }
        if (StringUtils.isBlank(this.ticketGrantingTicketCookieGenerator.getCookiePath())) {
            LOGGER.debug("Setting path for cookies for TGC cookie generator to: [{}] ", cookiePath);
            this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
        } else {
            LOGGER.debug("TGC cookie path is set to [{}] and path [{}]", this.ticketGrantingTicketCookieGenerator.getCookieDomain(),
                    this.ticketGrantingTicketCookieGenerator.getCookiePath());
        }
    }

    public ServicesManager getServicesManager() {
        return servicesManager;
    }
}
