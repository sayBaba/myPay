package com.hzit.pay.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hzit.common.req.PayCallBackData;
import com.hzit.common.resp.PayResultData;
import com.hzit.common.resp.Result;
import com.hzit.common.utils.XXPayUtil;
import com.hzit.pay.web.factory.PayStrategyFactory;
import com.hzit.pay.web.mapper.MchInfoMapper;
import com.hzit.pay.web.mapper.MchPayChannelMapper;
import com.hzit.pay.web.mapper.NoticeInfoMapper;
import com.hzit.pay.web.mapper.PaySerialNoMapper;
import com.hzit.pay.web.model.MchInfo;
import com.hzit.pay.web.model.MchPayChannel;
import com.hzit.pay.web.model.NoticeInfo;
import com.hzit.pay.web.model.PaySerialNo;
import com.hzit.pay.web.mq.config.RabbitMqConfig;
import com.hzit.pay.web.req.PayReq;
import com.hzit.pay.web.service.IPayService;
import com.hzit.pay.web.service.IPayStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    @Transactional //单机应用
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
        if(ObjectUtils.isEmpty(iPayStrategyService)){
            logger.info("商户号：{}不支持此支付渠道:{}",payReq.getMchId(),payReq.getChannelId());   //TODO
            payResultDataResult.setMsg("商户号不支持此支付渠道");
            payResultDataResult.setCode(-1);
            return payResultDataResult;
        }

        Result result = iPayStrategyService.payStrategy(payReq,reqSerialNo);
        return result;
    }

    @Override
    public Result<PaySerialNo> queryTradeInfoByReqSrerialNo(String reqSrerialNo) {
        Result<PaySerialNo> result = new Result<PaySerialNo>();
        PaySerialNo  paySerialNo = paySerialNoMapper.queryBySerialNo(reqSrerialNo);
        result.setCode(0);
        result.setMsg("查询成功");
        result.setData(paySerialNo);
        return result;
    }

    @Override
    @Transactional
    public Result updateTradeInfo(PayCallBackData payCallBackData) {

        Result result = new Result();

        //
        PaySerialNo  paySerialNo = paySerialNoMapper.queryBySerialNo(payCallBackData.getReqSrialNo());
        if(ObjectUtils.isEmpty(paySerialNo)){
            return null; //TODO
        }

        //todo 更新交易流水
        PaySerialNo newPaySerialNo = new PaySerialNo();
        newPaySerialNo.setId(paySerialNo.getId());
        newPaySerialNo.setRespMsg(payCallBackData.getRespMsg());
        newPaySerialNo.setRespSerialNo(payCallBackData.getRespSerialNo());
        newPaySerialNo.setUpdateTime(payCallBackData.getPaySuccessTime()); //交易成功的时间
        newPaySerialNo.setStatus(payCallBackData.getPayStatus());
        paySerialNoMapper.updateByPrimaryKeySelective(newPaySerialNo);

        //TODO 异步通知 业务系统（订单）

        NoticeInfo noticeInfo = noticeInfoMapper.queryByReqSerial(paySerialNo.getReqSerialNo());

        if(ObjectUtils.isEmpty(noticeInfo)){
            NoticeInfo record = new NoticeInfo();
            record.setNoticeCount(0);
            record.setCreateBy("system");
            record.setCreateTime(new Date());
            record.setReqSerialNo(paySerialNo.getReqSerialNo());
            record.setNoticeUrl(paySerialNo.getNotifyUrl());
            noticeInfoMapper.insert(noticeInfo);
        }else{
            noticeInfo.setUpdateTime(new Date());
            noticeInfoMapper.updateByPrimaryKey(noticeInfo);
        }

        //通知抢购服务
        String url = "http://127.0.0.1:8089/notify/pay"; //读配置中心

        //封装返回给业务系统的参数
        JSONObject object = new JSONObject();
        object.put("code",0);
        object.put("msg","成功");
        JSONObject data = new JSONObject();

        data.put("payStatus","0"); // 0 -成功，1-交易失败
        data.put("reqSerialNo",paySerialNo.getReqSerialNo());
        data.put("amout",paySerialNo.getAmount());
        data.put("mchOrderNo",paySerialNo.getMchId());
        object.put("data",data);


        String rlt =  restTemplate.getForObject(url+"?params="+object.toString(),String.class);
        logger.info("收到流水号：{}业务系统的返回结果:{}",paySerialNo.getReqSerialNo(),rlt);   //TODO

        //网络原因等，其他因素，导致请求没有发送成功
        if(!"success".equals(rlt)){
            //假设等5分钟， 把消息丢到mq.
            //TODO 通知的机制， 5m ，5m 15m 1h 2h
            rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE_NAME,RabbitMqConfig.DEAD_LETTER_QUEUEA_NAME,object);
        }
        //更新 notice_info 状态 修改为知成功
        result.setMsg("成功");
        result.setCode(0);
        return result;
    }


}
