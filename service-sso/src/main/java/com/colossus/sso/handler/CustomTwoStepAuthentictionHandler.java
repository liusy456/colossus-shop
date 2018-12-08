package com.colossus.sso.handler;

import com.colossus.sso.credential.CustomTwoStepCredential;
import com.colossus.sso.exception.ServiceException;
import okhttp3.*;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.List;

public class CustomTwoStepAuthentictionHandler extends AbstractPreAndPostProcessingAuthenticationHandler {


    private OkHttpClient client;
    private String memberUrl;
    public CustomTwoStepAuthentictionHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order, OkHttpClient client,String memberUrl) {
        super(name, servicesManager, principalFactory, order);
        this.client=client;
        this.memberUrl=memberUrl;
    }

    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        //todo 验证手机验证码
        CustomTwoStepCredential customTwoStepCredential=(CustomTwoStepCredential) credential;
        String mobileCode=customTwoStepCredential.getToken();
        String phone=customTwoStepCredential.getPhone();
        if(verifyCaptcha(phone,mobileCode)){
           return this.createHandlerResult(credential, this.principalFactory.createPrincipal(credential.getId()), (List)null);
            //throw new FailedLoginException("authCode does not match value on record.");
        }else {
            throw new FailedLoginException("authCode does not match value on record.");
        }
    }

    private Response execute1(Request request){
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    private boolean verifyCaptcha(String phoneNumber, String captcha) {
        RequestBody formBody = new FormBody.Builder()
                .add("phone", phoneNumber)
                .add("mobileCode",captcha)
                .add("reset","true")
                .build();
        Request request=new Request.Builder().url(memberUrl+"/api/member/validate/validateMobileCode").post(formBody).build();
        try {
            return execute1(request).isSuccessful();
        }catch (ServiceException e){
            return false;
        }
    }

    @Override
    public boolean supports(Credential credential) {
        return CustomTwoStepCredential.class.isAssignableFrom(credential.getClass());
    }
}
