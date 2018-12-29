package com.colossus.notify.web.api;

import com.colossus.notify.client.NotifyClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tlsy1
 * @since 2018-12-13 15:59
 **/
@RestController
public class NotifyFeign implements NotifyClient {
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
