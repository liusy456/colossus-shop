package com.colossus.order.client.hystrix;

import com.colossus.order.client.OrderClient;
import org.springframework.stereotype.Component;

/**
 * 订单服务 熔断处理
 *
 */

@Component
public class OrderClientHystrix implements OrderClient {

}
