package com.colossus.notify.client.hystrix;

import com.colossus.notify.client.NotifyClient;
import org.springframework.stereotype.Component;

/**
 * 用户通知 熔断处理
 */

@Component
public class NotifyClientHystrix implements NotifyClient {

    @Override
    public void sendSMS(String phone, String content) {

    }

    @Override
    public void sendNotify(String clientId, String content) {

    }

    @Override
    public void sendEmail(String email, String content) {

    }

    @Override
    public void sendAuthCode(String phone, String code) {

    }
}
