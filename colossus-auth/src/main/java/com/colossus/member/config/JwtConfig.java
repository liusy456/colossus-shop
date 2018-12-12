package com.colossus.member.config;


import com.colossus.member.utils.JwtAuthenticator;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tlsy1
 * @since 2018-11-08 18:57
 **/
@Configuration
public class JwtConfig {

    @Value("${spring.jwt.signingSecret}")
    private String signingSecret;
    @Value("${spring.jwt.encryptionSecret}")
    private String encryptionSecret;

    @Bean
    public JwtAuthenticator jwtAuthenticator(){
        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(signingSecret, JWSAlgorithm.HS512));
        jwtAuthenticator.addEncryptionConfiguration(new SecretEncryptionConfiguration(encryptionSecret, JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256));
        return jwtAuthenticator;
    }
}
