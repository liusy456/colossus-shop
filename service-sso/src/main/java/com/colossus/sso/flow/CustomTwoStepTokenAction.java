package com.colossus.sso.flow;

import com.colossus.sso.utils.AppUtils;
import okhttp3.OkHttpClient;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class CustomTwoStepTokenAction extends AbstractAction {

    private OkHttpClient okHttpClient;
    private String memberUrl;

    public CustomTwoStepTokenAction(OkHttpClient okHttpClient, String memberUrl) {
        this.okHttpClient = okHttpClient;
        this.memberUrl = memberUrl;
    }

    @Override
    protected Event doExecute(RequestContext requestContext) throws Exception {

        final Principal principal = WebUtils.getAuthentication(requestContext).getPrincipal();
        String phone=principal.getId();
        try {
            AppUtils.sendCaptcha(phone,"二步认证",memberUrl,okHttpClient);
        }catch (Exception e){

            final MessageContext messageContext = requestContext.getMessageContext();
            messageContext.addMessage(new MessageBuilder().error()
                    .code("authenticationFailure.".concat(e.getClass().getSimpleName())).build());
            return success(phone);
        }

        return success(phone);
    }
}
