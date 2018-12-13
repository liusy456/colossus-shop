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
    public String mobileNotify(String mobile) {
        return null;
    }
}
