package com.hzit.alipay.front.test.impl;

import com.hzit.alipay.front.test.IPay;
import com.hzit.alipay.front.test.factory.PayStrategyFactory;
import com.hzit.common.enums.PayEnum;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class Alipay implements IPay, InitializingBean {


    @Override
    public void pay() {
        System.out.println("----支付宝支付----------");

    }

    /**
     * 项目启动的时候，会执行
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         *
         * this 当前对象
         */

        System.out.println("==========="+PayEnum.ALIPAY_TRADE_PAY.getCode());
        PayStrategyFactory.map.put(PayEnum.ALIPAY_TRADE_PAY.getCode(),this);

    }
}
