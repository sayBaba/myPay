package com.hzit.common.enums;

/**
 * 支付方式枚举
 */
public enum PayEnum {


    ALIPAY_TRADE_PRECREATE("alipay.trade.precreate","alipay.trade.precreate"),

    ALIPAY_TRADE_PAY("alipay.trade.pay","alipay.trade.precreate"),

    WECHAT("wechat","wechat");

    /**
     * 枚举构造方法
     * @param code
     * @param value
     */
    PayEnum(String code,String value){
        this.code =code;
        this.value =value;

    }

    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 根据枚举的code 获取枚举的值
     * @param code
     * @return
     */
    public String getVlaue(String code){

        for(PayEnum p:  PayEnum.values()){

            if(code.equals(p.getCode())){
                return p.getValue();
            }

        }
        return null;
    }





}
