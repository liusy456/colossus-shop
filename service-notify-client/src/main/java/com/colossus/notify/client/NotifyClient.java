package com.colossus.notify.client;

import com.colossus.notify.client.hystrix.NotifyClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户通知服务接口
 */

@FeignClient(value = "service-notify",path = "/service-notify",fallback = NotifyClientHystrix.class)
public interface NotifyClient {

    /**
     * 发送短信
     * @param phone
     * @param content
     */
    @PostMapping("/service/notify/send-sms")
    void sendSMS(@RequestParam(value = "phone") String phone, @RequestParam(value = "content") String content);

    /**
     * 发送app通知
     * @param clientId
     * @param content
     */
    @PostMapping("/service/notify/send-notify")
    void sendNotify(@RequestParam(value = "clientId") String clientId,@RequestParam(value = "content") String content);

    /**
     * 发送邮件
     * @param email
     * @param content
     */
    @PostMapping("/service/notify/send-email")
    void sendEmail(@RequestParam(value = "email") String email,@RequestParam(value = "content") String content);

    /**
     * 发送验证码
     * @param phone
     * @param code
     */
    @PostMapping("/service/notify/send-auth-code")
    void sendAuthCode(@RequestParam(value = "phone") String phone,@RequestParam(value = "code") String code);

}
