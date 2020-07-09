package com.bit.module.oa.enums;

/**
 * @Description :
 * @Date ： 2019/2/14 15:37
 */
public enum LogOperateEnum {
    INSPECT_PUBLISH(1, "发布", "发布任务"),
    INSPECT_GIVE_UP(2, "放弃", "放弃任务"),
    INSPECT_EXECUTE(3, "执行", "开始执行"),
    REPORT(4, "上报", "上报记录"),
    CONFIRM_REPORT(5, "确认", "确认上报记录"),
    INSPECT_SIGN(6, "签到", "签到"),
    INSPECT_END(7, "结束", "结束任务"),
    INSPECT_CONFIRM_MISSION(8, "确认", "确认任务"),
    APPLY_PATCH(9, "申请", "申请补卡"),
    APPLY_AUDIT(10, "审核", "审核通过"),
    APPLY_REJECT(11, "驳回", "驳回审核"),
    MEETING_PUBLISH(12, "发布", "发起申请"),
    MEETING_AUDIT(13, "审核", "审核通过"),
    MEETING_REJECT(14, "驳回", "驳回审核"),
    MEETING_CANCEL(15, "取消", "取消会议"),
    MEETING_INVALID(16, "失效", "会议失效");


    private Integer key;

    private String operate;

    private String content;

    LogOperateEnum(Integer key, String operate, String content) {
        this.key = key;
        this.operate = operate;
        this.content = content;
    }

    public Integer getKey() {
        return key;
    }

    public String getOperate() {
        return operate;
    }

    public String getContent() {
        return content;
    }
}
