package com.bit.support;

import com.alibaba.fastjson.JSON;

import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.soft.push.payload.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void sendMessage(Long id, MessageTemplateEnum msgTemplate, TargetTypeEnum targetType, Long[] userIds,
                            String[] params, String creator) {
        sendMessage(id, msgTemplate, targetType, userIds, params, creator, null);
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