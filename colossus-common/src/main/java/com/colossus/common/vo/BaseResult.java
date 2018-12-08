package com.colossus.common.vo;

/**
 * Ajax返回对象扩展
 *
 * @author ganbo
 * @date 2017年3月2日 上午10:03:47
 */
public class BaseResult{

	private boolean success = false;
	private Object content;
	private String message;

	public BaseResult() {
		super();
	}

	public BaseResult(boolean success, Object content) {
		super();
		this.success = success;
		this.content = content;
	}

	public BaseResult(boolean success, Object content, String message) {
		super();
		this.success = success;
		this.content = content;
		this.message = message;
	}

	public BaseResult(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
