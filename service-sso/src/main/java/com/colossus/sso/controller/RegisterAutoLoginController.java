package com.colossus.sso.controller;

import com.colossus.sso.credential.CustomEcodePasswordCredential;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.AuthenticationResult;
import org.apereo.cas.authentication.AuthenticationSystemSupport;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.CookieRetrievingCookieGenerator;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;


public class RegisterAutoLoginController extends AbstractController{

    private static Logger logger= LoggerFactory.getLogger(RegisterAutoLoginController.class);

    private CentralAuthenticationService centralAuthenticationService;

    private AuthenticationSystemSupport authenticationSystemSupport;

    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    private ArgumentExtractor argumentExtractor;

    private RedisTemplate redisTemplate;
    private JdbcTemplate jdbcTemplate;

    public RegisterAutoLoginController() {
    }
    public RegisterAutoLoginController(CentralAuthenticationService centralAuthenticationService,
                                       AuthenticationSystemSupport authenticationSystemSupport,
                                       CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator,
                                       ArgumentExtractor argumentExtractor, RedisTemplate redisTemplate,
                                       JdbcTemplate jdbcTemplate){
        this.centralAuthenticationService=centralAuthenticationService;
        this.authenticationSystemSupport=authenticationSystemSupport;
        this.ticketGrantingTicketCookieGenerator=ticketGrantingTicketCookieGenerator;
        this.argumentExtractor=argumentExtractor;
        this.redisTemplate=redisTemplate;
        this.jdbcTemplate=jdbcTemplate;
    }

    /**
     * 获取用户名密码,验证有效性,生成相关票据并绑定注册,添加cookie
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequestInternal(HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception
    {
        ModelAndView signinView=new ModelAndView();
        String token=request.getParameter("token");
        String returnUrl=request.getParameter("returnUrl");
        if(StringUtils.isNotEmpty(returnUrl)){
            HttpSession session=request.getSession();
            session.setAttribute("pac4jRequestedUrl",returnUrl);
        }
        String userId=(String)redisTemplate.opsForValue().get("login_token_"+token);
        String sql="select * from user where id =?";
        if(StringUtils.isNotEmpty(userId)){
            Map<String, Object> values=jdbcTemplate.queryForMap(sql,userId);
            String password=(String)values.get("password");
            String username=(String)values.get("phone");
            bindTicketGrantingTicket(username, password, false,request, response);
            redisTemplate.delete("login_token_"+token);
        }
        String viewName=getSignInView(request);
        signinView.setViewName(viewName);

        return signinView;
    }


    /**
     * 具体生成相关票据并绑定注册,添加cookie实现方法
     * @param loginName
     * @param loginPassword
     * @param request
     * @param response
     */
    private void bindTicketGrantingTicket(String loginName, String loginPassword, boolean rememberMe, HttpServletRequest request, HttpServletResponse response){
        try {
            //UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();  //4.0之前
            CustomEcodePasswordCredential credentials = new CustomEcodePasswordCredential();
            credentials.setUsername(loginName);
            credentials.setPassword(loginPassword);
            credentials.setEncodePassword(loginPassword);
            credentials.setRememberMe(rememberMe);
            WebApplicationService service= WebUtils.getService(Collections.singletonList(argumentExtractor),request);
            AuthenticationResult authenticationResult=authenticationSystemSupport.handleAndFinalizeSingleAuthenticationTransaction(service,credentials);
            TicketGrantingTicket ticketGrantingTicket = centralAuthenticationService.createTicketGrantingTicket(authenticationResult);
            ticketGrantingTicketCookieGenerator.addCookie(request, response, ticketGrantingTicket.getId());
        }  catch (Exception e){
            logger.error("bindTicketGrantingTicket has exception.", e);
        }
    }

    /**
     * Get the signIn view URL.获取service参数并跳转页面
     * @param request the HttpServletRequest object.
     * @return redirect URL
     */
    private String getSignInView(HttpServletRequest request) {
        String service = ServletRequestUtils.getStringParameter(request, "client", "");
        String returnUrl=ServletRequestUtils.getStringParameter(request, "returnUrl", "");
        String redirect="redirect:login";
        if(StringUtils.isNotEmpty(service)&& StringUtils.isNotEmpty(returnUrl)){
            redirect= redirect+"?"+(StringUtils.isNotEmpty(service)?"client="+service:"")+"&"+(StringUtils.isNotEmpty(returnUrl)?"returnUrl="+returnUrl:"");
        }
         else if(StringUtils.isNotEmpty(service)){
            redirect= redirect+"?"+(StringUtils.isNotEmpty(service)?"client="+service:"");
        }
        else if(StringUtils.isNotEmpty(returnUrl)){
            redirect= redirect+"?"+(StringUtils.isNotEmpty(returnUrl)?"returnUrl="+returnUrl:"");
        }
        return redirect;
    }


}
