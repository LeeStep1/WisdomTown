package com.bit.ms.service;

import com.alibaba.fastjson.JSON;
import com.bit.config.PushRabbitConfig;
import com.bit.ms.bean.Notification;
import com.bit.ms.bean.PushLog;
import com.bit.ms.util.JPushUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: 消息接收并通过极光推送到手机上
 * @Author: Liy
 * @Date: 2019-02-28
 **/
@Component
@RabbitListener(queues = PushRabbitConfig.PUSHMESSAGE)
public class PushMessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(PushMessageReceiver.class);

    @Autowired
    private JPushUtil jPush;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RabbitHandler
    public void process(Channel channel, String str,Message message) {
        try {
            //获取推送消息
            Notification notification = JSON.parseObject(str,Notification.class);

//            int  t = 1/0;
            //告诉服务器已经收到消息 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

            PushLog pl = new PushLog();
            pl.setPushTitle(notification.getTitle());
            pl.setPushContent(notification.getContent());
            pl.setPushType("reg");
            pl.setPushFlg(true);

            mongoTemplate.save(pl,"pushLog");
            //推送
//            jPush.pushNotificationByRegIds(notification.getTitle(),notification.getContent(),notification.getRegIDList(),notification.getExtras());

        }catch (Exception ex){

            PushLog pl = new PushLog();
            pl.setPushContent(str);
            pl.setPushType("reg");
            pl.setPushFlg(false);
            mongoTemplate.save(pl,"pushLog");
            logger.info("推送失败 ：" + ex.getMessage());
        }
    }

}
