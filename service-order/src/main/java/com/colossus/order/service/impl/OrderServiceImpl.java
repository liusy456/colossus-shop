package com.colossus.order.service.impl;

import com.colossus.order.service.OrderService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * 订单Service
 */

//@Transactional
@Api(value = "API - OrderServiceImpl", description = "order 服务")
@Service
@RefreshScope
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
}
