package com.colossus.shiro;

import io.buji.pac4j.profile.ShiroProfileManager;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.core.profile.CommonProfile;

import static org.pac4j.core.util.CommonHelper.*;

public class CustomCallbackLogic<R, C extends WebContext> extends DefaultCallbackLogic<R,C> {

    public CustomCallbackLogic() {
        super();
        setProfileManagerFactory(ShiroProfileManager::new);
    }

    @Override
    public R perform(C context, Config config, HttpActionAdapter<R, C> httpActionAdapter, String inputDefaultUrl, Boolean inputMultiProfile, Boolean inputRenewSession) {
        logger.debug("=== CALLBACK ===");

        // default values
        final String defaultUrl;
        if (inputDefaultUrl == null) {
            defaultUrl = Pac4jConstants.DEFAULT_URL_VALUE;
        } else {
            defaultUrl = inputDefaultUrl;
        }
        final boolean multiProfile;
        if (inputMultiProfile == null) {
            multiProfile = false;
        } else {
            multiProfile = inputMultiProfile;
        }
        final boolean renewSession;
        if (inputRenewSession == null) {
            renewSession = true;
        } else {
            renewSession = inputRenewSession;
        }

        // checks
        assertNotNull("context", context);
        assertNotNull("config", config);
        assertNotNull("httpActionAdapter", httpActionAdapter);
        assertNotBlank(Pac4jConstants.DEFAULT_URL, defaultUrl);
        final Clients clients = config.getClients();
        assertNotNull("clients", clients);

        // logic
        final Client client = clients.findClient(context);
        logger.debug("client: {}", client);
        assertNotNull("client", client);
        assertTrue(client instanceof IndirectClient, "only indirect clients are allowed on the callback url");

        HttpAction action;
        try {
            final Credentials credentials = client.getCredentials(context);
            logger.debug("credentials: {}", credentials);

            final CommonProfile profile = client.getUserProfile(credentials, context);
            logger.debug("profile: {}", profile);
            saveUserProfile(context, config, profile, multiProfile, renewSession);
            action = redirectToOriginallyRequestedUrl(context, defaultUrl);

        } catch (final HttpAction e) {
            logger.debug("extra HTTP action required in callback: {}", e.getCode());
            action = e;
        }

        return httpActionAdapter.adapt(action.getCode(), context);
    }

    @Override
    protected void saveUserProfile(C context, Config config, CommonProfile profile, boolean multiProfile, boolean renewSession) {
        super.saveUserProfile(context, config, profile, multiProfile, renewSession);
    }

    @Override
    protected void renewSession(C context, Config config) {
        super.renewSession(context, config);
    }

    @Override
    protected HttpAction redirectToOriginallyRequestedUrl(C context, String defaultUrl) {
        final String requestedUrl = (String) context.getSessionAttribute(Pac4jConstants.REQUESTED_URL);
        final String returnUrl = context.getRequestParameter("returnUrl");
        String redirectUrl = defaultUrl;
        if (isNotBlank(requestedUrl)) {
            context.setSessionAttribute(Pac4jConstants.REQUESTED_URL, null);
            redirectUrl = requestedUrl;
        }
        if(isNotBlank(returnUrl)){
            redirectUrl = returnUrl;
        }
        logger.debug("redirectUrl: {}", redirectUrl);
        return HttpAction.redirect("redirect", context, redirectUrl);
    }
}
