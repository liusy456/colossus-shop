package com.colossus.notify.client.hystrix;

import com.colossus.notify.client.NotifyClient;
import org.springframework.stereotype.Component;

/**
 * 用户通知 熔断处理
 */

@Component
public class NotifyClientHystrix implements NotifyClient {

    @Override
    public String mobileNotify(String mobile) {
        return null;
    }
}
