package com.colossus.sso.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
* 系统自定义异常，处理业务异常后统一抛出
* @author chendilin
* @date 2016年8月17日
*/
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private List<String> messages;
	
	public ServiceException(Throwable t){
		this(t.getMessage(), t);
		
	}
	
	public ServiceException(String message, Throwable t){
		super(message,t);
		this.messages = new ArrayList<>();
		this.messages.add(message);
	}
	
	public ServiceException(String message){
		super(message);
		this.messages = new ArrayList<>();
		this.messages.add(message);
	}
	
	public ServiceException(List<String> messages, Throwable t){
		super(messages==null?"": StringUtils.join(messages,","),t);
		this.messages = messages;
	}

	public ServiceException(List<String> messages){		
		super(messages==null?"": StringUtils.join(messages,","));
		this.messages = messages;
		
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
}
