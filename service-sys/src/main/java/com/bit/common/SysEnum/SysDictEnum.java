package com.bit.common.SysEnum;

/**
 * Sys字典表枚举类
 * @author liyang
 * @create 2019-04-11
 */
public enum SysDictEnum {
    /**
     * 消息提醒
     */
    MESSAGE_REMIND(1,"msg_type"),

    /**
     * 待办提醒
     */
    UNDEAL_REMIND(2,"msg_type"),

    /**
     * 通知提醒
     */
    NOTICE_REMIND(3,"msg_type"),

    /**
     * 公告提醒
     */
    ANNOUNTCEMENT_REMIND(4,"msg_type"),

    /**
     * 已办提醒
     */
    DEAL_REMIND(5,"msg_type"),

    /**
     * 党建消息提醒
     */
    PB_MESSAGE_REMIND_CATEGORY(1,"remind_message_pb_category"),

    /**
     * 党建待办提醒
     */
    PB_TASK_REMIND_CATEGORY(1,"task_message_pb_category"),

    /**
     * 志愿者消息提醒
     */
    VOL_MESSAGE_REMIND_CATEGORY(8,"remind_message_vol_category"),

    /**
     * 志愿者待办提醒
     */
    VOL_TASK_REMIND_CATEGORY(8,"task_message_vol_category"),

    /**
     * 政务消息提醒
     */
    OA_MESSAGE_REMIND_CATEGORY(2,"remind_message_oa_category"),

    /**
     * 政务待办提醒
     */
    OA_TASK_REMIND_CATEGORY(2,"task_message_oa_category"),

    /**
     * 安监消息提醒
     */
    SV_REMIND_REMIND_MESSAGE_CATEGORY(3,"remind_message_sv_category"),

    /**
     * 环保消息提醒
     */
    EP_REMIND_REMIND_MESSAGE_CATEGORY(4,"remind_message_ep_category"),

    /**
     * 城建消息提醒
     */
    UC_REMIND_REMIND_MESSAGE_CATEGORY(6,"remind_message_uc_category");

    /**
     * 操作码
     */
    private int code;

    /**
     * 操作信息
     */
    private String info;

    /**
     * @param code  状态码
     * @param info  状态信息
     */
    SysDictEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }


    public String getInfo() {
        return info;
    }
}
