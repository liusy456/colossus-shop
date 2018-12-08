package com.colossus.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class APILogHelper {
	private static final Logger logger = LoggerFactory.getLogger(APILogHelper.class);
	
	public static String saveLog(HttpServletRequest request,String errorMsg){
		return saveLog(request,null,errorMsg);
	}

	public static String saveLog(HttpServletRequest request,Throwable t){
		return saveLog(request,t,null);
	}
	
	public static String saveLog(HttpServletRequest request,Throwable t,String errorMsg){
		String logId = AppUtil.getUUID();
		Enumeration<String> headerNames = request.getHeaderNames();
		Enumeration<String> parameterNames = request.getParameterNames();
		Cookie[] cookies = request.getCookies();
		StringBuilder logContent = new StringBuilder();
		logContent.append("API log:").append(logId).append("\n");
		if(StringUtils.isNotEmpty(errorMsg)){
			logContent.append(errorMsg).append("\n");
		}
		logContent.append(request.getRequestURI()).append("\n");
		logContent.append("headers:\n");
		while (headerNames.hasMoreElements()) {
			String name =  headerNames.nextElement();
			String value = request.getHeader(name);
			logContent.append(name).append(":").append(value).append("\n");
		}
		logContent.append("params:\n");
		while (parameterNames.hasMoreElements()) {
			String name =  parameterNames.nextElement();
			String value = request.getParameter(name);
			logContent.append(name).append(":").append(value).append("\n");
		}
		if(cookies!=null){
			logContent.append("cookies:\n");
			for (Cookie cookie : cookies) {
				logContent.append(cookie.getName()).append(":").append(cookie.getValue());
			}
		}
		logger.error(logContent.toString());
		
		if(t!=null){
			logger.error("API异常:"+logId,t);
		}
		return logId;
	}
}
