package com.bit.soft.push.msEnum;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-28 11:55
 */
public enum HandledTypeEnum {
    /*
     * 未办
     */
    HANDLED_TYPE_NO(0,"未办"),
    /*
     * 已办
     */
    HANDLED_TYPE_YES(1,"已办");


    /**
     * 操作信息
     */
    private int code;

    private String info;

    /**
     * @param code  状态信息
     */
    HandledTypeEnum(int code,String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo(){
        return info;
    }
}
