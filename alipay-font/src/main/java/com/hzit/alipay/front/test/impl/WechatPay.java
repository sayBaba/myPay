package com.hzit.alipay.front.test.impl;

import com.hzit.alipay.front.test.IPay;
import com.hzit.alipay.front.test.factory.PayStrategyFactory;
import com.hzit.common.enums.PayEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class WechatPay implements IPay, InitializingBean {


    @Override
    public void pay() {
        System.out.println("--------这是微信支付----");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        PayStrategyFactory.map.put(PayEnum.WECHAT.getCode(),this);

    }
}
