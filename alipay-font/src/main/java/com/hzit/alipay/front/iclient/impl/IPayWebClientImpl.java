package com.hzit.alipay.front.iclient.impl;

import com.hzit.alipay.front.iclient.IPayWebClient;
import com.hzit.common.req.PayCallBackData;
import com.hzit.common.req.PaySerialData;
import com.hzit.common.resp.Result;
import org.springframework.stereotype.Component;

@Component
public class IPayWebClientImpl implements IPayWebClient {

    @Override
    public Result<PaySerialData> queryTradeInfo(String reqSerialNo) {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("查询交易信息失败");

        return result;
    }

    @Override
    public Result<PaySerialData> updateTradeInfo(PayCallBackData payCallBackData) {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg("更新交易流水异常");

        return result;
    }
}
