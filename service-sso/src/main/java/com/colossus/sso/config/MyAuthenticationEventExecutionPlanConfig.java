package com.colossus.sso.config;

import com.colossus.sso.handler.CustomTwoStepAuthentictionHandler;
import com.colossus.sso.handler.MyAuthenticationHandler;
import com.colossus.sso.mfaProvider.CustomAuthenticatorMultifactorAuthenticationProvider;
import okhttp3.OkHttpClient;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationMetaDataPopulator;
import org.apereo.cas.authentication.metadata.AuthenticationContextAttributeMetaDataPopulator;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.jdbc.JdbcAuthenticationProperties;
import org.apereo.cas.services.MultifactorAuthenticationProvider;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration("MyAuthenticationEventExecutionPlanConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class MyAuthenticationEventExecutionPlanConfig implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;
    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;
    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;
    @Autowired
    @Qualifier("principalFactory")
    private PrincipalFactory principalFactory;

    @Autowired
    private OkHttpClient client;
    @Value("${member.url}")
    private String memberUrl;
    @Value("${env}")
    private String env;

    @Bean
    public AuthenticationHandler myAuthenticationHandler() {
        JdbcAuthenticationProperties.Encode encode=casProperties.getAuthn().getJdbc().getEncode().get(0);


        /*
            Configure the handler by invoking various setter methods.
            Note that you also have full access to the collection of resolved CAS settings.
            Note that each authentication handler may optionally qualify for an 'order`
            as well as a unique name.
        */
        return new MyAuthenticationHandler(encode.getName(),
               servicesManager,
               principalFactory,
                null,dataSource,encode.getAlgorithmName(),encode.getSql(),encode.getPasswordFieldName(),encode.getSaltFieldName(),
                encode.getExpiredFieldName(),encode.getDisabledFieldName(),encode.getNumberOfIterationsFieldName(),
                encode.getNumberOfIterations(),encode.getStaticSalt(),env);
    }

    @Bean
    public MultifactorAuthenticationProvider customAuthenticationProvider() {
        final CustomAuthenticatorMultifactorAuthenticationProvider p = new CustomAuthenticatorMultifactorAuthenticationProvider();
        p.setId("mfa-custom");
        return p;
    }
    @Bean
    public AuthenticationHandler twoStepAuthenticationHandler(){
        return new CustomTwoStepAuthentictionHandler("two-step",servicesManager,principalFactory,null,client,memberUrl);
    }

    @ConditionalOnMissingBean(name = "authenticationContextAttributeMetaDataPopulator")
    @Bean
    public AuthenticationMetaDataPopulator authenticationContextAttributeMetaDataPopulator() {
        return new AuthenticationContextAttributeMetaDataPopulator("authnContextClass",twoStepAuthenticationHandler(),customAuthenticationProvider());
    }
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(myAuthenticationHandler());
        plan.registerAuthenticationHandler(twoStepAuthenticationHandler());
        plan.registerMetadataPopulator(authenticationContextAttributeMetaDataPopulator());
    }
}
