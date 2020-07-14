package com.hzit.pay.web.factory;

import com.hzit.pay.web.service.IPayService;
import com.hzit.pay.web.service.IPayStrategyService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付策略工厂
 */
@Component
public class PayStrategyFactory {

    public static Map<String, IPayStrategyService> payMap = new ConcurrentHashMap<String,IPayStrategyService>();

    /**
     * 根据code获取策略
     * @param code
     * @return
     */
    public static IPayStrategyService getPayStrate(String code){
        return payMap.get(code);
    }

}
