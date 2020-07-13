package com.hzit.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/myPay")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private IPayService iPayService;


    /**
     * 统一支付接口
     */
    @RequestMapping("/toPay")    //1.参数校验
    public void pay(@RequestBody  @Valid PayReq payReq){
        logger.info("接收到请求参数为：{}的支付请求",payReq);


        try {
            iPayService.Pay(payReq);

            //8.封装javabean 返回给对应的业务系统

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
