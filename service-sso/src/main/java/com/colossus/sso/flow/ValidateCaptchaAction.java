package com.colossus.sso.flow;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.jaq.model.v20161123.AfsCheckRequest;
import com.aliyuncs.jaq.model.v20161123.AfsCheckResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apereo.cas.configuration.model.support.captcha.GoogleRecaptchaProperties;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;


public class ValidateCaptchaAction extends AbstractAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCaptchaAction.class);
    private static final ObjectReader READER = new ObjectMapper().findAndRegisterModules().reader();
    private static final String CODE = "captchaError";

    private final GoogleRecaptchaProperties recaptchaProperties;
    private IAcsClient client = null;

    public ValidateCaptchaAction(final GoogleRecaptchaProperties recaptchaProperties) throws ClientException {
        this.recaptchaProperties = recaptchaProperties;
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", recaptchaProperties.getSiteKey(), recaptchaProperties.getSecret());
        client = new DefaultAcsClient(profile);

        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Jaq", "jaq.aliyuncs.com");
    }


    @Override
    protected Event doExecute(final RequestContext requestContext) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(requestContext);
        //final String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        AfsCheckRequest checkRequest = new AfsCheckRequest();
        checkRequest.setPlatform(3);//必填参数，请求来源： 1：Android端； 2：iOS端； 3：PC端及其他
        checkRequest.setSession(request.getParameter("csessionid"));// 必填参数，从前端获取，不可更改
        checkRequest.setSig(request.getParameter("sig"));// 必填参数，从前端获取，不可更改
        checkRequest.setToken(request.getParameter("token"));// 必填参数，从前端获取，不可更改
        checkRequest.setScene(request.getParameter("scene"));// 必填参数，从前端获取，不可更改

        try {
            AfsCheckResponse response = client.getAcsResponse(checkRequest);
            if(response.getErrorCode() == 0 && response.getData() == true) {
                System.out.println("验签通过");
                return null;
            } else {
                System.out.println("验签失败");
            }
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getError(requestContext);
    }

    private Event getError(final RequestContext requestContext) {
        final MessageContext messageContext = requestContext.getMessageContext();
        messageContext.addMessage(new MessageBuilder().error().code(CODE).build());
        return getEventFactorySupport().event(this, CODE);
    }

}
