package com.hzit.pay.web.mq.customer;

import com.alibaba.fastjson.JSONObject;
import com.hzit.pay.web.controller.PayController;
import com.hzit.pay.web.mapper.NoticeInfoMapper;
import com.hzit.pay.web.model.NoticeInfo;
import com.hzit.pay.web.mq.config.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

/**
 * 支付结果通知消费者
 */
@Component
public class PayNotifyCustomer {

    private Logger logger = LoggerFactory.getLogger(PayNotifyCustomer.class);


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private NoticeInfoMapper noticeInfoMapper;


    @RabbitListener(queues = RabbitMqConfig.DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(JSONObject object, Channel channel,Message message) throws IOException {
        logger.info("通知支付结果的消费者{}获取的参数",object);   //TODO

        String url = "http://127.0.0.1:8089/notify/pay"; //读配置中心


        String rlt =restTemplate.postForObject(url,object,String.class);


        String serialNo = object.getString("reqSerialNo");

        NoticeInfo noticeInfo = noticeInfoMapper.queryByReqSerial(serialNo);

        //成功就更新
        if("success".equals(rlt)){
            //更新 notice_info 表
            noticeInfo.setUpdateTime(new Date());
            noticeInfo.setNoticeCount(noticeInfo.getNoticeCount()+1);
            noticeInfo.setStatus("0");
            noticeInfoMapper.updateByPrimaryKey(noticeInfo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        //没成功就判断,到达5次
        if(noticeInfo.getNoticeCount() == 5){
            noticeInfo.setUpdateTime(new Date());
            noticeInfo.setStatus("2");
            noticeInfoMapper.updateByPrimaryKey(noticeInfo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        //继续通知 又放到mq中

//        String msg = new String(message.getBody());
//        System.out.println("当前时间：{"+new Date().toString()+ "},死信队列A收到消息：{"+ msg+"}");
//
    }

}
