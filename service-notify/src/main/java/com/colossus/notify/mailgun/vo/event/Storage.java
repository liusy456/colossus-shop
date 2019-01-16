package com.colossus.notify.mailgun.vo.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Tlsy1
 * @since 2018-10-30 15:51
 **/
@Data
public class Storage {

    @JSONField(name="url")
    private String url;
    @JSONField(name="key")
    private String key;
}
