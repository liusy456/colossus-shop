package com.colossus.auth.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.*;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.jwt.config.encryption.EncryptionConfiguration;
import org.pac4j.jwt.config.signature.SignatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tlsy1
 * @since 2018-11-08 18:47
 **/
public class JwtAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticator.class);

    private List<EncryptionConfiguration> encryptionConfigurations = new ArrayList<>();

    private List<SignatureConfiguration> signatureConfigurations = new ArrayList<>();

    public void validate(String token) throws CredentialsException {
        try {
            // Parse the token
            JWT jwt = JWTParser.parse(token);

            if (jwt instanceof PlainJWT) {
                if (signatureConfigurations.isEmpty()) {
                    logger.debug("JWT is not signed and no signature configurations -> verified");
                } else {
                    throw new CredentialsException("A non-signed JWT cannot be accepted as signature configurations have been defined");
                }
            } else {

                SignedJWT signedJWT = null;
                if (jwt instanceof SignedJWT) {
                    signedJWT = (SignedJWT) jwt;
                }

                // encrypted?
                if (jwt instanceof EncryptedJWT) {
                    logger.debug("JWT is encrypted");

                    final EncryptedJWT encryptedJWT = (EncryptedJWT) jwt;
                    boolean found = false;
                    final JWEHeader header = encryptedJWT.getHeader();
                    final JWEAlgorithm algorithm = header.getAlgorithm();
                    final EncryptionMethod method = header.getEncryptionMethod();
                    for (final EncryptionConfiguration config : encryptionConfigurations) {
                        if (config.supports(algorithm, method)) {
                            logger.debug("Using encryption configuration: {}", config);
                            try {
                                config.decrypt(encryptedJWT);
                                signedJWT = encryptedJWT.getPayload().toSignedJWT();
                                if (signedJWT != null) {
                                    jwt = signedJWT;
                                }
                                found = true;
                                break;
                            } catch (final JOSEException e) {
                                logger.debug("Decryption fails with encryption configuration: {}, passing to the next one", config);
                            }
                        }
                    }
                    if (!found) {
                        throw new CredentialsException("No encryption algorithm found for JWT: " + token);
                    }
                }

                // signed?
                if (signedJWT != null) {
                    logger.debug("JWT is signed");

                    boolean verified = false;
                    boolean found = false;
                    final JWSAlgorithm algorithm = signedJWT.getHeader().getAlgorithm();
                    for (final SignatureConfiguration config : signatureConfigurations) {
                        if (config.supports(algorithm)) {
                            logger.debug("Using signature configuration: {}", config);
                            try {
                                verified = config.verify(signedJWT);
                                found = true;
                                if (verified) {
                                    break;
                                }
                            } catch (final JOSEException e) {
                                logger.debug("Verification fails with signature configuration: {}, passing to the next one", config);
                            }
                        }
                    }
                    if (!found) {
                        throw new CredentialsException("No signature algorithm found for JWT: " + token);
                    }
                    if (!verified) {
                        throw new CredentialsException("JWT verification failed: " + token);
                    }
                }
            }

        } catch (final ParseException e) {
            throw new CredentialsException("Cannot decrypt / verify JWT", e);
        }
    }


    public void addSignatureConfiguration(final SignatureConfiguration signatureConfiguration) {
        CommonHelper.assertNotNull("signatureConfiguration", signatureConfiguration);
        signatureConfigurations.add(signatureConfiguration);
    }


    public void addEncryptionConfiguration(final EncryptionConfiguration encryptionConfiguration) {
        CommonHelper.assertNotNull("encryptionConfiguration", encryptionConfiguration);
        encryptionConfigurations.add(encryptionConfiguration);
    }

}
