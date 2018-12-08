package com.colossus.common.exception;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
* 系统自定义异常，处理业务异常后统一抛出
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

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * 在request中获取异常类
	 * @param request
	 * @return
	 */
	public static Throwable getThrowable(HttpServletRequest request){
		Throwable ex = null;
		if (request.getAttribute("exception") != null) {
			ex = (Throwable) request.getAttribute("exception");
		} else if (request.getAttribute("javax.servlet.error.exception") != null) {
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}
	
}
