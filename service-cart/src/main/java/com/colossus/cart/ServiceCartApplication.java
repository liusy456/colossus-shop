package com.colossus.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableHystrix
@SpringBootApplication(scanBasePackages ={"com.colossus"})
@EnableDiscoveryClient
//@EnableApolloConfig
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.colossus.redis.service"})
@MapperScan(basePackages = "com.colossus.common.dao")
public class ServiceCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCartApplication.class, args);
	}

}
