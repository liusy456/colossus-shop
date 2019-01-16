package com.colossus.notify.mailgun.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class Event {

    public enum EventTypeEnum{
        accepted,
        rejected,
        delivered,
        failed,
        opened,
        clicked,
        unsubscribed,
        complained,
        stored
    }

    public enum FilterFiledEnum{
        event("event"),
        list("list"),
        attachment("attachment"),
        from("from"),
        messageId("message-id"),
        subject("subject"),
        to("to"),
        size("size"),
        recipient("recipient"),
        tags("tags"),
        severity("severity");

        private String fieldName;

        FilterFiledEnum(String fieldName){
            this.fieldName = fieldName;
        }

        public String getFieldName(){
            return this.fieldName;
        }
    }

    private EventTypeEnum event;

    private Date timestamp;

    private String id;

    private JSONObject object;
}
