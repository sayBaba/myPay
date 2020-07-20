package com.hzit.pay.web.anno;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AutoIdempotent1 {

    /**
     * true 默认开启token验证，false不开启
     *
     * @return
     */
    boolean isOpen() default true;
}
