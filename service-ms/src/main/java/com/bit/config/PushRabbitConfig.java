package com.bit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: mq推送消息队列
 * @author: liy
 * @create: 2019-02-28
 **/
@Configuration
public class PushRabbitConfig {

    final public static String PUSHMESSAGE = "topic.pushToApp";
    //final public static String PUSHMESSAGE = "topic.pushToApp"+"_lyj";
    @Bean
    public Queue queueMessage() {
        return new Queue(PushRabbitConfig.PUSHMESSAGE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("pushToAppExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(PushRabbitConfig.PUSHMESSAGE);
    }
}
