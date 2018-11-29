package com.colossus.shiro;

import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.session.SessionStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomContext extends J2EContext {

    public CustomContext(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public CustomContext(HttpServletRequest request, HttpServletResponse response, SessionStore<J2EContext> sessionStore) {
        super(request, response, sessionStore);
    }

    @Override
    public void setResponseStatus(int code) {
        getResponse().setStatus(code);
    }
}
