package com.bit.base.vo;


import com.bit.common.ResultCode;

/**
 * @Description:成功返回的不可变VO
 * @Author: mifei
 * @Date: 2018-09-17
 **/
public final class SuccessVo<T> extends BaseVo<T> {

    private final int code = ResultCode.SUCCESS.getCode();

    /**
     * 返回的信息
     */
    private final String msg = ResultCode.SUCCESS.getInfo();
    /**
     * 返回的数据
     **/

    @Override
    public T getData() {
        return null;
    }

    @Override
    public void setData(T data) {

    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {

    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {

    }
}
