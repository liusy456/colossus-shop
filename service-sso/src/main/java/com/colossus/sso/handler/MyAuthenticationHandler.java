package com.colossus.sso.handler;

import com.colossus.sso.credential.CustomEcodePasswordCredential;
import com.colossus.sso.utils.AppUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.CodecException;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ByteSource.Util;
import org.apereo.cas.adaptors.jdbc.QueryAndEncodeDatabaseAuthenticationHandler;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.AccountPasswordMustChangeException;
import org.apereo.cas.authentication.handler.PrincipalNameTransformer;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyAuthenticationHandler extends QueryAndEncodeDatabaseAuthenticationHandler {

    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    private PrincipalNameTransformer principalNameTransformer = formUserId -> formUserId;

    private String env;


    private static final Logger logger= LoggerFactory.getLogger(MyAuthenticationHandler.class);
    public MyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order, DataSource dataSource, String algorithmName, String sql, String passwordFieldName, String saltFieldName, String expiredFieldName, String disabledFieldName, String numberOfIterationsFieldName, long numberOfIterations, String staticSalt,String env) {
        super(name, servicesManager, principalFactory, order, dataSource, algorithmName, sql, passwordFieldName, saltFieldName, expiredFieldName, disabledFieldName, numberOfIterationsFieldName, numberOfIterations, staticSalt);
        this.env=env;
    }

    @Override
    protected HandlerResult doAuthentication(final Credential credential) throws GeneralSecurityException, PreventedException {

        UsernamePasswordCredential originalUserPass = (UsernamePasswordCredential) credential;
       // UsernamePasswordCredential userPass = new UsernamePasswordCredential(originalUserPass.getUsername(), originalUserPass.getPassword());

        if(credential instanceof CustomEcodePasswordCredential){
            originalUserPass=(CustomEcodePasswordCredential) credential;
        }


        if (StringUtils.isBlank(originalUserPass.getUsername())) {
            throw new AccountNotFoundException("Username is null.");
        }

        logger.debug("Transforming credential username via [{}]", this.principalNameTransformer.getClass().getName());
        final String transformedUsername = this.principalNameTransformer.transform(originalUserPass.getUsername());
        if (StringUtils.isBlank(transformedUsername)) {
            throw new AccountNotFoundException("Transformed username is null.");
        }

        if (StringUtils.isBlank(originalUserPass.getPassword())) {
            throw new FailedLoginException("Password is null.");
        }

        logger.debug("Attempting to encode credential password via [{}] for [{}]", this.passwordEncoder.getClass().getName(), transformedUsername);
        final String transformedPsw = this.passwordEncoder.encode(originalUserPass.getPassword());
        if (StringUtils.isBlank(transformedPsw)) {
            throw new AccountNotFoundException("Encoded password is null.");
        }

        originalUserPass.setUsername(transformedUsername);
        originalUserPass.setPassword(transformedPsw);

        logger.debug("Attempting authentication internally for transformed credential [{}]", originalUserPass);
        return authenticateUsernamePasswordInternal(originalUserPass, originalUserPass.getPassword());
    }
    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential transformedCredential, String originalPassword) throws GeneralSecurityException, PreventedException {
        if (!StringUtils.isBlank(this.sql) && !StringUtils.isBlank(this.algorithmName) && this.getJdbcTemplate() != null) {
            String username = transformedCredential.getUsername();

            try {
                Map<String, Object> values = this.getJdbcTemplate().queryForMap(this.sql, username);

                String digestedPassword;
                if(transformedCredential instanceof CustomEcodePasswordCredential){
                    digestedPassword=((CustomEcodePasswordCredential) transformedCredential).getEncodePassword();
                }else {
                    digestedPassword = this.digestEncodedPassword(transformedCredential.getPassword(), values);
                }

                String entryptPassword = ((String)values.get(this.passwordFieldName));
                boolean pass=equals(digestedPassword,entryptPassword);
                //测试环境不比对密码
                if("test".equals(env)){
                    pass=true;
                }
                //若不是正式密码则判断是否由零时密码登录
                if(!pass){
                    List<Map<String,Object>> tempPasswords=this.getJdbcTemplate().queryForList("SELECT * FROM temp_password WHERE phone=? and expire_time>=?",username,new Date());
                    for (Map<String, Object> tempPassword : tempPasswords) {
                        String password = (String) tempPassword.get("temp_password");
                        if(transformedCredential.getPassword().equals(password)){
                            pass=true;
                            break;
                        }
                    }
                }
                if (!pass) {
                    throw new FailedLoginException("Password does not match value on record.");
                } else {
                    Object dbDisabled;
                    if (StringUtils.isNotBlank(this.expiredFieldName)) {
                        dbDisabled = values.get(this.expiredFieldName);
                        if (dbDisabled != null && (Boolean.TRUE.equals(BooleanUtils.toBoolean(dbDisabled.toString())) || dbDisabled.equals(Integer.valueOf(1)))) {
                            throw new AccountPasswordMustChangeException("Password has expired");
                        }
                    }

                    if (StringUtils.isNotBlank(this.disabledFieldName)) {
                        dbDisabled = values.get(this.disabledFieldName);
                        if (dbDisabled != null && (Boolean.TRUE.equals(BooleanUtils.toBoolean(dbDisabled.toString())) || dbDisabled.equals(Integer.valueOf(1)))) {
                            throw new AccountDisabledException("Account has been disabled");
                        }
                    }

                    return this.createHandlerResult(transformedCredential, this.principalFactory.createPrincipal(username), (List)null);
                }
            } catch (IncorrectResultSizeDataAccessException var7) {
                if (var7.getActualSize() == 0) {
                    throw new AccountNotFoundException(username + " not found with SQL query");
                } else {
                    throw new FailedLoginException("Multiple records found for " + username);
                }
            } catch (DataAccessException var8) {
                throw new PreventedException("SQL exception while executing query for " + username, var8);
            }
        } else {
            throw new GeneralSecurityException("Authentication handler is not configured correctly");
        }
    }

    @Override
    protected String digestEncodedPassword(String encodedPassword, Map<String, Object> values) {
        ConfigurableHashService hashService = new DefaultHashService();
        if (StringUtils.isNotBlank(this.staticSalt)) {
            hashService.setPrivateSalt(Util.bytes(this.staticSalt));
        }

        hashService.setHashAlgorithmName(this.algorithmName);
        Long numOfIterations = this.numberOfIterations;
        String dynaSalt;

        hashService.setHashIterations(numOfIterations.intValue());
        if (!values.containsKey(this.saltFieldName)) {
            throw new RuntimeException("Specified field name for salt does not exist in the results");
        } else {
            dynaSalt =(String) values.get(this.saltFieldName);
            //HashRequest request = (new HashRequest.Builder()).setSalt(dynaSalt).setSource(encodedPassword).build();
            return AppUtils.hashPassword(encodedPassword,dynaSalt);
        }
    }

    private Hash hashProvidedCredentials(Object credentials, Object salt, int hashIterations, String hashAlgorithmName) {
        return new SimpleHash(hashAlgorithmName, credentials, ByteSource.Util.bytes(salt), hashIterations);
    }

    protected String  getCredentials(String  credentials,String hashAlgorithmName) {


        byte[] storedBytes = toBytes(credentials);


            //account.credentials were a char[] or String, so
            //we need to do text decoding first:
            storedBytes = Hex.decode(storedBytes);

        AbstractHash hash = new SimpleHash(hashAlgorithmName);
        hash.setBytes(storedBytes);
        return hash.toHex();
    }



    protected boolean equals(String  tokenCredentials, String  accountCredentials) {
        if (logger.isDebugEnabled()) {
            logger.debug("Performing credentials equality check for tokenCredentials of type [" +
                    tokenCredentials.getClass().getName() + " and accountCredentials of type [" +
                    accountCredentials.getClass().getName() + "]");
        }
        if (isByteSource(tokenCredentials) && isByteSource(accountCredentials)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Both credentials arguments can be easily converted to byte arrays.  Performing " +
                        "array equals comparison");
            }
            byte[] tokenBytes = toBytes(tokenCredentials);
            byte[] accountBytes = toBytes(accountCredentials);
            return MessageDigest.isEqual(tokenBytes, accountBytes);
        } else {
            return accountCredentials.equals(tokenCredentials);
        }
    }

    private byte[] toBytes(String  source) {
        try {
            return source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            String msg = "Unable to convert source [" + source + "] to byte array using encoding '" + "UTF-8" + "'";
            throw new CodecException(msg, var4);
        }
    }

    private boolean isByteSource(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    protected byte[] objectToBytes(Object o) {
        String msg = "The " + this.getClass().getName() + " implementation only supports conversion to " + "byte[] if the source is of type byte[], char[], String, " + ByteSource.class.getName() + " File or InputStream.  The instance provided as a method " + "argument is of type [" + o.getClass().getName() + "].  If you would like to convert " + "this argument type to a byte[], you can 1) convert the argument to one of the supported types " + "yourself and then use that as the method argument or 2) subclass " + this.getClass().getName() + "and override the objectToBytes(Object o) method.";
        throw new CodecException(msg);
    }


}
