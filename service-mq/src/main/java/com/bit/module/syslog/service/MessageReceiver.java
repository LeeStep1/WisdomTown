package com.bit.module.syslog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static com.bit.soft.push.common.MqBaseConst.MQ_MESSAGE;


/**
 * @Description: 消息接收
 * @Author: mifei
 * @Date: 2019-02-15
 **/
@Component
@RabbitListener(queues = MQ_MESSAGE)
@Slf4j
public class MessageReceiver {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitHandler
    public void process(String messageStr) {

        //接收到的消息，存入mongodb中
        log.info("Topic Receiver1 {},消息为：{}",MQ_MESSAGE,messageStr);
        mongoTemplate.save(messageStr,"operate_logs");
    }

}
