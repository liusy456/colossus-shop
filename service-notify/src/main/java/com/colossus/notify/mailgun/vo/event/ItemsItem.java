package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ItemsItem{

	@JSONField(name="ip")
	private String ip;

	@JSONField(name="message")
	private Message message;

	@JSONField(name="tags")
	private List<String> tags;

	@JSONField(name="storage")
	private Storage storage;


	@JSONField(name="client-info")
	private ClientInfo clientInfo;

	@JSONField(name="campaigns")
	private List<Object> campaigns;

	@JSONField(name="log-level")
	private String logLevel;

	@JSONField(name="recipient")
	private String recipient;

	@JSONField(name="id")
	private String id;

	@JSONField(name="recipient-domain")
	private String recipientDomain;

	@JSONField(name="event")
	private String event;

	@JSONField(name="geolocation")
	private Geolocation geolocation;

	@JSONField(name="timestamp")
	private double timestamp;
}