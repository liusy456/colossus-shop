package com.colossus.common.base;


import com.colossus.common.exception.ServiceException;
import com.colossus.common.utils.APILogHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 针对API的统一异常处理拦截器<br>
 * 
 * 将所有API异常封装成JSON对象返回客户端,并通过HTTP状态码表示服务出现异常
 * 
 * @author huzq
 * @date 2016-8-19
 */
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);


	/**
	 * 处理服务异常
	 *
	 * @param ex the ex
	 * @return the api message
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ApiError handleServiceException(ServiceException ex, HttpServletRequest request){
		String errorId = APILogHelper.saveLog(request,ex);
		List<String> messages = ex.getMessages();
		ApiError error = new ApiError();
		error.setErrorId(errorId);
		error.setMessages(messages);
		return error;
	}
	
	/**
	 * 处理其他运行时异常
	 *
	 * @param t the t
	 * @return the api message
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	@ResponseBody
	public ApiError handleOtherException(Throwable t, HttpServletRequest request){
		String errorId = APILogHelper.saveLog(request,t);
		ApiError error = new ApiError();
		error.setErrorId(errorId);
		error.setMessages(new ArrayList<>());
		if ((t instanceof TransactionSystemException)
				&& (t.getCause() != null && t.getCause() instanceof RollbackException)
				&& (t.getCause().getCause() != null && t.getCause().getCause() instanceof ConstraintViolationException)
				&& (CollectionUtils.isNotEmpty(((ConstraintViolationException) t.getCause().getCause()).getConstraintViolations()))) {
			Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) t.getCause().getCause()).getConstraintViolations();
			for (ConstraintViolation<?> violation:violations){
				error.getMessages().add(violation.getMessage());
			}
		}else if(t instanceof ConstraintViolationException) {
			Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) t).getConstraintViolations();
			for (ConstraintViolation<?> violation:violations){
				error.getMessages().add(violation.getMessage());
			}
		}else {
			error.getMessages().add(t.getMessage());
		}
		return error;
	}
}