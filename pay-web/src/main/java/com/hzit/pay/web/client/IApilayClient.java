package com.hzit.pay.web.client;

import com.hzit.common.req.AliapyPayReq;
import com.hzit.common.resp.Result;
import com.hzit.pay.web.client.impl.ApilayClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * alipay-font的服务
 */
@FeignClient(value = "alipay-front",fallback = ApilayClientImpl.class)
public interface IApilayClient {

    /**
     * 支付宝支付
     * @param aliapyPayReq
     * @return
     */
    @RequestMapping("/alipay/pay")
    public Result<String> toAlipay(@RequestBody AliapyPayReq aliapyPayReq);


}
