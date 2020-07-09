package com.bit.base.vo;


import com.bit.common.ResultCode;

/**
 * @Description:基础VO,用于前后端数据传输
 * @Author: mifei
 * @Date: 2018-09-17
 **/
public class BaseVo<T> {

    /**
     * 操作码
     */
    private int code;

    /**
     * 返回的信息
     */
    private String msg;

    /**
     * 返回的数据
     **/
    private T data;

    public BaseVo(int code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public BaseVo(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public BaseVo() {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getInfo();
    }

    public BaseVo(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getInfo();
        this.data = data;
    }
    public BaseVo(ResultCode code){
        this.code = code.getCode();
        this.msg = code.getInfo();
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
