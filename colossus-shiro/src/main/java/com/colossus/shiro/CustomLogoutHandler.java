package com.colossus.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.cas.logout.CasLogoutHandler;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.store.GuavaStore;
import org.pac4j.core.store.Store;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CustomLogoutHandler<C extends WebContext> implements CasLogoutHandler<C> {
    protected static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    private Store<String, Object> store = new GuavaStore<>(10000, 30, TimeUnit.MINUTES);

    private boolean destroySession;
    private DefaultWebSessionManager sessionManager;

    public CustomLogoutHandler() {
    }

    public CustomLogoutHandler(final Store<String, Object> store,DefaultWebSessionManager sessionManager) {
        this.store = store;
        this.sessionManager=sessionManager;
    }

    public DefaultWebSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(DefaultWebSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    @Override
    public void recordSession(final C context, final String ticket) {
        final SessionStore sessionStore = context.getSessionStore();
        if (sessionStore == null) {
            logger.error("No session store available for this web context");
        } else {
            final String sessionId = sessionStore.getOrCreateSessionId(context);
            store.set(sessionId, ticket);
            store.set(ticket,sessionId);
        }
    }

    @Override
    public void destroySessionFront(final C context, final String ticket) {
        String trackSessionId=(String )store.get(ticket);
        if(StringUtils.isEmpty(trackSessionId)){
            logger.error("No trackSessionId available for this web context");
            return;
        }
        store.remove(ticket);
        final SessionStore sessionStore = context.getSessionStore();
        if (sessionStore == null) {
            logger.error("No session store available for this web context");
        } else {
            //final String currentSessionId = sessionStore.getOrCreateSessionId(context);
            logger.debug("trackSessionId: {}", trackSessionId);
            final String sessionToTicket = (String) store.get(trackSessionId);
            logger.debug("-> ticket: {}，sessionTicket:{}", ticket,sessionToTicket);
            store.remove(trackSessionId);

            if (CommonHelper.areEquals(ticket, sessionToTicket)) {
                // remove profiles
                final ProfileManager manager = new ProfileManager(context);
                manager.logout();
                logger.debug("destroy the user profiles");
                if(destroySession){
                    SessionKey sessionKey=new DefaultSessionKey(trackSessionId);
                    sessionManager.stop(sessionKey);
                }
            } else {
                logger.error("The user profiles (and session) can not be destroyed for CAS front channel logout because the provided ticket is not the same as the one linked to the current session");
            }
        }
    }

    protected void destroy(final C context, final SessionStore sessionStore, final String channel) {
        // remove profiles
        final ProfileManager manager = new ProfileManager(context);
        manager.logout();
        logger.debug("destroy the user profiles");
        // and optionally the web session
        if (destroySession) {
            logger.debug("destroy the whole session");
            final boolean invalidated = sessionStore.destroySession(context);
            if (!invalidated) {
                logger.error("The session has not been invalidated for {} channel logout", channel);
            }
        }
    }

    @Override
    public void destroySessionBack(final C context, final String ticket) {
        destroySessionFront(context,ticket);
    }

    @Override
    public void renewSession(final String oldSessionId, final C context) {
        final String ticket = (String) store.get(oldSessionId);
        logger.debug("oldSessionId: {} -> ticket: {}", oldSessionId, ticket);
        if (ticket != null) {
            store.remove(ticket);
            store.remove(oldSessionId);
            recordSession(context, ticket);
        }
    }

    public Store<String, Object> getStore() {
        return store;
    }

    public void setStore(final Store<String, Object> store) {
        this.store = store;
    }

    public boolean isDestroySession() {
        return destroySession;
    }

    public void setDestroySession(final boolean destroySession) {
        this.destroySession = destroySession;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "store", store, "destroySession", destroySession);
    }
}
