package com.hzit.alipay.front;

import com.hzit.alipay.front.test.IPay;
import com.hzit.alipay.front.test.factory.PayStrategyFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlipayFontApplicationTests {


    @Autowired
    private PayStrategyFactory factory;


    @Test
    void contextLoads() {

        String channel = "wechat";
        IPay iPay = factory.getPayStrateg(channel);

        System.out.println("iPay =" + iPay);
        iPay.pay();

    }
}
