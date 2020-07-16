package com.hzit.alipay.front.iclient;

import com.hzit.alipay.front.iclient.impl.IPayWebClientImpl;
import com.hzit.common.req.PayCallBackData;
import com.hzit.common.req.PaySerialData;
import com.hzit.common.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 支付服务的接口
 */
@FeignClient(value = "pay-web",fallback = IPayWebClientImpl.class)
public interface IPayWebClient {


    @RequestMapping("/myPay/queryTrade")
    public Result<PaySerialData> queryTradeInfo(@RequestParam String reqSerialNo);



    /**
     * 更新交易流水
     * @return
     */
    @RequestMapping("/myPay/updateTrade")
    public Result<PaySerialData> updateTradeInfo(@RequestBody PayCallBackData payCallBackData);




}
