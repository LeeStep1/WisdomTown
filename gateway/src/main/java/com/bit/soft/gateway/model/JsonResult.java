package com.bit.soft.gateway.model;
import java.io.Serializable;


/**
 * Created by lyj on 2018/8/31.
 */
public class JsonResult<T> implements Serializable {


    private static final long serialVersionUID = 1717488731978480120L;

    /*操作码*/
    private int code;

    /*返回的信息*/
    private String msg;

    /*返回的数据*/
    private T data;

    public JsonResult(int code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    public JsonResult(int code, String msg){
        this.code=code;
        this.msg=msg;

    }
    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
