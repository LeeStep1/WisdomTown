package com.bit.common.config;


import com.bit.soft.push.utils.SendMqPushUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 初始化消息和待办工具类推送
 * @Author: liyujun
 * @Date: 2020-04-15
 **/
@Configuration
public class MqPushConfig {

    @Autowired
    private RabbitTemplate  rabbitTemplate;


    @Bean(name="sendMqPushUtil")
    public SendMqPushUtil  init(){
       return  new SendMqPushUtil(rabbitTemplate);
    }
}
