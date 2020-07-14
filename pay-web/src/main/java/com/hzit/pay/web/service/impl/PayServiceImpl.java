package com.hzit.pay.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.req.AliapyPayReq;
import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.common.utils.PayDigestUtil;
import com.hzit.common.utils.XXPayUtil;
import com.hzit.pay.web.client.IApilayClient;
import com.hzit.pay.web.controller.PayController;
import com.hzit.pay.web.factory.PayStrategyFactory;
import com.hzit.pay.web.mapper.MchInfoMapper;
import com.hzit.pay.web.mapper.MchPayChannelMapper;
import com.hzit.pay.web.mapper.PaySerialNoMapper;
import com.hzit.pay.web.model.MchInfo;
import com.hzit.pay.web.model.MchPayChannel;
import com.hzit.pay.web.model.PaySerialNo;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayService;
import com.hzit.pay.web.service.IPayStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付相关接口实现类
 */
@Service
public class PayServiceImpl implements IPayService {

    private Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private MchInfoMapper mchInfoMapper;


    @Autowired
    private MchPayChannelMapper mchPayChannelMapper;

    @Autowired
    private PaySerialNoMapper paySerialNoMapper;

    @Autowired
    private PayStrategyFactory payStrategyFactory;






    @Override
    public  Result<PayResultData> Pay(PayReq payReq) throws Exception {

        Result<PayResultData> payResultDataResult = new Result<PayResultData>();

        //2. 签名验证
        ///对象转json
        JSONObject params = (JSONObject)JSONObject.toJSON(payReq);

        MchInfo mchInfo = mchInfoMapper.selectByPrimaryKey(payReq.getMchId());
        //2.商户状态
        if(ObjectUtils.isEmpty(mchInfo) || "1".equals(mchInfo.getStatus())){
            logger.info("商户号：{}不存在或者状态异常",payReq.getMchId());   //TODO
            payResultDataResult.setMsg("商户号不存在或者状态异常");
            payResultDataResult.setCode(-1);
            return payResultDataResult;
        }

        //3.支付方式是否支持
        MchPayChannel mchPayChannel =  mchPayChannelMapper.selectByPrimaryKey(1);

        if(ObjectUtils.isEmpty(mchPayChannel) ){
            logger.info("商户号：{}不支持此支付方式:{}",payReq.getMchId(),payReq.getChannelId());   //TODO
            payResultDataResult.setMsg("商户号不支持此支付方式");
            payResultDataResult.setCode(-1);
            return payResultDataResult;
        }
        //4.参数验签，判断请求数据是否被修改
        String reqkey = mchInfo.getReqKey();
        boolean flag = XXPayUtil.verifyPaySign(params,reqkey);

        if(flag == false){
            logger.info("商户号：{}验签失败:{}",payReq.getMchId(),payReq.getChannelId());  //TODO
//            result.setMsg("验签失败");
//            result.setCode(-1);
//            return result;
//            return;
        }

        //5.生成支付流水

        PaySerialNo paySerialNo = new PaySerialNo();
        paySerialNo.setAmount(new BigDecimal(payReq.getAmount())); //注意单位
        paySerialNo.setGoodsBody(payReq.getBody());
        paySerialNo.setGoodsSubject(payReq.getSubject());
        paySerialNo.setMchId(payReq.getMchId());
        paySerialNo.setMchOrderNo(payReq.getMchOrderNo());
        paySerialNo.setNotifyUrl(payReq.getNotifyUrl()); //异步通知地址

        paySerialNo.setPayChannel(payReq.getChannelId());

        //1.jdk uuid
        //2.没数据自增id
        //3.redisd的 increment方法
        //4.snowflake 雪花算法
        String reqSerialNo = String.valueOf(System.currentTimeMillis());

        paySerialNo.setReqSerialNo(reqSerialNo); //TODO 请求流水号 支付系统生成, 全局唯一。微服务，
        paySerialNo.setStatus("1"); // 1-初始中
        paySerialNo.setCreateTime(new Date());
        paySerialNo.setCreateBy("system");
        paySerialNoMapper.insert(paySerialNo);

        // 策略+ 工厂+ 枚举
        IPayStrategyService iPayStrategyService = PayStrategyFactory.getPayStrate(payReq.getChannelId());
        Result result = iPayStrategyService.payStrategy(payReq,reqSerialNo);
        return result;
    }
}
