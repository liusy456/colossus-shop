package com.colossus.sso.vo;

import java.util.List;

/**
 * API异常对象.
 *
 * @author huzq
 * @date 2016-9-2
 */
public class ApiError {

	/**  错误Id. */
	private String errorId;
	
	/**  错误的消息列表. */
	private List<String> messages;

	/**
	 * 获取 错误Id.
	 *
	 * @return 错误Id
	 */
	public String getErrorId() {
		return errorId;
	}

	/**
	 * 设置 错误Id.
	 *
	 * @param errorId 错误Id
	 */
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	/**
	 * 获取 错误的消息列表.
	 *
	 * @return 错误的消息列表
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * 设置 错误的消息列表.
	 *
	 * @param messages 错误的消息列表
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	
}
