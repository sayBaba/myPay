package com.hzit.pay.web.service;

/**
 * token服务接口
 */
public interface ITokenService {


    /**
     * 创建token
     * @return
     */
    public  String createToken();


    /**
     * 检验token
     * @param token
     * @return
     */
    public boolean checkToken(String token) throws Exception;


}
