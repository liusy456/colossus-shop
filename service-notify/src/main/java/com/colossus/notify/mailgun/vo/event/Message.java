package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Message{

	@JSONField(name="headers")
	private Headers headers;
}