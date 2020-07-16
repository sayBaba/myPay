package com.hzit.alipay.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.hzit.common.req.AliapyPayReq;
import com.hzit.common.resp.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付宝的接口
 */
@RestController
@RequestMapping("/alipay")
public class AliapyController {

    private Logger logger = LoggerFactory.getLogger(AliapyController.class);


    //TODO  读springCloud config 从配置中心
    //应用私钥，用来加密数据
    private String  apiPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC64VIFZHn6vs8R7AWIE6VAcu9q2PooIEMjCKSQnUtgyDCBTxB/+yiPlzi+AfO8c8E6YYzMiBkKaECBHi1l/Lb/UU6bmG+si4xuI0LtRvgdbGXpN/YLBK+EHVgxcEpu7EfSwg07YaWVD1cM3ELU8fi+7Z1R1gcgmUlmzQNNAXqxLnHcNiyIwp9a0i7CkxxJgcJFr9w7Ctkt7v7kuMHQTzJXRYtDDfIRdSOjdozgkfGgQY2wyG/LxdGmzppavQkp+XcRw/Rnmphqc1Maw0PYEwFy4zfnoLxkCFTpsB5yT8re5ZYFB7sR+lmpgnQL48T9ay56GPfNAhmT1R70xQXCUSnzAgMBAAECggEAa7mK4FTlA6eOEdSt85zve1FFtZ/Rw+OlD8LhqgMh+m4rl76N0lhjLzKKoOzbJVzMm+yzjws89ATYehV9MfcVPeQOnh6m8R2YG7zzzXdFeyCLNn7ts7hFvLB1sDn8jihh1pnJqttE6cHn+h/V3o6FRvoN36iyfxYde8beCju6D/urq6PijfPZ/wYmL3LeruYsjm1vYIgLndm5eThbRsJ6VlTvywAJxpJ25Jco46yKH4GJV9AGMdJqMzADZ4RVgCTC5dUl/HM8j36/GJ+Jm8JrPpibv4Es375OAfsB5FVvv27djo5SmiL/YtcSkaA0TwgA6YIcj1QqL53Grey8neM5UQKBgQDe+t2QSGAwIo39QP+N7G9nYdZsGfATBWiS9FdLeI4DaeHUwraKvAajPrtbiBs+xrglq8e/z1nwodD4KjClN06sX+FSEytIrTNQqc1QvCM/9JbB3gdzhpS8zU6DWmE82rlm0nVfiYalqKa84Jjm7lpS8jm1city2FibcM1rRifMzQKBgQDWjerwdN86w6gVVR2oq9XKfmgAeydZoLBwqZvbsnO4pV/gaD7eIkuJ7XO+31h6JO8BodBGzaV3BndxUjqh9mtl4njUAPfPHh318yUuBzbLCo7ojvPiCIO5SU1YXRi+h5ctcIlE7/G/e9V1RTa96qkmxBgV/I9TbKZPCnTJnhzRvwKBgDtuqjeW0uyBiU8DUBpH9rmbfkd58l9fhawFrG7VRyTpx+OpsnXv/+TrB40yDfEBjnub+bXKUrKsrsI2nJis0S9oruntahAI+jB50du/CbY2PyiVQW8tY9+e6VphcKGDuv8JAXfhdB4bgbW+N4L17OVG4tLWB3ao65Uiz93T6S/RAoGBAM2UtmUo1yyrhUbLg9u2queYL3cmihHrUqThH6JnX1Kxwyh3OqYGHnNOvvVJdLG06f6aWcM+ESn3XEONEvlV8JpzYmthchFXPkYIcLul1e6/cyeKsk0G85B6mE3vBmKp8UVs61rOvslHksUHSOi5DNNEWf/XPE4ncUOTNUxq4BppAoGBAKC3Zf6w7iUPn/KvHqGAG/EfYlIyvHUv0e/+Ed7zGZ/js3DPNwOHA9fvpQFGjG2GoYClufloaRLzkE1DNxuGMXEFEMPDeB8wv785ECP0oJPwHO/Tb45180PQZmhnhlc+py7qZiwNiDzPiEa4vna4jeuwdxGoy3plM6/vZ55bwCEJ";

    //支付宝的公钥，用来验证支付宝返回的数据
    private String  alipayPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzJnbTT7sBQJTZvp3gGztei1V2eONrrbhxuPHojkAFTQzGE7nsWL2/TvVbOJihCq8JQtU9gSXBedNePetNLz4R7eMcZztTV9M9kxxwB5TKxjbI3l9DFDj3Q9sOUq8F1Afy8XiBfYdqvv+Haz4AWDdo6EljvXY6amrXbyBralIyXC/7exOqLs17/gx4DInfdf8ophOFbRSYbQCcRbDyxPdqT7mUY9ozfmoWaj/acjbH2gGGY26ptF9bDtkrPYLPgeIUNqYU1LsWgqqDxhL5eDYIGGvPMr5aFq9s29BjEAWdoDDAnUt8R0azhc1A6I1ONVspQToxPAMMVaYAirb1EXrQIDAQAB";

    private String appId = "2016101200667654";

    private String url = "https://openapi.alipaydev.com/gateway.do";

    private String NOTIFY_URL = "http://hzit666.free.idcfengye.com/alipayNotify/pay";


    /**
     * 支付宝扫码支付
     */
    @RequestMapping("/preCreate")
    public Result<String> toPay(@RequestBody AliapyPayReq aliapyPayReq ){
        logger.info("接受到支付请求,请求参数为：{}",aliapyPayReq);
        AlipayClient alipayClient = new DefaultAlipayClient(url,appId,apiPrivateKey,"json","utf-8",alipayPubKey,"RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        JSONObject jsonObject = new JSONObject();


        //必传参数
        jsonObject.put("out_trade_no",aliapyPayReq.getOutTradeNo());//
        jsonObject.put("total_amount", aliapyPayReq.getAmount());//
        jsonObject.put("subject",aliapyPayReq.getSubject());
        jsonObject.put("body", aliapyPayReq.getBody());

        //http://hzit666.free.idcfengye.com

        request.setNotifyUrl(NOTIFY_URL); //异步通知地址，支付成功后，支付宝回调的地址
        request.setBizContent(jsonObject.toJSONString()); //json字符串

        Result<String > result = new Result<String>();
        try {
            AlipayTradePrecreateResponse precreateResponse = alipayClient.execute(request);

           String code =  precreateResponse.getCode();
           if("10000".equals(code)){
               result.setMsg("成功");
               result.setCode(0);
               result.setData(precreateResponse.getQrCode());
//               return result;
           }else{
               result.setMsg(precreateResponse.getMsg());
               result.setCode(-1);
           }

        } catch (AlipayApiException e) {
            logger.error("AlipayApiException:{}",aliapyPayReq.getOutTradeNo(),e);
            result.setCode(-1);
            result.setMsg(e.getErrMsg());
//            return result;
        }
            logger.info("支付处理完成,返回参数为：{}",result);
            return result;
        //jsonjson
/*****************************goodsDetail对象******************************************/
/*        JSONArray  goodsDetail = new JSONArray(); //JSON数组

        JSONObject  goods = new JSONObject();
        goods.put("goods_id","001");
        goods.put("goods_name","测试商品");
        goods.put("quantity","1");
        goods.put("price","0.50");
        goods.put("goods_category","34543238");
        goods.put("categories_tree","124868003|126232002|126252004");
        goods.put("body","特价手机");
        goods.put("show_url","www.baidu.com");

        goodsDetail.add(goods);

        goods = new JSONObject();
        goods.put("goods_id","002");
        goods.put("goods_name","测试商品1");
        goods.put("quantity","1");
        goods.put("price","0.50");
        goods.put("goods_category","34543238");
        goods.put("categories_tree","124868003|126232002|126252004");
        goods.put("body","特价手机");
        goods.put("show_url","www.baidu.com");

        goodsDetail.add(goods);
        jsonObject.put("goods_detail",goodsDetail);*/

/*****************************goodsDetail对象封装******************************************/




    }
}
