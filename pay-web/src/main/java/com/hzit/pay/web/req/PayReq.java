package com.hzit.pay.web.req;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 支付请求参数
 */
@Data
@ToString
public class PayReq {

    @NotNull(message = "mchId不能为空")
    private String mchId;

    @NotNull(message = "mchOrderNo不能为空")
    private String mchOrderNo;

    @NotNull(message = "channelId不能为空")
    private String channelId;

    @NotNull(message = "currency不能为空")
    private String currency;

    @NotNull(message = "amount不能为空")
    private String amount;

    @NotNull(message = "notifyUrl不能为空")
    private String notifyUrl;

    @NotNull(message = "subject不能为空")
    private String subject;

    @NotNull(message = "body不能为空")
    private String body;

    @NotNull(message = "sign不能为空")
    private String sign; //签名串



    private String clientIp;
    private String device;

    private String param1;
    private String param2;




}
