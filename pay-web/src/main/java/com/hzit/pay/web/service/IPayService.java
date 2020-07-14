package com.hzit.pay.web.service;

import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.pay.web.req.PayReq;

/**
 * 支付相关接口
 */
public interface IPayService {

    /**
     * 处理支付请求
     * @param payReq
     */
    public Result<PayResultData> Pay(PayReq payReq) throws Exception;
}
