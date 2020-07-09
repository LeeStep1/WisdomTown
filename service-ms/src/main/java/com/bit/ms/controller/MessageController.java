package com.bit.ms.controller;

import com.alibaba.fastjson.JSON;
import com.bit.ms.bean.Notification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-01-25
 **/
@RestController
@RequestMapping(value = "/ms")
public class MessageController {

    private static Log logger = LogFactory.getLog(MessageController.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @description:
     * @author liyujun
     * @date 2019-01-25

     * @return : java.lang.String
     */
    @GetMapping("/send{id}")
    public  String  sendMsg(@PathVariable(name = "id") String id){
        logger.info("发送websocket消息");
        messagingTemplate.convertAndSend("/topic/msg/"+id,"发消息"+System.currentTimeMillis());
        return "tt";
    }

    @GetMapping("/push")
    public  String  sendPushMsg(){
        Notification n = new Notification();
        n.setContent("这是一条测试通知");
        n.setTitle("测试通知");
        List<String> regedList = new ArrayList<>();
        regedList.add("140fe1da9eec9c88638");
        n.setRegIDList(regedList);
        String str = JSON.toJSONString(n);

        rabbitTemplate.convertAndSend("pushToAppExchange","topic.pushToApp",str);
        rabbitTemplate.convertSendAndReceive("pushToAppExchange","topic.pushToApp",str);
        return "tt";
    }
}
