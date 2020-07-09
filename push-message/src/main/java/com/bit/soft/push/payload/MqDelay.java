package com.bit.soft.push.payload;

import com.bit.base.exception.BusinessException;
import lombok.Data;

/**
 * @description: 志愿者活动延迟提醒业务的相关消息体
 * @author: chenduo
 * @create: 2019-04-17 11:11
 */
@Data
public class MqDelay {
    /**
     * 活动id  广义的业务数据的id
     */
    private Long campaignId;
    /**
     * 活动主题 计划废弃
     */
    //private String campaignTheme;
    /**
     * 活动地点 计划废弃
     */
    //private String campaignPlace;
    /**
     * 活动日期 计划废弃
     */
    //private Integer startDate;
    /**
     * 活动时间 计划废弃
     */
    //private String startTime;
    /**
     * 延迟时间
     */
    private Long delayTime;
    /**
     * 目标人群
     */
    private Long[] targetIds;
    /**
     * 消息类型 计划废弃
     */
    //private Integer msgType;
    /**
     * 消息类型名称 计划废弃
     */
    //private String msgTypeName;
    /**
     * 应用id 计划废弃
     */
    //private Integer appId;
    /**
     * 模板id
     */
    private Long templateId;
    /**
     * 消息参数
     */
    private String[] params;


    //新增
    /**
     * 定义app端极光推送的方式 默认 0：别名推送(别名推送目前默认一种组装方式)，1 标签推送
     */
    private Integer appJpushType;



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void checkParams(){

        if(this.getCampaignId() == null ||this.getCampaignId().equals(0L)){
            throw new BusinessException("参数不全：CampaignId 为空");
        }
        if(this.getTemplateId() == null || this.getTemplateId().equals(0L)){
            throw new BusinessException("消息发送失败：TemplateId 为空");
        }
        if(this.getDelayTime() == null ){
            throw new BusinessException("消息发送失败：DelayTime 为空");
        }
        if(this.getParams() == null || this.getParams().length <= 0){
            throw new BusinessException("消息发送失败：Params 为空");
        }
      /*  if(this.getTargetIds() == null || this.getTargetIds().length <= 0){
            throw new BusinessException("消息发送失败：targetIds 为空");
        }*/

        /*新增校验推送*/
        if(this.getAppJpushType() == null ){
            throw new BusinessException("消息发送失败：appJpushType为空");
        }

    }
}
