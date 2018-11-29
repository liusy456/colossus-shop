package com.colossus.shiro;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tembin.common.base.ApiError;
import io.buji.pac4j.profile.ShiroProfileManager;
import org.pac4j.core.authorization.checker.AuthorizationChecker;
import org.pac4j.core.authorization.checker.DefaultAuthorizationChecker;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.DirectClient;
import org.pac4j.core.client.finder.ClientFinder;
import org.pac4j.core.client.finder.DefaultClientFinder;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityGrantedAccessAdapter;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.core.matching.DefaultMatchingChecker;
import org.pac4j.core.matching.MatchingChecker;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.pac4j.core.util.CommonHelper.*;

public class CustomSecurityLogic<R, C extends WebContext> extends DefaultSecurityLogic<R,C> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ClientFinder clientFinder = new DefaultClientFinder();

    private AuthorizationChecker authorizationChecker = new DefaultAuthorizationChecker();

    private MatchingChecker matchingChecker = new DefaultMatchingChecker();


    public CustomSecurityLogic() {
        super();
        setProfileManagerFactory(ShiroProfileManager::new);
    }

    @Override
    public R perform(final C context, final Config config, final SecurityGrantedAccessAdapter<R, C> securityGrantedAccessAdapter, final HttpActionAdapter<R, C> httpActionAdapter,
                     final String clients, final String authorizers, final String matchers, final Boolean inputMultiProfile, final Object... parameters) {

        logger.debug("=== SECURITY ===");

        // default value
        final boolean multiProfile;
        if (inputMultiProfile == null) {
            multiProfile = false;
        } else {
            multiProfile = inputMultiProfile;
        }

        // checks
        assertNotNull("context", context);
        assertNotNull("config", config);
        assertNotNull("httpActionAdapter", httpActionAdapter);
        assertNotNull("clientFinder", clientFinder);
        assertNotNull("authorizationChecker", authorizationChecker);
        assertNotNull("matchingChecker", matchingChecker);
        final Clients configClients = config.getClients();
        assertNotNull("configClients", configClients);

        // logic
        HttpAction action;
        try {

            if(context.getPath().contains("api/v1/internal/call/")){
                logger.debug("内部服务器调用,url: {}", context.getFullRequestURL());
                return securityGrantedAccessAdapter.adapt(context, parameters);
            }

            logger.debug("url: {}", context.getFullRequestURL());
            logger.debug("matchers: {}", matchers);
            if (matchingChecker.matches(context, matchers, config.getMatchers())) {

                logger.debug("clients: {}", clients);
                final List<Client> currentClients = clientFinder.find(configClients, context, clients);
                logger.debug("currentClients: {}", currentClients);

                final boolean loadProfilesFromSession = loadProfilesFromSession(context, currentClients);
                logger.debug("loadProfilesFromSession: {}", loadProfilesFromSession);
                final ProfileManager manager = getProfileManager(context, config);
                List<CommonProfile> profiles = manager.getAll(loadProfilesFromSession);
                logger.debug("profiles: {}", profiles);

                // no profile and some current clients
                if (isEmpty(profiles) && isNotEmpty(currentClients)) {
                    boolean updated = false;
                    // loop on all clients searching direct ones to perform authentication
                    for (final Client currentClient : currentClients) {
                        if (currentClient instanceof DirectClient) {
                            logger.debug("Performing authentication for direct client: {}", currentClient);

                            final Credentials credentials = currentClient.getCredentials(context);
                            logger.debug("credentials: {}", credentials);
                            final CommonProfile profile = currentClient.getUserProfile(credentials, context);
                            logger.debug("profile: {}", profile);
                            if (profile != null) {
                                final boolean saveProfileInSession = saveProfileInSession(context, currentClients, (DirectClient) currentClient, profile);
                                logger.debug("saveProfileInSession: {} / multiProfile: {}", saveProfileInSession, multiProfile);
                                manager.save(saveProfileInSession, profile, multiProfile);
                                updated = true;
                                if (!multiProfile) {
                                    break;
                                }
                            }
                        }
                    }
                    if (updated) {
                        profiles = manager.getAll(loadProfilesFromSession);
                        logger.debug("new profiles: {}", profiles);
                    }
                }

                // we have profile(s) -> check authorizations
                if (isNotEmpty(profiles)) {
                    logger.debug("authorizers: {}", authorizers);
                    if (authorizationChecker.isAuthorized(context, profiles, authorizers, config.getAuthorizers())) {
                        logger.debug("authenticated and authorized -> grant access");
                        return securityGrantedAccessAdapter.adapt(context, parameters);
                    } else {
                        logger.debug("forbidden");
                        action = forbidden(context, currentClients, profiles, authorizers);
                    }
                } else {
                    if (startAuthentication(context, currentClients)) {
                        logger.debug("Starting authentication");
                        saveRequestedUrl(context, currentClients);
                        action = redirectToIdentityProvider(context, currentClients);
                    } else {
                        logger.debug("unauthorized");
                        action = unauthorized(context, currentClients);
                    }
                }

            } else {

                logger.debug("no matching for this request -> grant access");
                return securityGrantedAccessAdapter.adapt(context, parameters);
            }

        } catch (final HttpAction e) {
            logger.debug("extra HTTP action required in security: {}", e.getCode());
            action = e;
        } catch (final TechnicalException e) {
            throw e;
        } catch (final Throwable e) {
            throw new TechnicalException(e);
        }

        return httpActionAdapter.adapt(action.getCode(), context);
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
    protected HttpAction forbidden(C context, List<Client> currentClients, List<CommonProfile> profiles, String authorizers) throws HttpAction {
        ApiError apiError=new ApiError();
        apiError.setErrorId(getUUID());
        apiError.setMessages(Lists.newArrayList("authentication forbidden"));
        context.setResponseContentType("application/json");
        context.writeResponseContent(JSON.toJSONString(apiError));
        return HttpAction.status("authentication forbidden",403,new CustomContext(((J2EContext)context).getRequest(),((J2EContext)context).getResponse()));

    }
}
