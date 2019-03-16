package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Headers{

	@JSONField(name="message-id")
	private String messageId;
}