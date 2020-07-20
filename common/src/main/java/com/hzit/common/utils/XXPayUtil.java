package com.hzit.common.utils;

import com.alibaba.fastjson.JSON;
import java.util.Map;
import java.util.Set;


public class XXPayUtil {


//    /**
//     *
//     * @param retMap 请求数据
//     * @param resKey 加密密钥
//     * @return
//     */
//    public static String makeRetData(Map retMap, String resKey) {
//        if(retMap.get(PayConstant.RETURN_PARAM_RETCODE).equals(PayConstant.RETURN_VALUE_SUCCESS)) {
//            String sign = PayDigestUtil.getSign(retMap, resKey, "payParams");
//            retMap.put(PayConstant.RESULT_PARAM_SIGN, sign);
//        }
//        return JSON.toJSONString(retMap);
//    }

    /**
     * map转json字符串
     * @param retMap
     * @return
     */
    public static String makeRetFail(Map retMap) {
        //{"code":"1111",msg:"签名错误"}

        return JSON.toJSONString(retMap);
    }

    /**
     * 验证支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        System.err.println(params);
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    /**
     * 验证VV平台支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key, String... noSigns) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        if(noSigns != null && noSigns.length > 0) {
            for (String noSign : noSigns) {
                params.remove(noSign);
            }
        }
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    public static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=").append(paraMap.get(key));
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }



}
