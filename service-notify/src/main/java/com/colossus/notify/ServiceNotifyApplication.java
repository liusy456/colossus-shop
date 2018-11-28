package com.colossus.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableHystrix
//@EnableApolloConfig
@SpringBootApplication(scanBasePackages ={"com.colossus"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.colossus.notify.service","com.colossus.redis.service"})
@EnableEurekaClient
public class ServiceNotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceNotifyApplication.class, args);
	}

}
