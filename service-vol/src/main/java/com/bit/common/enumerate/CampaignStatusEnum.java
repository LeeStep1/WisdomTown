package com.bit.common.enumerate;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-03 9:53
 */
public enum CampaignStatusEnum {

    /**
     * 活动状态 0-未开始 1-进行中 2-已结束 3-已取消 4-默认值
     */
    CAMPAIGN_STATUS_NOT_START(0,"未开始"),
    CAMPAIGN_STATUS_RUNNING(1,"进行中"),
    CAMPAIGN_STATUS_ENDED(2,"已结束"),
    CAMPAIGN_STATUS_CANCELED(3,"已取消"),
    CAMPAIGN_STATUS_DEFAULT(4,"默认值");

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
    CampaignStatusEnum(int code, String info) {
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
