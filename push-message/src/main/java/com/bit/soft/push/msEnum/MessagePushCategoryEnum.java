package com.bit.soft.push.msEnum;

/**
 * 消息category 枚举类  计划废弃
 *
 * @author mifei
 * @create 2019-09-04
 */
public enum MessagePushCategoryEnum {

    /**
     * 党建消息提醒
     */
    PB_MESSAGE_REMIND_CATEGORY(1, 1, "remind_message_pb_category"),

    /**
     * 党建待办提醒
     */
    PB_TASK_REMIND_CATEGORY(1, 2, "task_message_pb_category"),

    /**
     * 志愿者消息提醒
     */
    VOL_MESSAGE_REMIND_CATEGORY(8, 1, "remind_message_vol_category"),

    /**
     * 志愿者待办提醒
     */
    VOL_TASK_REMIND_CATEGORY(8, 2, "task_message_vol_category"),

    /**
     * 政务消息提醒
     */
    OA_MESSAGE_REMIND_CATEGORY(2, 1, "remind_message_oa_category"),

    /**
     * 政务待办提醒
     */
    OA_TASK_REMIND_CATEGORY(2, 2, "task_message_oa_category"),

    /**
     * 安检消息提醒
     */
    SV_REMIND_REMIND_MESSAGE_CATEGORY(3, 1, "remind_message_sv_category"),

    /**
     * 环保消息提醒
     */
    EP_REMIND_REMIND_MESSAGE_CATEGORY(4, 1, "remind_message_ep_category"),

    /**
     * 城建消息提醒
     */
    UC_REMIND_REMIND_MESSAGE_CATEGORY(6, 1, "remind_message_uc_category"),

    /**CBO 社区模块**/
    /**
     * 社区模块消息
     */
    REMIND_MESSAGE_CBO_CATEGORY(5,1,"remind_message_cbo_category"),

    /**
     * 社区模块待办
     */
    TASK_MESSAGE_CBO_CATEGORY(5,2,"task_message_cbo_category");

    /**
     * 应用id
     */
    private int appid;
    /**
     * 提醒类型，如消息，待办
     */
    private int msgType;

    /**
     * 对应dict表的module
     */
    private String module;

    /**
     * @param appid  appid
     * @param module module
     */
    MessagePushCategoryEnum(int appid, int msgType, String module) {
        this.appid = appid;
        this.msgType = msgType;
        this.module = module;
    }

    public int getAppid() {
        return appid;
    }


    public String getModule() {
        return module;
    }

    public static MessagePushCategoryEnum getTypeByAppidAndMsgType(int appid, int msgType) {
        for (MessagePushCategoryEnum ftype : MessagePushCategoryEnum.values()) {
            if (ftype.appid == appid && ftype.msgType == msgType) {
                return ftype;
            }
        }
        return null;
    }
}
