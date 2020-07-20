package com.hzit.pay.web.req;

import lombok.Data;
import lombok.ToString;

/**
 * 支付请求参数
 */
@Data
@ToString
public class PayWebReq {

    private String mchId;

    private String mchOrderNo;

    private String channelId;

    private String currency;

    private String amount;

    private String notifyUrl;

    private String subject;

    private String body;

    private String sign; //签名串

    private String token; //签名串

    private String clientIp;
    private String device;

    private String param1;
    private String param2;




}
