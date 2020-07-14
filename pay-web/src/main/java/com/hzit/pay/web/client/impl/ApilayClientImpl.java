package com.hzit.pay.web.client.impl;

import com.hzit.common.req.AliapyPayReq;
import com.hzit.common.resp.Result;
import com.hzit.pay.web.client.IApilayClient;

public class ApilayClientImpl implements IApilayClient {

    /**
     * fegin容错
     * @param aliapyPayReq
     * @return
     */
    @Override
    public Result<String> toAlipay(AliapyPayReq aliapyPayReq) {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("调用alipay-front服务异常");
        return result;
    }
}
