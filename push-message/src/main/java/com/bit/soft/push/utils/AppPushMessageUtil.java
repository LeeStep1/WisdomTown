package com.bit.soft.push.utils;

import com.bit.base.exception.BusinessException;

import com.bit.soft.push.msEnum.AppPushTypeEnum;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.soft.push.payload.MqDelay;
import com.bit.soft.push.payload.MqSendMessage;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description: 应用业务服务层构建推送消息模板参数类
 * @Author: liyujun
 * @Date: 2019-09-04
 **/
public class AppPushMessageUtil {

    private AppPushMessageUtil() {

    }

    /**
     * @param messageTemplateEnum :模板ID
     * @param userIdArrays        : 推送的目标ID userID
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加 非必输
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description: 根据模，别名推送板推送到人
     * @author
     * @date 2019-09-04
     */
    public static MqSendMessage pushUserMessageByAlias(MessageTemplateEnum messageTemplateEnum, Long[] userIdArrays, String[] params, String creater, Date createTime, String pushTime) {
        if (userIdArrays.length == 0) {
            throw new BusinessException("推送的人员不能为空");
        }

        return intProperties(Long.valueOf(messageTemplateEnum.getId()), userIdArrays, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.USER, null, null, creater, createTime, pushTime, null);


    }
    /**
     * @param buzId :业务I表单D
     * @param messageTemplateEnum :模板ID
     * @param userIdArrays        : 推送的目标ID userID
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加 非必输
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description: 根据模，别名推送板推送到人
     * @author
     * @date 2019-09-04
     */
    public static MqSendMessage pushUserMessageWithBuzIdByAlias(Long buzId,MessageTemplateEnum messageTemplateEnum, Long[] userIdArrays, String[] params, String creater, Date createTime, String pushTime) {
        if (userIdArrays.length == 0) {
            throw new BusinessException("推送的人员不能为空");
        }

        return intProperties(Long.valueOf(messageTemplateEnum.getId()), userIdArrays, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.USER, buzId, null, creater, createTime, pushTime, null);


    }

    /**
     * @param messageTemplateEnum :模板枚举
     * @param orgId               : 组织的ID（sys  表中的相关业务的）
     * @param params              :模板的参数
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加,非必输
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description:根据模版，别名推送板推送到组织下的人
     * @author
     * @date 2019-09-04
     */
    public static MqSendMessage pushOrgMessageByAlias(MessageTemplateEnum messageTemplateEnum, Long[] orgId, String[] params, String creater, Date createTime, String pushTime) {

        if (orgId.length == 0) {
            throw new BusinessException("推送的组织ID不能为空");
        }

        return intProperties(Long.valueOf(messageTemplateEnum.getId()), orgId, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.ORG, null, null, creater, createTime, pushTime, null);


    }

    public static MqSendMessage pushOrgMessageWithBuzIByAlias(Long buzId,MessageTemplateEnum messageTemplateEnum, Long[] orgId, String[] params, String creater, Date createTime, String pushTime) {

        if (orgId.length == 0) {
            throw new BusinessException("推送的组织ID不能为空");
        }

        return intProperties(Long.valueOf(messageTemplateEnum.getId()), orgId, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.ORG, buzId, 0, creater, createTime, pushTime, null);


    }

    /**
     * @param messageTemplateEnum :模板ID
     * @param params              :模板的参数
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description:根据模版，标签推送
     * @author
     * @date 2019-09-04
     */
    public static MqSendMessage pushMessageByTag(MessageTemplateEnum messageTemplateEnum, List<String> tagList, String[] params, String creater, Date createTime, String pushTime) {
        if (tagList == null || tagList.size() == 0) {
            throw new BusinessException("推送的标签不能为空");
        }
        return intProperties(Long.valueOf(messageTemplateEnum.getId()), null, params, AppPushTypeEnum.PUSHBYTAG, TargetTypeEnum.TAGSUSER, null, null, creater, createTime, pushTime, tagList);

    }

    /**
     * @param messageTemplateEnum :模板ID
     * @param userId              : 用户ID
     * @param params              : 模板参数
     * @param businessId          : 业务层的表单ID
     * @param version             :  业务版本号
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description:根据模版，别名推送待办
     * @author
     * @date 2019-09-04
     */
    public static MqSendMessage pushUserTaskByAlias(MessageTemplateEnum messageTemplateEnum, Long[] userId, String[] params, Long businessId, Integer version, String creater, Date createTime, String pushTime) {

        if (businessId == null) {
            throw new BusinessException("业务的表单数据不能为空");
        }
        if (version == null) {
            throw new BusinessException("业务的表单版本号不能为空");
        }
        if (userId == null || userId.length == 0) {
            throw new BusinessException("推送的人ID不能为空");
        }

        return intProperties(Long.valueOf(messageTemplateEnum.getId()), userId, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.USER, businessId, version, creater, createTime, pushTime, null);

    }

    /**
     * @param messageTemplateEnum :模板ID
     * @param orgId               : 组织的ID（sys  表中的相关业务的）
     * @param params              :模板的参数
     * @param businessId          : 业务层的表单ID
     * @param version             :  业务版本号
     * @param creater             :创建人 ，非必输
     * @param createTime          :发布时间，非必输
     * @param pushTime            :定时推送时请加
     * @return : com.bit.module.mqCore.MqBean.MqMessage
     * @description:根据模版，别名推送板推送待办到组织下的人 目前支持党建 志愿者和 政务
     * @author liyujun
     * @date 2019-09-04
     */
    public static MqSendMessage pushOrgTaskByAlias(MessageTemplateEnum messageTemplateEnum, Long[] orgId, String[] params, Long businessId, Integer version, String creater, Date createTime, String pushTime) {

        if (businessId == null) {
            throw new BusinessException("业务的表单数据不能为空");
        }
        if (version == null) {
            throw new BusinessException("业务的表单版本号不能为空");
        }
        if (orgId == null || orgId.length == 0) {
            throw new BusinessException("推送的组织ID不能为空");
        }
        return intProperties(Long.valueOf(messageTemplateEnum.getId()), orgId, params, AppPushTypeEnum.PUSHBYALIASE, TargetTypeEnum.ORG, businessId, version, creater, createTime, pushTime, null);

    }

    /**
     * @description: 构建发送消息体方法
     * @param templteId       : 模板ID
     * @param targetId        : 目标ID
     * @param params          : 附加参数，替换模板中的占位符信息
     * @param appPushTypeEnum : 推送的类型
     * @param targetTypeEnum  : 目标的类型
     * @param businessId      : 业务表单的ID
     * @param version         : 表单的版本，对待待办类的消息有作用。
     * @param creater         : 创建者
     * @param createTime      : 常见创建时间
     * @param pushTime        : 推送时间 （app:定时推送的时间）
     * @param tagList         : 标签（移动端推送的）
     * @return : com.bit.module.mqCore.MqBean.MqSendMessage
     * @author liyujun
     * @date 2020-04-15
     */
    private static MqSendMessage intProperties(Long templteId, Long[] targetId, String[] params, AppPushTypeEnum appPushTypeEnum, TargetTypeEnum targetTypeEnum, Long businessId, Integer version, String creater, Date createTime, String pushTime, List<String> tagList) {
        MqSendMessage a = new MqSendMessage();

        if (templteId == null) {
            throw new BusinessException("模板不能为空");
        } else if (appPushTypeEnum == null) {
            throw new BusinessException("推送的类型不能为空");
        } else /*if (params.length == 0) {
            throw new BusinessException("推送的拼装参数不能为空");
        } else*/ if (targetTypeEnum == null) {
            throw new BusinessException("推送的目标参数不能为空");
        }
        a.setTemplateId(templteId);
        a.setAppJpushType(appPushTypeEnum.getPushCode());
        a.setParams(params);
        a.setTargetType(targetTypeEnum.getCode());

        if (targetId != null && targetId.length > 0) {
            a.setTargetId(targetId);
        }
        if (businessId != null) {
            a.setBusinessId(businessId);
        }
        if (version != null) {
            a.setVersion(version);
        }
        if (!StringUtils.isEmpty(creater)) {
            a.setCreater(creater);
        }
        if (createTime != null) {
            a.setCreateTime(createTime);
        }
        if (!StringUtils.isEmpty(pushTime)) {
            a.setPushTime(pushTime);
        }
        if (tagList != null && tagList.size() > 0) {
            a.setTagList(tagList);
        }
        return a;

    }

    /**
     * @description:  构建延迟消息模板
     * @author liyujun
     * @date 2020-04-16
     * @param buzId :
     * @param messageTemplateEnum :
     * @param delayTime :
     * @return : com.bit.module.mqCore.MqBean.MqDelay
     */
    public  static MqDelay pushOrgDelayMessageByAlias(Long buzId, MessageTemplateEnum messageTemplateEnum, String [] param, Long delayTime){
        MqDelay mqDelay=new MqDelay();
        mqDelay.setCampaignId(buzId);
        mqDelay.setDelayTime(delayTime);
        mqDelay.setTemplateId(Long.valueOf(messageTemplateEnum.getId()));
        mqDelay.setAppJpushType(AppPushTypeEnum.PUSHBYALIASE.getPushCode());
        mqDelay.setParams(param);
        return mqDelay;
    }

}
