package com.colossus.portal;

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
@EnableFeignClients(basePackages = {"com.colossus.redis.service","com.colossus.portal.service"})
@EnableTransactionManagement
@MapperScan(basePackages = "com.colossus.common.dao")
public class ServicePortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePortalApplication.class, args);
	}

}
