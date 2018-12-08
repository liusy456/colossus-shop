package com.colossus.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableHystrix
//@EnableApolloConfig
@SpringBootApplication(scanBasePackages ={"com.colossus"} )
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.colossus.redis.service"})
@MapperScan(basePackages = "com.colossus.common.dao")
public class ServiceAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthApplication.class, args);
	}

}
