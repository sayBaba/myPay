package com.hzit.pay.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.utils.XXPayUtil;
import com.hzit.pay.web.controller.PayController;
import com.hzit.pay.web.mapper.MchInfoMapper;
import com.hzit.pay.web.mapper.MchPayChannelMapper;
import com.hzit.pay.web.mapper.PaySerialNoMapper;
import com.hzit.pay.web.model.MchInfo;
import com.hzit.pay.web.model.MchPayChannel;
import com.hzit.pay.web.model.PaySerialNo;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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


    @Override
    public void Pay(PayReq payReq) throws Exception {

        //2. 签名验证
        ///对象转json
        JSONObject params = (JSONObject)JSONObject.toJSON(payReq);

        MchInfo mchInfo = mchInfoMapper.selectByPrimaryKey(payReq.getMchId());
        //2.商户状态
        if(ObjectUtils.isEmpty(mchInfo) || "1".equals(mchInfo.getStatus())){
            logger.info("商户号：{}不存在或者状态异常",payReq.getMchId());   //TODO
            return;
        }

        //3.支付方式是否支持
        MchPayChannel mchPayChannel =  mchPayChannelMapper.selectByPrimaryKey(0);

        if(ObjectUtils.isEmpty(mchPayChannel) ){
            logger.info("商户号：{}不支持此支付方式:{}",payReq.getMchId(),payReq.getChannelId());   //TODO
            return;
        }
        //4.参数验签
        String reqkey = mchInfo.getReqKey();
        boolean flag = XXPayUtil.verifyPaySign(params,reqkey);

        if(flag == false){
            logger.info("商户号：{}验签失败:{}",payReq.getMchId(),payReq.getChannelId());  //TODO
            return;
        }

        //5.生成支付流水

        PaySerialNo paySerialNo = new PaySerialNo();

        paySerialNoMapper.insert(paySerialNo);

//        if("Alipay_wap".equals(payReq.getChannelId())){
//            //调用支付宝wap支付接口
//        }else if(){
//
//        }else if(){
//
//        }else if(){
//
//        }else{
//
//        }


        //6.根据channelId 调用对应的支付接口，或者银行接口  工厂模式+策略模式 取代if.else、

        //7.解析，支付宝/银行返回的参数。



    }
}
