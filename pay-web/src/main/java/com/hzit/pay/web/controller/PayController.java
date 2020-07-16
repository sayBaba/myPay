package com.hzit.pay.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.req.PayCallBackData;
import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.pay.web.model.PaySerialNo;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/myPay")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private IPayService iPayService;

    /**
     * 统一支付接口, 订单
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

    /**
     *  查询交易流水
     * @param reqSerialNo
     * @return
     */
    @RequestMapping("/queryTrade")
    public Result<PaySerialNo> queryTradeInfo(@RequestParam String reqSerialNo){
        logger.info("接收到 查询交易流水请求，reqSerialNo：{}",reqSerialNo);

        return iPayService.queryTradeInfoByReqSrerialNo(reqSerialNo);
    }

    /**
     * 更新交易流水
     * @return
     */
    @RequestMapping("/updateTrade")
    public Result<PaySerialNo> updateTradeInfo(@RequestBody PayCallBackData payCallBackData){
        logger.info("接收到 更新交易流水请求，参数：{}",payCallBackData);
        Result result = iPayService.updateTradeInfo(payCallBackData);
        return result;
    }

}
