package com.hzit.pay.web.service;

import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.pay.web.req.PayReq;

/**
 * 支付策略接口
 */
public interface IPayStrategyService {

    /**
     * 支付接口
     * @return
     */
    public Result<?> payStrategy(PayReq payReq,String reqSerialNo);
}
