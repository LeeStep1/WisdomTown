package com.bit.ms.service;

import cn.jpush.api.push.PushResult;
import com.alibaba.fastjson.JSON;
import com.bit.base.exception.BusinessException;

import com.bit.ms.bean.Jpush;
import com.bit.ms.dao.JpushDao;
import com.bit.ms.util.JPushUtil;
import com.bit.soft.push.msEnum.AppPushTypeEnum;
import com.bit.soft.push.msEnum.MessageTemplatePropertyEnum;
import com.bit.soft.push.payload.MsMessage;
import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bit.common.MsConstEnum.*;
import static com.bit.soft.push.common.MqBaseConst.MS_MESSAGETOJPUSH;


/**
 * @Description: 消息接收并通过极光推送到APP端
 * @Author: Liyang
 * @Date: 2019-04-03
 **/
@Component
//@RabbitListener(queues = MS_MESSAGETOJPUSH+"_lyj")
@RabbitListener(queues = MS_MESSAGETOJPUSH)
@Slf4j
public class MessageToJpushReceiverNew {

    /**
     * 极光推送工具类
     */
    @Autowired
    private JPushUtil jPushUtil;

    @Value("${jpush.tag}")
    private String allTag;

    /**
     * 是否启用mongo日志
     */
    @Value("${jpush.mongoLog}")
    private String mongoLog;

    @Autowired
    private JpushDao jpushDao;

    /**
     * mongo工具类
     */
    @Autowired
    private MongoTemplate mongoTemplate;


    private static final Logger logger = LoggerFactory.getLogger(MessageToJpushReceiverNew.class);

    /***
     * 推送消息至APP
     * @author liyang
     * @date 2019-04-03
     * @param messageStr : 消息
     * @return : void
     */
    @RabbitHandler
    public void process(Channel channel, String messageStr, Message message) {
        logger.info("MS ....Topic Receiver  : {}",messageStr);

        try {
            //告诉服务器已经收到消息 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception ex){
            throw new BusinessException("消息确认失败");
        }
        MsMessage msMessage = JSON.parseObject(messageStr,MsMessage.class);
        Map<String,MsMessage> msMap = new HashMap<>(10);
        //推送时扩展的业务字段，以key,value形式存在，比如IOS设置通知铃声和badge
        ImmutableMap.Builder<String,String> extras = ImmutableMap.builder();

        //拼装bussiness
        if(!StringUtils.isEmpty(msMessage.getBusinessId())){
            extras.put(BUSSINESSID.getInfo(), String.valueOf(msMessage.getBusinessId()));
        }

        Jpush jpush = jpushDao.findByTid(Integer.parseInt(msMessage.getTid()));

        //是否需要跳转 0、需要  1、不需要
        if(msMessage.getJumpFlag() != null){

            if (msMessage.getJumpFlag().equals( MessageTemplatePropertyEnum.JUMPSTATUS.getProperty()) ){

                //APPurl
                extras.put(APPURL.getInfo(), msMessage.getAppUrl());

                //是否需要跳转0、需要  1、不需要
                extras.put(JUMPFLAG.getInfo(), String.valueOf(msMessage.getJumpFlag()));
            }
        }

        //通过tagType判断是标签推送还是别名推送 1 标签推送 2 别名推送
        //
        if(msMessage.getTagType().equals(AppPushTypeEnum.PUSHBYTAG.getPushCode())){
            List<String> tagList = new ArrayList<>();
            //标签构成  tid_allTag
            if(!CollectionUtils.isEmpty(msMessage.getTagList())){
                tagList=msMessage.getTagList();
            }else{
                tagList.add(allTag + "_" + msMessage.getTid());
            }

            try{
                //根据标签推送 党建APP
                String title="";
                String content="";
                if(tagList.size()>0){
                    title=msMessage.getTitle();
                    content=msMessage.getContent();
                }/*else{
                    //对于默认的
                    title=msMessage.getMsgTypeName();
                    content=msMessage.getTitle();
                }*/
                PushResult pushResult = jPushUtil.pushNotificationByTags(title,content,tagList,extras.build(),jpush.getAppKey(),jpush.getMasterSecret());

                if(!pushResult.isResultOK()){
                    log.info("标签推送失败，内容为：{}",JSON.toJSONString(msMessage));
                }

            }catch (Exception ex){
                log.info("标签推送异常{}",ex.getMessage());
                throw new BusinessException("推送失败");
            }


        }else {
            //1、成功  0、失败
            int result = 0;
            //如果是别名推送,先组成别名
            List<String> aliaseList = new ArrayList<>( msMessage.getUserId().length);
            for (Long userId : msMessage.getUserId()){
                //别名格式 userId+Tid
                aliaseList.add(msMessage.getTid()  + "_" +  userId);
            }

            try {
                //判断应用类型推送至不同APP
                result = jPushUtil.pushNotificationByAliases(msMessage.getTitle(),msMessage.getContent(),aliaseList,extras.build(),jpush.getAppKey(),jpush.getMasterSecret(),msMessage.getTime());
                if(result==1){
                    log.info("别名推送成功，内容为：{}",JSON.toJSONString(msMessage));
                }else{
                    log.info("别名推送失败，内容为：{}",JSON.toJSONString(msMessage));
                }

            }catch (Exception ex){
                throw new BusinessException("推送失败");

            }

        }

    }


}
