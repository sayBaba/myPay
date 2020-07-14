//package com.hzit.pay.web;
//
//
//import com.hzit.pay.web.test.IPay;
//import com.hzit.pay.web.test.factory.PayStrategyFactory;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class PayWebApplicationTests {
//
//
//    @Autowired
//    private PayStrategyFactory factory;
//
//
//    @Test
//    void contextLoads() {
//
//        String channel = "alipay.trade.precreate";
//        IPay iPay = factory.getPayStrateg(channel);
//        iPay.pay();
//
//    }
//
//}
