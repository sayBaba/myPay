package com.hzit.pay.web;


import com.alibaba.fastjson.JSONObject;
import com.hzit.common.utils.PayDigestUtil;
import com.hzit.common.utils.XXPayUtil;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.req.PayWebReq;


import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class PayWebApplicationTests {

    @Autowired
    private RestTemplate restTemplate;


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

    @Test
    void contextLoads() {

        //Skill 的代码

        String reqKey = "test012345"; //TOOD 读取配置文件，读config中心
        String mchId = "10000"; //TOOD 读取配置文件，读config中心
        String notifyUrl = "http:127.0.0.1:8808/notify/pay"; //支付回调;
        PayWebReq payWebReq = new PayWebReq();
        payWebReq.setMchId(mchId);
        payWebReq.setChannelId("Alipay"); //支付方式
        payWebReq.setCurrency("ccy"); //TODO
        payWebReq.setNotifyUrl(notifyUrl);
        payWebReq.setMchOrderNo("100");
        payWebReq.setAmount("1000");
        payWebReq.setBody("测试");
        payWebReq.setSubject("测试");
        JSONObject params = (JSONObject)JSONObject.toJSON(payWebReq);
        System.out.println("params =" + params);

        String sign = PayDigestUtil.getSign(params,reqKey);
        System.out.println("sign = " + sign);
        payWebReq.setSign(sign);


        //pay-web的代码

        PayReq payReq = new PayReq();

        BeanUtils.copyProperties(payWebReq,payReq);

        JSONObject data = (JSONObject)JSONObject.toJSON(payReq);

        System.out.println("data =" + data);


        boolean flag = XXPayUtil.verifyPaySign(data,reqKey);

        System.out.println("flag = " + flag);






    }


}
