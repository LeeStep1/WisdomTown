package com.bit.soft.push.utils;

import com.alibaba.fastjson.JSON;
import com.bit.base.exception.BusinessException;

import com.bit.soft.push.common.MqBaseConst;
import com.bit.soft.push.payload.MqDelay;
import com.bit.soft.push.payload.MqMessage;
import com.bit.soft.push.payload.MqNoticeMessage;
import com.bit.soft.push.payload.MqSendMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;



/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-04-15
 **/
public class SendMqPushUtil {


    private RabbitTemplate rabbitTemplate;


    public  SendMqPushUtil(RabbitTemplate rabbitTemplate){

        this.rabbitTemplate=rabbitTemplate;
    }

    /**
     * @param mqDelay :
     * @return : void
     * @description: 推送延迟活动消息提醒
     * @author liyujun
     * @date 2020-04-07
     */
    public void sendMqDelayMessage(MqDelay mqDelay) {

        mqDelay.checkParams();
        String s = JSON.toJSONString(mqDelay);
        this.rabbitTemplate.convertAndSend(MqBaseConst.MQ_DELAY_EXCHANGE, MqBaseConst.MQ_DELAY, s, (Message message)->{
            message.getMessageProperties().setDelay(mqDelay.getDelayTime().intValue());
            return message;
        } );

      /*  this.rabbitTemplate.convertAndSend(MqBaseConst.MQ_DELAY_EXCHANGE, MqBaseConst.MQ_DELAY, s, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message utils) throws AmqpException {
                utils.getMessageProperties().setDelay(mqDelay.getDelayTime().intValue());
                return utils;
            }
        });*/
    }

    /**
     * @param mqSendMessage : 消息模板，支持业务的消息和待办提醒
     * @return : void
     * @description: 发送消息，待办提醒通知
     * @author liyujun
     * @date 2020-04-07
     */
    public void sendMqMessage(MqSendMessage mqSendMessage) {

        MqMessage mqMessage = new MqMessage();
        BeanUtils.copyProperties(mqSendMessage, mqMessage);
        mqMessage.checkParams();
        String str = JSON.toJSONString(mqMessage);
        this.rabbitTemplate.convertAndSend(MqBaseConst.MQ_MESSAGES_EXCHANGE, MqBaseConst.MQ_MESSAGES, str);
    }

    /**
     * @param mqNoticeMessage : 发送综合管理平台的通知公告
     * @return : void
     * @description:
     * @author liyujun
     * @date 2020-04-07
     */
    public void sendMqNoticeMessage(MqNoticeMessage mqNoticeMessage) {
        mqNoticeMessage.checkParams();
        String str = JSON.toJSONString(mqNoticeMessage);
        sendMQ(MqBaseConst.MQ_NOTICE_EXCHANGE, MqBaseConst.MQ_MESSAGETONOCTICE, str);
    }

    /**
     * @param mqExchange :  交换机
     * @param topic      :  主题
     * @param payload    :  消息体
     * @return : void
     * @description: 原生的MQ發送消息
     * @author liyujun
     * @date 2020-04-07
     */
    public void sendMQ(String mqExchange, String topic, String payload) {
        if (this.rabbitTemplate == null) {
            throw new BusinessException("消息组件未初始化");
        } else {
            this.rabbitTemplate.convertAndSend(mqExchange, topic, payload);
        }

    }

    /**
     * @param rabbitTemplate :  mq发送工具
     * @return : void
     * @description: 初始化此工具类方法
     * @author liyujun
     * @date a
     */
    public void init(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
