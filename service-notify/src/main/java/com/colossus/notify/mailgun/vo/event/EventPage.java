package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class EventPage{

	@JSONField(name="paging")
	private Paging paging;

	@JSONField(name="items")
	private List<ItemsItem> items;
}