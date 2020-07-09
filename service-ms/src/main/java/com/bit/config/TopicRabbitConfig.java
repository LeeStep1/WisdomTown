package com.bit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.bit.soft.push.common.MqBaseConst.*;


@Configuration
public class TopicRabbitConfig {

    /**
     * websocket队列
     */
    @Bean
    public Queue queueMessageToWeb() {
        return new Queue(MS_MESSAGETOWEB);
    }

    /**
     * 极光推送队列
     */
    @Bean
    public Queue queueMessageToJpush() {
        return new Queue(MS_MESSAGETOJPUSH);
    }
 /*   public Queue queueMessageToJpush() {
        return new Queue(MS_MESSAGETOJPUSH+"_lyj");
    }*/

    /**
     * websocket交换机
     */
    @Bean
    TopicExchange exchangeToWeb() {
        return new TopicExchange(MS_WEB_EXCHANGE);
    }

    /**
     * 极光交换机
     */
    @Bean
    TopicExchange exchangeToJpush() {
        return new TopicExchange(MS_JPUSH_EXCHANGE);
    }
/*    TopicExchange exchangeToJpush() {
        return new TopicExchange(MS_JPUSH_EXCHANGE+"_lyj");
    }*/
    @Bean
    Binding bindingExchangeMessageToWeb(Queue queueMessageToWeb, TopicExchange exchangeToWeb) {
        return BindingBuilder.bind(queueMessageToWeb).to(exchangeToWeb).with(MS_MESSAGETOWEB);
    }

    @Bean
    Binding bindingExchangeMessageToJpush(Queue queueMessageToJpush, TopicExchange exchangeToJpush) {
        return BindingBuilder.bind(queueMessageToJpush).to(exchangeToJpush).with(MS_MESSAGETOJPUSH);
    }
/*    Binding bindingExchangeMessageToJpush(Queue queueMessageToJpush, TopicExchange exchangeToJpush) {
        return BindingBuilder.bind(queueMessageToJpush).to(exchangeToJpush).with(MS_MESSAGETOJPUSH+"_lyj");
    }*/
}
