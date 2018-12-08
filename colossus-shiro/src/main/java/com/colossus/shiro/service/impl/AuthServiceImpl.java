package com.colossus.shiro.service.impl;

import com.colossus.shiro.service.AuthService;
import com.colossus.shiro.utils.ShiroUtil;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tlsy1
 * @since 2018-12-08 14:32
 **/
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CasRestFormClient casRestFormClient;
    @Autowired
    private JwtGenerator jwtGenerator;

    @Value("${spring.cas.prefix_url}")
    private String serviceUrl;

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response) {
        J2EContext context = new J2EContext(request, response);
        CasRestProfile restProfile = (CasRestProfile)ShiroUtil.getProfile();
        //获取ticket
        TokenCredentials tokenCredentials = casRestFormClient.requestServiceTicket(serviceUrl, restProfile, context);
        //根据ticket获取用户信息
        final CasProfile casProfile = casRestFormClient.validateServiceTicket(serviceUrl, tokenCredentials, context);
        //生成jwt token
        return jwtGenerator.generate(casProfile);
    }
}
