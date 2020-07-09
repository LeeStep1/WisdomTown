package com.bit.module.oa.utils;

import com.alibaba.fastjson.JSON;
import com.bit.base.vo.BaseVo;

import com.bit.module.oa.feign.SysServiceFeign;
import com.bit.module.oa.vo.user.BusinessRelRole;
import com.bit.module.oa.vo.user.BusinessRolePage;
import com.bit.module.oa.vo.user.UserVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.soft.push.payload.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.bit.soft.push.common.MqBaseConst.MQ_MESSAGES;
import static com.bit.soft.push.common.MqBaseConst.MQ_MESSAGES_EXCHANGE;


/**
 * @Description :
 * @Date ： 2019/4/17 11:29
 */
@Component
@Slf4j
public class PushUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SysServiceFeign sysServiceFeign;

    public void sendMessage(Long id, MessageTemplateEnum msgTemplate, TargetTypeEnum targetType, Long[] userIds,
                            String[] params, String creator) {
        sendMessage(id, msgTemplate, targetType, userIds, params, creator, 0);
    }

    public void sendMessageByFlow(Long id, Integer flowId, MessageTemplateEnum msgTemplate, TargetTypeEnum targetType,
                                  String[] params, String creator, Integer version) {

        // 查询需要审批的角色
        BaseVo<BusinessRolePage> baseVo = sysServiceFeign.reflectByFlowId(flowId);
        BusinessRolePage data = baseVo.getData();
        if (data == null || CollectionUtils.isEmpty(data.getBusinessRelRoleList())) {
            return;
        }
        List<Long> roleIds = data.getBusinessRelRoleList().stream().map(BusinessRelRole::getRoleId)
                .collect(Collectors.toList());
        // 查询角色对应的人员
        BaseVo<List<UserVO>> batchUser = sysServiceFeign.queryUserByRoleBatch(roleIds);
        List<UserVO> userData = batchUser.getData();
        if (CollectionUtils.isEmpty(userData)) {
            return;
        }
        Long[] userIds = userData.stream().map(UserVO::getId).collect(Collectors.toList()).toArray(new Long[]{});

        // 发送推送
        this.sendMessage(id, msgTemplate, targetType, userIds, params, creator, version);
    }

    public void sendMessage(Long id, MessageTemplateEnum msgTemplate, TargetTypeEnum targetType, Long[] userIds,
                            String[] params, String creator, Integer version) {
        MqMessage mqMessage = new MqMessage();
        //业务ID
        mqMessage.setBusinessId(id);

        //模板ID
        mqMessage.setTemplateId((long) msgTemplate.getId());

        //推送类型  0 所有用户 1 用户 2 组织
        mqMessage.setTargetType(targetType.getCode());

        //推送人员
        mqMessage.setTargetId(userIds);

        if (TargetTypeEnum.ORG.equals(targetType)) {
            //用户类型 1 内部用户   2、外部用户
            mqMessage.setTargetUserType(String.valueOf(TargetTypeEnum.INTERNALUSER.getCode()));
        }

        //当前版本
        mqMessage.setVersion(version);

        //作者
        mqMessage.setCreater(creator);

        //模板参数
        mqMessage.setParams(params);

        String str = JSON.toJSONString(mqMessage);
        log.info("发送消息 : {}", str);
        rabbitTemplate.convertAndSend(MQ_MESSAGES_EXCHANGE, MQ_MESSAGES, str);
    }
}