package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Geolocation{

	@JSONField(name="country")
	private String country;

	@JSONField(name="city")
	private String city;

	@JSONField(name="region")
	private String region;
}