package com.colossus.shiro;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tembin.common.base.ApiError;
import org.pac4j.cas.authorization.DefaultCasAuthorizationGenerator;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.credentials.authenticator.CasAuthenticator;
import org.pac4j.cas.credentials.extractor.TicketAndLogoutRequestExtractor;
import org.pac4j.cas.logout.CasLogoutHandler;
import org.pac4j.cas.redirect.CasRedirectActionBuilder;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.http.AjaxRequestResolver;
import org.pac4j.core.http.DefaultAjaxRequestResolver;
import org.pac4j.core.logout.CasLogoutActionBuilder;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.redirect.RedirectAction;
import org.pac4j.core.util.CommonHelper;

import java.util.UUID;

public class CustomCasClient extends IndirectClient<TokenCredentials, CommonProfile> {

    private CasConfiguration configuration = new CasConfiguration();

    private AjaxRequestResolver ajaxRequestResolver = new DefaultAjaxRequestResolver();

    public CustomCasClient() { }

    public CustomCasClient(final CasConfiguration configuration) {
        setConfiguration(configuration);
    }

    @Override
    protected void clientInit(final WebContext context) {
        CommonHelper.assertNotNull("configuration", configuration);
        configuration.setUrlResolver(this.getUrlResolver());
        configuration.init(context);

        defaultRedirectActionBuilder(new CasRedirectActionBuilder(configuration, callbackUrl));
        defaultCredentialsExtractor(new TicketAndLogoutRequestExtractor(configuration, getName()));
        defaultAuthenticator(new CasAuthenticator(configuration, callbackUrl));
        defaultLogoutActionBuilder(new CasLogoutActionBuilder<>(configuration.getPrefixUrl() + "logout", configuration.getPostLogoutUrlParameter()));
        addAuthorizationGenerator(new DefaultCasAuthorizationGenerator<>());
    }

    @Override
    public RedirectAction getRedirectAction(WebContext context) throws HttpAction{
        init(context);
        // it's an AJAX request -> unauthorized (with redirection url in header)
        if (ajaxRequestResolver.isAjax(context)) {
            logger.info("AJAX request detected -> returning 401");
            RedirectAction action = getRedirectActionBuilder().redirect(context);
            context.setSessionAttribute(Pac4jConstants.REQUESTED_URL, "");
            throw HttpAction.unauthorized("AJAX request -> 401", context, null, action.getLocation());
        }
        // authentication has already been tried -> unauthorized
        final String attemptedAuth = (String) context.getSessionAttribute(getName() + ATTEMPTED_AUTHENTICATION_SUFFIX);
        if (CommonHelper.isNotBlank(attemptedAuth)) {
            context.setSessionAttribute(getName() + ATTEMPTED_AUTHENTICATION_SUFFIX, "");
            context.setSessionAttribute(Pac4jConstants.REQUESTED_URL, "");
            ApiError apiError=new ApiError();
            apiError.setErrorId(getUUID());
            apiError.setMessages(Lists.newArrayList("用户名或密码错误!"));
            context.setResponseContentType("application/json;charset=utf-8");
            context.writeResponseContent(JSON.toJSONString(apiError));

            throw  HttpAction.status("authentication forbidden",403,new CustomContext(((J2EContext)context).getRequest(),((J2EContext)context).getResponse()));
        }

        return getRedirectActionBuilder().redirect(context);
    }

    private final String getUUID() {
        UUID uuid = UUID.randomUUID();
        if (uuid.toString().length() <= 32) {
            return uuid.toString();
        } else {
            return uuid.toString().replace("-", "");
        }
    }

    @Override
    public void notifySessionRenewal(final String oldSessionId, final WebContext context) {
        final CasLogoutHandler casLogoutHandler = configuration.getLogoutHandler();
        if (casLogoutHandler != null) {
            casLogoutHandler.renewSession(oldSessionId, context);
        }
    }

    public CasConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final CasConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "name", getName(), "callbackUrl", this.callbackUrl,
                "urlResolver", this.urlResolver, "ajaxRequestResolver", getAjaxRequestResolver(),
                "redirectActionBuilder", getRedirectActionBuilder(), "credentialsExtractor", getCredentialsExtractor(),
                "authenticator", getAuthenticator(), "profileCreator", getProfileCreator(),
                "logoutActionBuilder", getLogoutActionBuilder(), "configuration", this.configuration);
    }
}
