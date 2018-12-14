package com.colossus.sso.config;

import com.colossus.sso.flow.CustomInitialFlowSetupAction;
import com.colossus.sso.flow.CustomTwoStepTokenAction;
import okhttp3.OkHttpClient;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.webflow.execution.Action;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(CasConfigurationProperties.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class FlowActionConfig {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    @Qualifier("ticketGrantingTicketCookieGenerator")
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("warnCookieGenerator")
    private CookieRetrievingCookieGenerator warnCookieGenerator;

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
    }


    @RefreshScope
    @Bean("initialFlowSetupAction")
    @Autowired
    public Action initialFlowSetupAction(@Qualifier("argumentExtractor") final ArgumentExtractor argumentExtractor,
                                         @Value("${oldriver.home}")String oldriverHome,
                                         @Value("${money.home}")String moneyHome) {
        return new CustomInitialFlowSetupAction(Collections.singletonList(argumentExtractor),
                servicesManager,
                ticketGrantingTicketCookieGenerator,
                warnCookieGenerator, casProperties,oldriverHome,moneyHome);
    }

    @Bean("twoStepTokenAction")
    @Autowired
    public Action twoStepTokenAction(@Value("${member.url}")String memberUrl){
        return new CustomTwoStepTokenAction(okHttpClient(),memberUrl);
    }
}
