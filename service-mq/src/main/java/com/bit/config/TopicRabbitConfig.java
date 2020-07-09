package com.bit.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.bit.soft.push.common.MqBaseConst.*;
//import static com.bit.mqCore.MqConst.MqBaseConst.*;

/**
* @Description: mq 配置类
* @author: mifei
* @create: 2019-02-21
**/
@Configuration
public class TopicRabbitConfig {

    @Bean
    public Queue queueMessage() {
        return new Queue(MQ_MESSAGE);
    }

    /**
     * 创建消息队列
     * @return
     */
    @Bean
    public Queue queueMessages() {
        return new Queue(MQ_MESSAGES);
    }

    /**
     * 创建通知公告队列
     * @return
     */
    @Bean
    public Queue queueMessageToNotice() {
        return new Queue(MQ_MESSAGETONOCTICE);
    }
    /**
     * 创建延迟队列
     * @return
     */
    @Bean
    public Queue queueMessageDelay(){
        return new Queue(MQ_DELAY);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(MQ_SYSLOG_EXCHANGE);
    }

    /**
     * 创建消息队列交换机
     * @return
     */
    @Bean
    TopicExchange exchangeToMessages() {
        return new TopicExchange(MQ_MESSAGES_EXCHANGE);
    }

    /**
     * 创建通知公告队列交换机
     * @return
     */
    @Bean
    TopicExchange exchangeToMessageToNotice() {
        return new TopicExchange(MQ_NOTICE_EXCHANGE);
    }

    /**
     * 创建延迟队列交换机
     * @return
     */
    @Bean
    CustomExchange exchangeToDelay() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MQ_DELAY_EXCHANGE,"x-delayed-message",true,false,args);
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(MQ_MESSAGE);
    }

    /**
     * 创建消息队列binding
     * @param queueMessages  要绑定的队列
     * @param exchangeToMessages   要绑定的交换机
     * @return
     */
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchangeToMessages) {
        return BindingBuilder.bind(queueMessages).to(exchangeToMessages).with(MQ_MESSAGES);
    }

    /**
     * 创建通知公告队列binding
     * @param queueMessageToNotice  要绑定的队列
     * @param exchangeToMessageToNotice  要绑定的交换机
     * @return
     */
    @Bean
    Binding bindingExchangeMessageToNotice(Queue queueMessageToNotice, TopicExchange exchangeToMessageToNotice) {
        return BindingBuilder.bind(queueMessageToNotice).to(exchangeToMessageToNotice).with(MQ_MESSAGETONOCTICE);
    }

    /**
     * 创建延迟队列binding
     * @return
     */
    @Bean
    Binding bindingExchangeMessageToDelay() {
        return BindingBuilder.bind(queueMessageDelay()).to(exchangeToDelay()).with(MQ_DELAY).noargs();
    }
}
