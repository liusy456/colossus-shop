package com.colossus.product.client;

import com.colossus.product.client.hystrix.ProductClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品 Service
 */

@FeignClient(value = "service-product",fallback = ProductClientHystrix.class)
public interface ProductClient {

}
