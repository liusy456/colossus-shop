package com.colossus.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.colossus","org.apereo"})
public class CasApplication {
    public static void main(String[] args) {

        SpringApplication.run(CasApplication.class,args);
    }
}
