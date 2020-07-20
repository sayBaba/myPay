package com.hzit.pay.web.asper;

import com.hzit.pay.web.anno.AutoIdempotent1;
import com.hzit.pay.web.service.ITokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 切面
 */
@Aspect
@Component
public class TokenCheck {


    @Autowired
    private ITokenService iTokenService;

    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.hzit.pay.web.controller.*.*(..))")
    public void webLog(){
    }


    //通知
    @Before("webLog()&&@annotation(autoIdempotent1)")
    public void requestLimit(JoinPoint joinpoint, AutoIdempotent1 autoIdempotent1) {
        System.out.println("-----------autoIdempotent1-----"+ autoIdempotent1);

        boolean isOpen = autoIdempotent1.isOpen();
        if(isOpen){
            //检查 TOKEN

            //去获取request对象

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

            String token  = request.getParameter("token");

            if(StringUtils.isEmpty(token)){
                //获取respons对象返回
                try {
                    response.getWriter().println("token不能为空");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            try {
                boolean flag = iTokenService.checkToken(token);

                if(flag){
                    return;
                }

                response.getWriter().println("重复");

            } catch (Exception e) {
                e.printStackTrace();

            }


        }


        //token验证的逻辑写到这里

    }


}
