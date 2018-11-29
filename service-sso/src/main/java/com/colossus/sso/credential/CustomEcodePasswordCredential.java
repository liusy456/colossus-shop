package com.colossus.sso.credential;

import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;

public class CustomEcodePasswordCredential extends RememberMeUsernamePasswordCredential {
    private static final long serialVersionUID = 8120716614898622567L;

    private String encodePassword;

    public String getEncodePassword() {
        return encodePassword;
    }

    public void setEncodePassword(String encodePassword) {
        this.encodePassword = encodePassword;
    }
}
