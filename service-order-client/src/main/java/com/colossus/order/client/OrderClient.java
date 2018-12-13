package com.colossus.order.client;


import com.colossus.order.client.hystrix.OrderClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "service-order",fallback = OrderClientHystrix.class)
public interface OrderClient {

}
