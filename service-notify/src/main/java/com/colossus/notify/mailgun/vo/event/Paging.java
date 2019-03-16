package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Paging{

	@JSONField(name="next")
	private String next;

	@JSONField(name="previous")
	private String previous;

	@JSONField(name="last")
	private String last;

	@JSONField(name="first")
	private String first;

}