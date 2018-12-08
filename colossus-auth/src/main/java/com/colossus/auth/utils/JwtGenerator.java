package com.colossus.auth.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

import static com.nimbusds.jose.JWSAlgorithm.HS512;

/**
 * @author Tlsy1
 * @since 2018-11-08 17:59
 **/

@EnableScheduling
@Configuration
public class JwtGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);

    private String jwtToken;

    @Value("${spring.application.name}")
    private String serviceId;
    @Value("${jwt.signingSecret}")
    private String signingSecret;
    @Value("${jwt.encryptionSecret}")
    private String encryptionSecret;
    @Value("${jwt.header}")
    private String header;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshJwtToken() {
        logger.info("refresh jwt token.....");
        org.pac4j.jwt.profile.JwtGenerator generator = new org.pac4j.jwt.profile.JwtGenerator();
        generator.setSignatureConfiguration(new SecretSignatureConfiguration(signingSecret, HS512));
        generator.setEncryptionConfiguration(new SecretEncryptionConfiguration(encryptionSecret,
                JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256));
        Map<String,Object> claims = new HashMap<>();
        claims.put("serverId", serviceId);

        this.jwtToken = generator.generate(claims);
    }

    public String getHeader() {
        return header;
    }

    public String getJwtToken() {
        if (this.jwtToken == null) {
            this.refreshJwtToken();
        }
        return jwtToken;
    }
}
