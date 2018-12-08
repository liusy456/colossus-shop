package com.colossus.shiro.web.controller;

import com.colossus.shiro.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tlsy1
 * @since 2018-12-08 14:28
 **/
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * rest登录拿到jwt，传入参数username，password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        return authService.login(request, response);
    }
}
