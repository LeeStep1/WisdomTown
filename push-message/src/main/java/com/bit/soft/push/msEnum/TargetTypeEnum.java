package com.bit.soft.push.msEnum;

/**
 *  发送的推送的目标的类型，对应targetType和targetUserType
 * 0所有用户  1用户  2组织
 * @author mifei
 * @create 2019-04-02 11:18
 **/
public enum TargetTypeEnum {
    /*
     * 0所有用户   客户端全部调用时，all 为标签推送
     */
    ALL(0,"所有用户"),
    /*
     * 1用户
     */
    USER(1,"用户"),
    /*
     * 2组织
     */
    ORG(2,"组织"),


    /**
     *自有的业务表用户
     */
    SERVICEUSERS(4,"自有的业务表用户"),


    /**
     *自有的业务表用户
     */
    TAGSUSER(5,"标签推送无用户"),


    /**组织时在进行判断**/

    /**
     * 党委组织时，再判断   内部用户
     */
    INTERNALUSER(1,"内部用户"),

    /**
     * 外部用户
     */
    EXTERNALUSER(2,"外部用户");


    /**
     * 操作信息
     */
    private int code;

    /**
     * 操作内容
     */
    private String info;

    /**
     * @param code  状态信息
     */
    TargetTypeEnum(int code,String info) {
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
