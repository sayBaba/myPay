package com.hzit.common.req;

import lombok.Data;
import lombok.ToString;

/**
 * 支付宝支付请求参数
 */
@Data
@ToString
public class AliapyPayReq {

    private String outTradeNo;
    private String amount;
    private String subject;
    private String body;

}
