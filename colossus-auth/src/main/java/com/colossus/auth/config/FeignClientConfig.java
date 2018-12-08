package com.colossus.auth.config;

import com.colossus.auth.interceptor.JwtTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tlsy1
 * @since 2018-11-08 18:22
 **/
@Configuration
public class FeignClientConfig {

    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor(){
        return new JwtTokenInterceptor();
    }
}
