package com.colossus.shiro;

import com.tembin.member.client.api.AuthTokenApi;
import com.tembin.member.client.api.UserApi;
import com.tembin.member.client.vo.AuthTokenVo;
import com.tembin.member.client.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomFormExtractor implements CredentialsExtractor<TokenCredentials> {

    private static final Logger logger= LoggerFactory.getLogger(CustomFormExtractor.class);
    private final String usernameParameter;

    private final String passwordParameter;

    private final String clientName;

    private CasConfiguration configuration;

    private UserApi userApi;

    private AuthTokenApi authTokenApi;

    public CustomFormExtractor(final String usernameParameter, final String passwordParameter, final String clientName) {
        this.usernameParameter = usernameParameter;
        this.passwordParameter = passwordParameter;
        this.clientName = clientName;
    }

    @Override
    public TokenCredentials extract(WebContext context) throws HttpAction {
        String token=context.getRequestHeader("token");
        String clientId=context.getRequestHeader("client");
        if(StringUtils.isNotEmpty(token)&& StringUtils.isNotEmpty(clientId)){
            AuthTokenVo authToken=authTokenApi.findAvailableToken(clientId,token);
            if(authToken!=null){
                String userId=authToken.getOwnerId();
               return createTokenCredential(userId);
            }
            final String username = context.getRequestParameter(this.usernameParameter);
            final String password = context.getRequestParameter(this.passwordParameter);
            if (username == null || password == null) {
                return null;
            }
        }
        final String username = context.getRequestParameter(this.usernameParameter);
        final String password = context.getRequestParameter(this.passwordParameter);
        if (username == null || password == null) {
            throw new RuntimeException("未检查到有用户名密码，不是rest登录，转为正常登录");
        }

        final String userId = (String) userApi.verifyPassword(username, password).getData();
        if (CommonHelper.isBlank(userId)) {
            //TokenCredentials tokenCredentials=requestServiceTicket()
           return null;
        }
       return createTokenCredential(userId);
    }


    private TokenCredentials createTokenCredential(String userId){
        UserVo userInformation = userApi.findUserById(userId);
        if(userInformation!=null){
            CasProfile casProfile=new CasProfile();
            casProfile.setClientName(clientName);
            casProfile.setId(userInformation.getPhone());
            casProfile.addAttribute("id",userId);
            casProfile.addAttribute("phone",userInformation.getPhone());
            casProfile.addAttribute("org_id",userInformation.getOrgId());
            casProfile.addAttribute("email",userInformation.getEmail());
            TokenCredentials tokenCredentials= new TokenCredentials(userId,clientName);
            tokenCredentials.setUserProfile(casProfile);
            return tokenCredentials;
        }
        return null;
    }
    private TokenCredentials requestServiceTicket(final String serviceURL, final String  ticketGrantingTicketId, final WebContext context) {
        HttpURLConnection connection = null;
        try {
            final URL endpointURL = new URL(configuration.computeFinalRestUrl(context));
            final URL ticketURL = new URL(endpointURL, endpointURL.getPath() + "/" + ticketGrantingTicketId);

            connection = HttpUtils.openPostConnection(ticketURL);
            final String payload = HttpUtils.encodeQueryParam("service", serviceURL);

            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), HttpConstants.UTF8_ENCODING));
            out.write(payload);
            out.close();

            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpConstants.OK) {
                try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), HttpConstants.UTF8_ENCODING))) {
                    return new TokenCredentials(in.readLine(), getClass().getSimpleName());
                }
            }
            throw new TechnicalException("Service ticket request for `" + ticketGrantingTicketId + "` failed: " +
                    HttpUtils.buildHttpErrorMessage(connection));
        } catch (final IOException e) {
            throw new TechnicalException(e);
        } finally {
            HttpUtils.closeConnection(connection);
        }
    }

    private String requestTicketGrantingTicket(final String username, final String password, final WebContext context) {
        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.openPostConnection(new URL(this.configuration.computeFinalRestUrl(context)));
            final String payload = HttpUtils.encodeQueryParam(Pac4jConstants.USERNAME, username)
                    + "&" + HttpUtils.encodeQueryParam(Pac4jConstants.PASSWORD, password);

            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), HttpConstants.UTF8_ENCODING));
            out.write(payload);
            out.close();

            final String locationHeader = connection.getHeaderField("location");
            final int responseCode = connection.getResponseCode();
            if (locationHeader != null && responseCode == HttpConstants.CREATED) {
                return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
            }

            logger.debug("Ticket granting ticket request failed: " + locationHeader + " "
                    + responseCode +
                    HttpUtils.buildHttpErrorMessage(connection));

            return null;
        } catch (final IOException e) {
            throw new TechnicalException(e);
        } finally {
            HttpUtils.closeConnection(connection);
        }
    }

    public UserApi getUserApi() {
        return userApi;
    }

    public void setUserApi(UserApi userApi) {
        this.userApi = userApi;
    }

    public AuthTokenApi getAuthTokenApi() {
        return authTokenApi;
    }

    public void setAuthTokenApi(AuthTokenApi authTokenApi) {
        this.authTokenApi = authTokenApi;
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public CasConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final CasConfiguration configuration) {
        this.configuration = configuration;
    }
}
