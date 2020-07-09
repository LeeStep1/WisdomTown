package com.bit.soft.push.msEnum;

/**
 * @description:   发布推送的消息类型
 * @author: chenduo
 * @create: 2019-04-18 12:00
 */
public enum MsgTypeEnum {
    /**
     * 消息提醒
     */
    MSG_TYPE_MESSAGE(1,"消息提醒"),
    /**
     * 待办提醒
     */
    MSG_TYPE_WAIT(2,"待办提醒"),
    /**
     * 通知提醒
     */
    MSG_TYPE_NOTICE(3,"通知提醒"),
    /**
     * 公告提醒
     */
    MSG_TYPE_ANNOUNCEMENT(4,"公告提醒"),
    /**
     * 已办提醒
     */
    MSG_TYPE_ALREADY(5,"已办提醒");

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
    MsgTypeEnum(int code,String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * @description:  根据消息类型判断相关的消息枚举类
     * @author liyujun
     * @date 2020-05-12
     * @param msgTypeCode : 消息类型的code值
     * @return : java.lang.String
     */
    public static MsgTypeEnum getMsgType(int  msgTypeCode){
        MsgTypeEnum[] enums= MsgTypeEnum.values();
        MsgTypeEnum msgTypeEnum=null;
        for (MsgTypeEnum item:enums ) {
                 if(item.getCode()==msgTypeCode){
                     msgTypeEnum= item;
                 }
        }
        return msgTypeEnum;
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
