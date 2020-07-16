package com.hzit.pay.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.enums.PayEnum;
import com.hzit.common.req.AliapyPayReq;
import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.common.utils.PayDigestUtil;
import com.hzit.pay.web.client.IApilayClient;
import com.hzit.pay.web.factory.PayStrategyFactory;
import com.hzit.pay.web.mapper.MchInfoMapper;
import com.hzit.pay.web.model.MchInfo;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliapyPreCreatePay implements IPayStrategyService, InitializingBean {

    @Autowired
    private IApilayClient iApilayClient;

    @Autowired
    private MchInfoMapper mchInfoMapper;


    private Logger logger = LoggerFactory.getLogger(AliapyPreCreatePay.class);


    @Override
    public Result payStrategy(PayReq payReq,String reqSerialNo) {

        Result payResultDataResult = new Result<>();

//          //调用支付宝扫码支付的接口
            AliapyPayReq aliapyPayReq = new AliapyPayReq();
            aliapyPayReq.setAmount(payReq.getAmount());
            aliapyPayReq.setBody(payReq.getBody());
            aliapyPayReq.setOutTradeNo(reqSerialNo);
            aliapyPayReq.setSubject(payReq.getSubject());
            Result<String>  result = iApilayClient.toAlipay(aliapyPayReq);

            logger.info("调用支付宝扫码支付接口返回：{}",result);

            if(0 != result.getCode()){
                payResultDataResult.setCode(-1);
                payResultDataResult.setMsg(result.getMsg());
                return payResultDataResult;
            }

            payResultDataResult.setCode(0);
            payResultDataResult.setMsg("成功");

            PayResultData payResultData = new PayResultData();

            payResultData.setChannelId(payReq.getChannelId());

            payResultData.setMchOrderNo(payReq.getMchOrderNo());

            payResultData.setReqSerialNo(reqSerialNo);

            payResultData.setUrl(result.getData()); //支付宝返回的二维码地址

            String sign = ""; //计算出来

            JSONObject params1 = (JSONObject)JSONObject.toJSON(payResultData);

            //使用MD5 生成返回签名

            MchInfo  mchInfo = mchInfoMapper.selectByPrimaryKey(payReq.getMchId());
            String respSign = PayDigestUtil.getSign(params1, mchInfo.getRespKey());
            payResultData.setSign(respSign);
            payResultDataResult.setData(payResultData);

            logger.info("");
            return payResultDataResult;


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化的时候，把支付宝这个实例放入map中
        PayStrategyFactory.payMap.put(PayEnum.ALIPAY_TRADE_PRECREATE.getCode(),this);
    }
}
