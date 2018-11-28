package com.colossus.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication(scanBasePackages ={"com.colossus"})
@EnableDiscoveryClient
@EnableHystrix
public class ServiceRedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRedisApplication.class,args);
    }
}
