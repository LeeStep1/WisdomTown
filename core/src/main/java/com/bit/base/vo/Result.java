package com.bit.base.vo;

import com.bit.common.ResultCode;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2018-09-17
 **/
public class Result <T>{


    /**
     * 操作状态码
     */
    private int  code;

    /**
     * 操作信息
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    public Result() {

    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @description:
     * @author liyujun
     * @date 2018-09-17
     * @param code : 
     * @param msg : 
     * @return : Result<E>
     */
    public static <E> Result<E> fail(int code, String msg) {

        return new Result<E>(code, msg,null);
    }

    /**
     * @description:
     * @author liyujun
     * @date 2018-09-17
     * @param msg : 
     * @return : Result<E>
     */
    public static <E> Result<E> fail(String msg) {
        return fail(ResultCode.WRONG.getCode(), msg);
    }

    /**
     * @description:
     * @author liyujun
     * @date 2018-09-17
     * @param code : 
     * @param msg : 
     * @param data : 
     * @return : Result<E>
     */
    public static <E> Result<E> timeout(int code, String msg, E data) {

        return new Result<E>(ResultCode.HYSTRIX_TIME_OUT.getCode(), ResultCode.HYSTRIX_TIME_OUT.getInfo() ,null);
    }

    /**
     * @description:
     * @author liyujun
     * @date 2018-09-17
     * @param data : 返回数据
     * @return : Result<E>
     */
    public static <E> Result<E> success(E data) {
        return new Result<E>(ResultCode.SUCCESS.getCode(), null,data);
    }

    /**
     * @description:
     * @author liyujun
     * @date 2018-09-17
     * @param code :
     * @param message :
     * @param data :
     * @return : Result<E>
     */
    public static <E> Result<E> success(int code, String message, E data) {
        return new Result<E>(code, message,data);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
