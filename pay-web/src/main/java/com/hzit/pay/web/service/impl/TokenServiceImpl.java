package com.hzit.pay.web.service.impl;

import com.hzit.pay.web.service.ITokenService;
import com.hzit.pay.web.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *  token服务接口实现类
 */
@Service
public class TokenServiceImpl implements ITokenService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String TOKEN_PREFIX = "TOKEN:";


    @Override
    public String createToken() {
        //生成UUID 做为token
        String toke = UUID.randomUUID().toString().replace("-","");
        System.out.println("jdk生成的uuid："+ toke);
        //redis 的key
        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN_PREFIX).append(toke);
        //存入redis
        redisUtil.setEx(builder.toString(),toke,10000L);
        return toke;
    }

    /**
     * 判断token是否存在
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkToken(String token) throws Exception {

     boolean flag = redisUtil.exists(TOKEN_PREFIX+token);
     if(!flag){
         return false;
     }
//     String redisToken = (String)redisUtil.get(TOKEN_PREFIX+token);

        boolean flag1 =  redisUtil.remove(TOKEN_PREFIX+token);

     if(flag1 == true){
         return true;

     }
        return false;
    }
}
