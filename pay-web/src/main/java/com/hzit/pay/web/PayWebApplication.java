package com.hzit.pay.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hzit.pay.web.mapper")
@SpringBootApplication
public class PayWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayWebApplication.class, args);
    }

}
