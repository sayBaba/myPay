package com.hzit.common.req;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 支付回调参数,拿这个对象去更新流水
 */
@Data
@ToString
public class PayCallBackData {

    private String bank; // 可以是只支付宝，微信，银行

    private String respSerialNo; //银行或支付公司返回的流水号

    private String respMsg; //返回描述

    private Date paySuccessTime; //支付成功的时间

    private String reqSrialNo; //支付服务流水号

    private String payStatus; //支付状态




}
