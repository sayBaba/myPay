package com.hzit.alipay.front.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hzit.alipay.front.iclient.IPayWebClient;
import com.hzit.common.req.PayCallBackData;
import com.hzit.common.req.PaySerialData;
import com.hzit.common.resp.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 接受支付宝的回调
 */
@RestController
@RequestMapping("/alipayNotify")
public class AlipayNotifyController  {

    private Logger logger = LoggerFactory.getLogger(AlipayNotifyController.class);


    private  String alipayPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzJnbTT7sBQJTZvp3gGztei1V2eONrrbhxuPHojkAFTQzGE7nsWL2/TvVbOJihCq8JQtU9gSXBedNePetNLz4R7eMcZztTV9M9kxxwB5TKxjbI3l9DFDj3Q9sOUq8F1Afy8XiBfYdqvv+Haz4AWDdo6EljvXY6amrXbyBralIyXC/7exOqLs17/gx4DInfdf8ophOFbRSYbQCcRbDyxPdqT7mUY9ozfmoWaj/acjbH2gGGY26ptF9bDtkrPYLPgeIUNqYU1LsWgqqDxhL5eDYIGGvPMr5aFq9s29BjEAWdoDDAnUt8R0azhc1A6I1ONVspQToxPAMMVaYAirb1EXrQIDAQAB";

    @Autowired
    private IPayWebClient iPayWebClient;

    /**
     * 支付宝的支付回调
     * @return
     */
    @RequestMapping("/pay")
    public String receveNotify(HttpServletRequest request){
        logger.info("********接收到支付宝异步回调**********");

        //1.获取支付宝回调的参数- 把请求参数放到这个map中

        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//          valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        logger.info("接收支付宝回调的参数,params:{}" ,params);

        try {
           //2.验签，验证支付宝回调参数， 签名验证
            boolean flag = AlipaySignature.rsaCheckV1(params,alipayPubKey,"utf-8","RSA2");
            logger.info("flag = " + flag);
            if(!flag){
                return "fail";
            }

        } catch (AlipayApiException e) {
            logger.error("AlipayApiException",e);
            return "fail";
        }

        String reqSerialNo = params.get("out_trade_no");

        Result<PaySerialData>  paySerialDataResult = iPayWebClient.queryTradeInfo(reqSerialNo);

        if(paySerialDataResult.getCode() != 0){
            logger.info("调用支付服务的查询交易信息接口失败 msg：{}",paySerialDataResult.getMsg());
            return "fail";
        }

        PaySerialData paySerialData = paySerialDataResult.getData();

        //3.1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
        if(ObjectUtils.isEmpty(paySerialDataResult.getData())){
            logger.info("没有找到支付请求流水好为：{}的交易流水",reqSerialNo);
            return "fail";
        }

        //3.验证支付金额和流水表是否一致， 请求流水。
        String amt = params.get("total_amount");

        if (!amt.equals(paySerialData.getAmount())){
            logger.info("支付宝返回的金额和数据库不一致：{}的交易流水",reqSerialNo);
            return "fail";
        }

        //3.3.验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        //3.4.验证app_id是否为该商户本身

        //4.判断状态是否 TRADE_SUCCESS或TRADE_FINISHE
        String status = params.get("trade_status");

        String finishedTime = params.get("timestamp"); //String 转date

        String tradeNo = params.get("trade_no"); //
        String msg = params.get("msg"); //



        if("TRADE_SUCCESS".equals(status) || "TRADE_FINISHE".equals(status)){
            //更新交易流水, 往mq放一个消息：去通知商户的
            PayCallBackData payCallBackData = new PayCallBackData();
            payCallBackData.setBank("Alipay");
            payCallBackData.setPayStatus("0");
//            payCallBackData.setPaySuccessTime(finishedTime);

            payCallBackData.setReqSrialNo(reqSerialNo);

            payCallBackData.setRespSerialNo(tradeNo);
            payCallBackData.setRespMsg(msg);

            iPayWebClient.updateTradeInfo(payCallBackData);

            return "success";

        }else if("TRADE_CLOSED".equals(status)){
            //修改流水状态支付失败
            PayCallBackData payCallBackData = new PayCallBackData();

            payCallBackData.setBank("Alipay");
            payCallBackData.setPayStatus("1");
//          payCallBackData.setPaySuccessTime(finishedTime);

            payCallBackData.setReqSrialNo(reqSerialNo);

            payCallBackData.setRespSerialNo(tradeNo);

            iPayWebClient.updateTradeInfo(payCallBackData);

            return "success";
        } else{

            return "fail";
        }

        //6.返回支付宝一个succes，告诉支付宝你已经收到回调了，否则支付宝一直通知，24小时9次

    }


}
