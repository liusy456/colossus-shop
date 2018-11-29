package com.colossus.shiro;


import org.pac4j.cas.authorization.DefaultCasAuthorizationGenerator;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.credentials.authenticator.CasAuthenticator;
import org.pac4j.core.client.DirectClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.extractor.ParameterExtractor;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;

import static org.pac4j.core.client.IndirectClient.ATTEMPTED_AUTHENTICATION_SUFFIX;

public class CustomRestClient extends DirectClient<TokenCredentials, CommonProfile> {
    private CasConfiguration configuration;

    public CustomRestClient() { }

    public CustomRestClient(final CasConfiguration casConfiguration) {
        this.configuration = casConfiguration;
    }

    @Override
    protected TokenCredentials retrieveCredentials(final WebContext context) throws HttpAction {
        init(context);
        String currentUrl = configuration.computeFinalUrl(context.getFullRequestURL(), context);
        //final String loginUrl = configuration.computeFinalLoginUrl(context);

        TokenCredentials credentials =null;
        try {
            credentials = getCredentialsExtractor().extract(context);
        }catch (Exception e){
            return null;
        }

        if (credentials == null) {
            context.setSessionAttribute("casClient" + ATTEMPTED_AUTHENTICATION_SUFFIX, "true");
            return null;
        } else {
            context.setSessionAttribute("casClient" + ATTEMPTED_AUTHENTICATION_SUFFIX, "");
        }
        //如果已经经过token验证了，就不用去cas验证了
        if(credentials.getUserProfile()!=null){
            return credentials;
        }
        // clean url from ticket parameter
        currentUrl = CommonHelper.substringBefore(currentUrl, "?" + CasConfiguration.TICKET_PARAMETER + "=");
        currentUrl = CommonHelper.substringBefore(currentUrl, "&" + CasConfiguration.TICKET_PARAMETER + "=");
        final CasAuthenticator casAuthenticator = new CasAuthenticator(configuration, currentUrl);
        casAuthenticator.init(context);
        casAuthenticator.validate(credentials, context);

        return credentials;
    }


    @Override
    protected void clientInit(final WebContext context) {
        CommonHelper.assertNotNull("configuration", this.configuration);
        CommonHelper.assertTrue(!configuration.isGateway(), "the DirectCasClient can not support gateway to avoid infinite loops");
        configuration.init(context);

        defaultCredentialsExtractor(new ParameterExtractor(CasConfiguration.TICKET_PARAMETER, true, false, getName()));
        // only a fake one for the initialization as we will build a new one with the current url for each request
        super.defaultAuthenticator(new CasAuthenticator(configuration, "fake"));
        addAuthorizationGenerator(new DefaultCasAuthorizationGenerator<>());
    }

    public CasConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final CasConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void defaultAuthenticator(final Authenticator authenticator) {
        throw new TechnicalException("You can not set an Authenticator for the DirectCasClient at startup. A new CasAuthenticator is automatically created for each request");
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "configuration", this.configuration);
    }
}
