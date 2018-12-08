package com.colossus.shiro.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tlsy1
 * @since 2018-12-08 14:31
 **/
public interface AuthService {

    String login(HttpServletRequest request, HttpServletResponse response);
}
