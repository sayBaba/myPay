package com.hzit.alipay.front.test.factory;

import com.hzit.alipay.front.test.IPay;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付策略工厂
 */
@Component
public class PayStrategyFactory {

    /**
     * 容器启动，把支付的实现类放入到map
     */
    public static Map<String, IPay> map = new ConcurrentHashMap<String, IPay> ();



    /**
     * 根据code 取支付策略
     * @param code
     * @return
     */
    public IPay getPayStrateg(String code){

        return map.get(code);

    }



}
