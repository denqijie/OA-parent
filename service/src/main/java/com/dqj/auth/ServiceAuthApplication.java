package com.dqj.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dqj.auth.mapper")
public class ServiceAuthApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceAuthApplication.class);
    }
}
