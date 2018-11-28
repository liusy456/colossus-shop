package com.colossus.auth.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Tlsy
 * @version commerce 0.0.1
 * @date 2017/4/20  15:42
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {
    private static final long serialVersionUID = 5441806005017171601L;


    public CustomUsernamePasswordToken() {
        super();
    }

    public CustomUsernamePasswordToken(String username, char[] password,
                                 boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }
}
