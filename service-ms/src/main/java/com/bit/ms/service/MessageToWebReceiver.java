package com.bit.ms.service;

import com.alibaba.fastjson.JSON;
import com.bit.base.exception.BusinessException;
import com.bit.soft.push.payload.MsMessage;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.bit.soft.push.common.MqBaseConst.MS_MESSAGETOWEB;

/**
 * @Description: 消息接收并推送到web端websocket上
 * @Author: mifei
 * @Date: 2019-02-15
 **/
@Component
@RabbitListener(queues = MS_MESSAGETOWEB)
public class MessageToWebReceiver {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
//    @Autowired
//    private SysServiceFeign sysServiceFeign;

    private static final Logger logger = LoggerFactory.getLogger(MessageToWebReceiver.class);

    /***
     * 推送消息至websocket
     * @author liyang
     * @date 2019-04-02
     * @param messageStr : 消息
     * @return : void
    */
    @RabbitHandler
    public void process(Channel channel, String messageStr, Message message) {
        logger.info("websocket received  : {}",messageStr);

        MsMessage msMessage = JSON.parseObject(messageStr,MsMessage.class);

        try {
            //告诉服务器已经收到消息 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception ex){
            throw new BusinessException("消息确认失败");
        }

        //循环数组发送websocket
        if (msMessage.getTid()!=null && msMessage.getTid().length()>0){
            for (Long userId : msMessage.getUserId()) {
                messagingTemplate.convertAndSend("/topic/msg/"+userId,userId);
                logger.info("success push  /topic/msg/" + userId);
            }
        }


//        MqMessage mqMessage = JSON.parseObject(messageStr,MqMessage.class);
        //如果用户id是空 判断组织id 批量添加 否则直接添加
//        if (mqMessage.getUserId()==null ){
//            if (mqMessage.getOrgId()!=null){
//                Long appId = mqMessage.getAppId();
//                Long orgId = mqMessage.getOrgId();
//                if (orgId!=null){
//                    OrgAndName orgAndName = new OrgAndName();
//                    orgAndName.setAppId(mqMessage.getAppId());
//                    List<String> pbOrgIds = new ArrayList<>();
//                    List<Long> orgIds = new ArrayList<>();
//                    if (appId.equals(1L)){
//                        pbOrgIds.add(orgId+"");
//                        orgAndName.setPbOrgIds(pbOrgIds);
//                    }
//                    if (appId.equals(2L)){
//                        orgIds.add(orgId);
//                        orgAndName.setOrgIds(orgIds);
//                    }
//
//                    BaseVo b1 = sysServiceFeign.queryUserByAppIdOrgIdsName(orgAndName);
//                    String ss = JSON.toJSONString(b1.getData());
//                    List<User> userList = JSON.parseArray(ss, User.class);
//                    if (userList!=null && userList.size()>0){
//                        for (User user : userList) {
//                            Message ms = new Message();
//                            ms.setTitle(mqMessage.getTitle());
//                            ms.setAppId(appId);
//                            ms.setCategoryId(mqMessage.getCategoryId());
//                            ms.setUserId(user.getId());
//                            //调用sys服务添加消息
//                            BaseVo base = sysServiceFeign.add(ms);
//                            String str = JSON.toJSONString(base.getData());
//                            Message mm = JSON.parseObject(str,Message.class);
//                            messagingTemplate.convertAndSend("/topic/msg/"+ms.getUserId(),mm);
//                        }
//                    }
//                }
//
//
//            }
//        }else {
//            Message ms = new Message();
//            BeanUtils.copyProperties(mqMessage,ms);
//            BaseVo base = sysServiceFeign.add(ms);
//            String str = JSON.toJSONString(base.getData());
//            Message mm = JSON.parseObject(str,Message.class);
//            messagingTemplate.convertAndSend("/topic/msg/"+mqMessage.getUserId(),mm);
//        }

    }


}
