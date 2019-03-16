package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ClientInfo{

	@JSONField(name="client-os")
	private String clientOs;

	@JSONField(name="device-type")
	private String deviceType;

	@JSONField(name="client-type")
	private String clientType;

	@JSONField(name="client-name")
	private String clientName;

	@JSONField(name="user-agent")
	private String userAgent;
}