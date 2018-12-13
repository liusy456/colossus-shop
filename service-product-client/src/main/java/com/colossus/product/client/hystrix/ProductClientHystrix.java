package com.colossus.product.client.hystrix;

import com.colossus.product.client.ProductClient;
import org.springframework.stereotype.Component;

/**
 * 商品服务 熔断处理
 *
 */
@Component
public class ProductClientHystrix implements ProductClient {

}
