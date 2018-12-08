package com.colossus.auth.filter;


import com.colossus.auth.utils.JwtAuthenticator;
import com.colossus.auth.utils.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tlsy1
 * @since 2018-11-08 17:25
 **/
public class ServiceAuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAuthFilter.class);

    private JwtGenerator jwtGenerator;
    private JwtAuthenticator jwtAuthenticator;

    public ServiceAuthFilter(JwtGenerator jwtGenerator,JwtAuthenticator jwtAuthenticator) {
        this.jwtGenerator = jwtGenerator;
        this.jwtAuthenticator = jwtAuthenticator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if( httpServletRequest.getRequestURI().startsWith("/actuator/")){
            logger.info("健康检查放行remoteAddr:{}, url:{}", httpServletRequest.getRemoteAddr(),
            httpServletRequest.getRequestURL());
            chain.doFilter(request, response);
            return;
        }
        String jwt = httpServletRequest.getHeader(jwtGenerator.getHeader());
        try {
            jwtAuthenticator.validate(jwt);
            chain.doFilter(request, response);
        }catch (Exception e){
            logger.error("checkToken false remoteAddr:{}, url:{}", httpServletRequest.getRemoteAddr(),
                    httpServletRequest.getRequestURL());
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter pw = resp.getWriter();
            pw.write("{\n" +
                    "    \"data\": null,\n" +
                    "    \"code\": 401,\n" +
                    "    \"pagination\": null,\n" +
                    "    \"message\": \"非法请求\",\n" +
                    "    \"successful\": false\n" +
                    "}");
            pw.flush();
            pw.close();
        }

    }

    @Override
    public void destroy() {

    }
}
