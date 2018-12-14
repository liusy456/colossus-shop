package com.colossus.sso.controller;

import com.colossus.sso.utils.AppUtils;
import okhttp3.OkHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;

public class SendCaptchaController extends AbstractController {

    private OkHttpClient okHttpClient;
    private String memberUrl;
    private static final Logger logger= LoggerFactory.getLogger(SendCaptchaController.class);
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String phone=request.getParameter("phone");
        logger.info("重发验证码phone：{}",phone);
        if(StringUtils.isNotEmpty(phone)){
            try {
                AppUtils.sendCaptcha(phone,"二步认证",memberUrl,okHttpClient);
            }catch (Exception e){
                response.setStatus(500);
                response.resetBuffer();
                response.setContentType("application/json;charset=UTF-8");
                IOUtils.copy(new StringReader("{\"errorId\":\"\",\"messages\":[\""+e.getMessage()+"\"]}"),response.getOutputStream(),"utf-8");
                response.flushBuffer();
            }
        }

        return null;
    }

    public SendCaptchaController(OkHttpClient okHttpClient, String memberUrl){
        this.okHttpClient = okHttpClient;
        this.memberUrl = memberUrl;
    }

}
