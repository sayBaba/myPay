package com.hzit.common.resp;

import lombok.Data;
import lombok.ToString;

/**
 * 统一支付接口支付返回详细数据
 */
@Data
@ToString
public class PayResultData{

    private String channelId; //支付方式  扫码支付

    private String url; //二维码地址,或者其他


    private String mchOrderNo; //商户订单号

    private String reqSerialNo; //支付系统请求流水

    private String sign; //签名串




}
