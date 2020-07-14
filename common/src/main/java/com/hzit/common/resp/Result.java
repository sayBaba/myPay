package com.hzit.common.resp;

import lombok.Data;
import lombok.ToString;

/**
 * 接口公用返回对象
 */
@Data
@ToString
public class Result<T> {

    private int code;

    private String msg;

    private T data;

}
