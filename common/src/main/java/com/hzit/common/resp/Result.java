package com.hzit.common.resp;

import lombok.Data;

/**
 * 接口公用返回对象
 */
@Data
public class Result<T> {

    private int code;

    private String msg;

    private T t;

}
