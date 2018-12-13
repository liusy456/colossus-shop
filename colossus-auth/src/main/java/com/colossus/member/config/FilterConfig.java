package com.colossus.member.config;

import com.colossus.member.filter.ServiceAuthFilter;
import com.colossus.member.utils.JwtAuthenticator;
import com.colossus.member.utils.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-11-08 19:29
 **/
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean UpdateUserFilterProxy(@Autowired JwtAuthenticator jwtAuthenticator, @Autowired JwtGenerator jwtGenerator){
        ServiceAuthFilter serviceAuthFilter=new ServiceAuthFilter(jwtGenerator,jwtAuthenticator);
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(serviceAuthFilter);
        List<String> urlPatterns=new ArrayList<String>();
        urlPatterns.add("/client/**");//只拦截微服务的接口
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
