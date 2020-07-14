package com.hzit.alipay.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AlipayFontApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlipayFontApplication.class, args);
    }

}
