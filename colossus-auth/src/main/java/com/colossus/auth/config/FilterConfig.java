package com.colossus.auth.config;

import com.colossus.auth.filter.ServiceAuthFilter;
import com.colossus.auth.utils.JwtAuthenticator;
import com.colossus.auth.utils.JwtGenerator;
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
    public FilterRegistrationBean UpdateUserFilterProxy( JwtAuthenticator jwtAuthenticator, JwtGenerator jwtGenerator){
        ServiceAuthFilter serviceAuthFilter=new ServiceAuthFilter(jwtGenerator,jwtAuthenticator);
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(serviceAuthFilter);
        List<String> urlPatterns=new ArrayList<String>();
        urlPatterns.add("/client/**");//只拦截微服务的接口
        registrationBean.addInitParameter("exclusions","/actuator,/actuator/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(10);
        return registrationBean;
    }
}
