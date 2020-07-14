package com.hzit.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
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
    @RequestMapping("/toPay")
    public Result<PayResultData> pay(@RequestBody  @Valid PayReq payReq){
        logger.info("接收到请求参数为：{}的支付请求",payReq);

        Result<PayResultData> payResultDataResult = new Result<PayResultData>();
        try {
            payResultDataResult = iPayService.Pay(payReq);
            return payResultDataResult;
        } catch (Exception e) {
            logger.error("Exception",e);
            payResultDataResult.setMsg(e.getMessage());
            payResultDataResult.setCode(-1);
            return payResultDataResult;
        }
    }

}
