package com.colossus.sso.config;

import com.colossus.sso.controller.RegisterAutoLoginController;
import com.colossus.sso.controller.SendCaptchaController;
import com.colossus.sso.handler.CustomApplicationServiceResponseBuilder;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import okhttp3.OkHttpClient;
import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.CipherExecutor;
import org.apereo.cas.authentication.AuthenticationSystemSupport;
import org.apereo.cas.authentication.principal.ResponseBuilder;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.core.util.CryptographyProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.token.cipher.TokenTicketCipherExecutor;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.CookieRetrievingCookieGenerator;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration("casCaptchaConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class ControllerConfig {

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    @Qualifier("grantingTicketExpirationPolicy")
    private ExpirationPolicy grantingTicketExpirationPolicy;


    @Autowired
    private CasConfigurationProperties casProperties;


    @Bean
    public RegisterAutoLoginController registerAutoLoginController(@Qualifier("centralAuthenticationService") CentralAuthenticationService centralAuthenticationService,
                                                                   @Qualifier("defaultAuthenticationSystemSupport") AuthenticationSystemSupport authenticationSystemSupport,
                                                                   CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator,
                                                                   @Qualifier("argumentExtractor") ArgumentExtractor argumentExtractor,
                                                                   @Qualifier("stringRedisTemplate") RedisTemplate redisTemplate){
        return new RegisterAutoLoginController(centralAuthenticationService,authenticationSystemSupport,ticketGrantingTicketCookieGenerator,argumentExtractor,redisTemplate,new JdbcTemplate(dataSource));
    }

    @Bean
    public SendCaptchaController sendCaptchaController(OkHttpClient okHttpClient, @Value("${member.url}")String memberUrl){
        return  new SendCaptchaController(okHttpClient,memberUrl);
    }
    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public SimpleUrlHandlerMapping handlerMapping(RegisterAutoLoginController registerAutoLoginController,SendCaptchaController sendCaptchaController) {
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();

        mapping.setOrder(1);
        mapping.setAlwaysUseFullPath(true);
        mapping.setRootHandler(registerAutoLoginController);
        final Map urls = new HashMap();
        urls.put("/register-login", registerAutoLoginController);
        urls.put("/send-captcha",sendCaptchaController);

        mapping.setUrlMap(urls);
        return mapping;
    }

    @Bean
    public CipherExecutor tokenCipherExecutor() {
        final CryptographyProperties crypto = casProperties.getAuthn().getToken().getCrypto();
        return new TokenTicketCipherExecutor(crypto.getEncryption().getKey(), crypto.getSigning().getKey());
    }

    @Bean
    protected JwtGenerator jwtGenerator() {
        final CryptographyProperties crypto = casProperties.getAuthn().getToken().getCrypto();
        return new JwtGenerator(new SecretSignatureConfiguration(crypto.getSigning().getKey(), JWSAlgorithm.HS512), new SecretEncryptionConfiguration(crypto.getEncryption().getKey(), JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256));
    }
    @Bean("webApplicationServiceResponseBuilder")
    public ResponseBuilder<WebApplicationService> webApplicationServiceResponseBuilder() {
        return new CustomApplicationServiceResponseBuilder(servicesManager,tokenCipherExecutor(),grantingTicketExpirationPolicy,jwtGenerator());
    }
}
