package com.colossus.notify.mailgun.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 被买家拒绝返回的消息
 */
public class StoreMail {

	@JSONField(name="Sender")
	private String sender;

	@JSONField(name="References")
	private String references;

	@JSONField(name="Received")
	private String received;

	@JSONField(name="body-plain")
	private String bodyPlain;

	@JSONField(name="From")
	private String from;

	@JSONField(name="To")
	private String to;

	@JSONField(name="Subject")
	private String subject;

	@JSONField(name="Content-Type")
	private String contentType;

	@JSONField(name="body-html")
	private String bodyHtml;

	public void setSender(String sender){
		this.sender = sender;
	}

	public String getSender(){
		return sender;
	}

	public void setReferences(String references){
		this.references = references;
	}

	public String getReferences(){
		return references;
	}

	public void setReceived(String received){
		this.received = received;
	}

	public String getReceived(){
		return received;
	}

	public void setBodyPlain(String bodyPlain){
		this.bodyPlain = bodyPlain;
	}

	public String getBodyPlain(){
		return bodyPlain;
	}

	public void setFrom(String from){
		this.from = from;
	}

	public String getFrom(){
		return from;
	}

	public void setTo(String to){
		this.to = to;
	}

	public String getTo(){
		return to;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return subject;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setBodyHtml(String bodyHtml){
		this.bodyHtml = bodyHtml;
	}

	public String getBodyHtml(){
		return bodyHtml;
	}

	@Override
 	public String toString(){
		return 
			"StoreMail{" +
			"sender = '" + sender + '\'' + 
			",references = '" + references + '\'' + 
			",received = '" + received + '\'' + 
			",body-plain = '" + bodyPlain + '\'' + 
			",from = '" + from + '\'' + 
			",to = '" + to + '\'' + 
			",subject = '" + subject + '\'' + 
			",content-Type = '" + contentType + '\'' + 
			",body-html = '" + bodyHtml + '\'' + 
			"}";
		}
}