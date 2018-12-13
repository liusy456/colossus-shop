package com.colossus.notify.service.impl;


import com.colossus.notify.service.NotifyService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户通知服务实现
 */

@Api(value = "API - NotifyUserServiceImpl", description = "用户通知")
@RefreshScope
@RestController
public class NotifyUserServiceImpl implements NotifyService {
    private static final Logger logger = LoggerFactory.getLogger(NotifyUserServiceImpl.class);

}
