package com.bit.module.syslog.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.consts.ApplicationTypeEnum;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.module.syslog.bean.MessageTemplate;
import com.bit.module.syslog.bean.MessageTemplateRelTid;
import com.bit.module.syslog.feign.SysServiceFeign;
import com.bit.module.syslog.feign.VolServiceFeign;
import com.bit.module.syslog.vo.MessageTemplateVO;
import com.bit.soft.push.common.MqBaseConst;
import com.bit.soft.push.msEnum.AppPushTypeEnum;
import com.bit.soft.push.msEnum.MsgTypeEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.soft.push.payload.MqMessage;
import com.bit.soft.push.payload.MsMessage;
import com.bit.soft.push.payload.UserMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bit.soft.push.common.MqBaseConst.MQ_MESSAGES;

/**
 * @author liyujun
 * @description: 新版本对应的消费主要对MQmessage
 * @date 2020-04-08
 */
@Component
//@RabbitListener(queues = MQ_MESSAGES+"_lyj")
@RabbitListener(queues = MQ_MESSAGES)
@Slf4j
public class MessagesReceiverNewTwo {


    /**
     * mongo工具类
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * sys微服务
     */
    @Autowired
    private SysServiceFeign sysServiceFeign;

    /**
     * 志愿者微服务
     */
    @Autowired
    private VolServiceFeign volServiceFeign;

    /**
     * mq工具模板
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 监听消息，待办
     * 消息处理
     *
     * @param messageStr
     */
    @RabbitHandler
    public void process(Channel channel, String messageStr, Message message) {
        try {
            //告诉服务器已经收到消息 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("Topic_topic.messages Receiver  : {}", messageStr);
        } catch (Exception ex) {
            log.error("消费失败 {}", ex.getStackTrace());
        }
        MqMessage mqMessage = JSON.parseObject(messageStr, MqMessage.class);
        //根据模板类型获取模板详情
        MessageTemplateVO vo = sysServiceFeign.getTemplateCategory(mqMessage.getTemplateId());
        //进行处理具体的业务：组织转换人员，推送方式区分
        handleMessges(mqMessage, vo);

    }


    /**
     * @param mqMessage:
     * @param messageTemplateVO :
     * @return : void
     * @description: 处理消息
     * @author liyujun
     * @date 2020-04-09
     */
    private void handleMessges(MqMessage mqMessage, MessageTemplateVO messageTemplateVO) {

        List<Long> userIdList = new ArrayList();
        //发送消息实体
        MsMessage msMessage = new MsMessage();
        //拼装消息的类目和相关的标题
        assembleMsMessage(messageTemplateVO, mqMessage, msMessage);
        MessageTemplate tp = new MessageTemplate();
        BeanUtils.copyProperties(messageTemplateVO, tp);
        List<MessageTemplateRelTid> list = sysServiceFeign.getTemplateTidConfig(tp);
        List<String> tids = new ArrayList<String>();
        List<String> storeTids = new ArrayList<String>();
        list.stream().forEach(ii -> {
            if (ii.getStore().equals(0)) {
                storeTids.add(ii.getTid());
            }
            tids.add(ii.getTid());
        });
        //别名推送
        if (mqMessage.getAppJpushType() == AppPushTypeEnum.PUSHBYALIASE.getPushCode()) {
            log.info("别名推送");
            //别名推送方式下需要获取人员
            if (mqMessage.getTargetType().equals(TargetTypeEnum.ORG.getCode())) {
                log.info("组织");
                userIdList = userData(mqMessage, messageTemplateVO);

            } else {
                if (mqMessage.getTargetId()!=null && mqMessage.getTargetId().length>0){
                    log.info("用户");
                    //使用的是用户
                    userIdList = Arrays.asList(mqMessage.getTargetId());
                }
            }
            log.info("别名推送的初始化的目标用户{}", userIdList);
            //todo 存储的只能是别名推送，并且需要改造模板与接入端数据表，增加一列
            if (storeTids.size() > 0) {
                insertToMongo( messageTemplateVO, msMessage,  mqMessage,  userIdList,  tids);
            }
        } else/* if (mqMessage.getAppJpushType() == AppPushTypeEnum.PUSHBYTAG.getPushCode())*/ {
            //标签推送
            log.info("标签推送");
            mqMessage.setAppJpushType(AppPushTypeEnum.PUSHBYTAG.getPushCode());
        }
		log.info("模板拼接完毕..... 推送的人是....." + userIdList);
		sendToMs(messageTemplateVO,mqMessage,userIdList,tids,msMessage);
    }



    /**
     * @param mqMessage       :
     * @param messageTemplate :
     * @return : java.util.List<java.lang.Long>
     * @description:
     * @author liyujun
     * @date 2020-04-09
     */
    private List<Long> volUser(MqMessage mqMessage, MessageTemplateVO messageTemplate) {

        List<Long> userIdList = new ArrayList<>();
        // EXTERNALUSER(2,"外部用户"), INTERNALUSER(1,"内部用户"),
        //根据消息类型，获取具体人员 1、内部用户  2、外部用户
        log.info("志愿者组织的用户类型----{}",messageTemplate.getUserType());
        if (messageTemplate.getUserType().equals(TargetTypeEnum.EXTERNALUSER.getCode())) {
            //外部用戶
            userIdList = volServiceFeign.getAllVolUserIds();
        } else if (messageTemplate.getUserType().equals(TargetTypeEnum.INTERNALUSER.getCode())) {
            //内部用戶
            MessageTemplate tp = new MessageTemplate();
            tp.setUserType(messageTemplate.getUserType());
            tp.setOrgIds(messageTemplate.getOrgIds());
            BaseVo baseVo = sysServiceFeign.getAdminUserByStationOrgIds(tp);
            String userIds = JSON.toJSONString(baseVo.getData());
            userIdList = JSONArray.parseArray(userIds, Long.class);
            log.info("获得的志愿者用户{}",userIdList);
        } else {
            throw new BusinessException("志願者服務無法滿足此種用戶形式");
        }
        return userIdList;

    }

    /**
     * 政务组织消息处理,目前实现的是组织下推送的话，不区分内外用户。
     *
     * @param mqMessage         : 需要处理的消息
     * @param messageTemplateVO : 消息模板详情
     * @author liyujun
     * @date 2020-04-09
     */
    private List<Long> oaUser(MqMessage mqMessage, MessageTemplateVO messageTemplateVO) {

        List<Long> userIdList = new ArrayList<>();
        if (mqMessage.getTargetId() == null || mqMessage.getTargetId().length == 0) {
            userIdList = sysServiceFeign.getAllUserIdsForOaOrg();
        } else {
            BaseVo baseVo = sysServiceFeign.getAllUserIdsByOaOrgIds(mqMessage.getTargetId());
            String userIds = JSON.toJSONString(baseVo.getData());
            userIdList = JSONArray.parseArray(userIds, Long.class);
        }
        return userIdList;
    }

    /**
     * 党建组织消息处理,党建人员支持组织下的内部用户、外部用户、以及所有用户。
     *
     * @param mqMessage         : 需要处理的消息
     * @param messageTemplateVO : 消息模板详情
     * @author liyujun
     * @date 2020-04-09
     */
    private List<Long> pbUsers(MqMessage mqMessage, MessageTemplateVO messageTemplateVO) {

        BaseVo baseVo = null;
        List<Long> userIdList = new ArrayList<>();
        //
        if (!messageTemplateVO.getUserType().equals(TargetTypeEnum.ALL.getCode())) {
            //如果為内部或外部用戶時，查詢
            MessageTemplate tp = new MessageTemplate();
            tp.setOrgIds(messageTemplateVO.getOrgIds());
            tp.setUserType(messageTemplateVO.getUserType());
            baseVo = sysServiceFeign.getUserIdsByOrgIds(tp);

        } else if (messageTemplateVO.getUserType().equals(TargetTypeEnum.ALL.getCode())) {
            //如果是所有用户
            baseVo = sysServiceFeign.getAllUserIdsByPbOrgIds(mqMessage.getTargetId());

        }

        if (baseVo != null) {
            String userIds = JSON.toJSONString(baseVo.getData());
            userIdList = JSONArray.parseArray(userIds, Long.class);
        }

        return userIdList;
    }

    /**
     * 组装推送至相应的消息队列
     *
     * @param messageTemplate ：消息模板详情
     * @param mqMessage       ：mq队列内容
     * @param userIdList      ：userId用户集合
     * @param tidList         ：接入端集合
     * @author liyang
     * @date 2019-04-08
     */
    private void sendToMs(MessageTemplateVO messageTemplate, MqMessage mqMessage, List<Long> userIdList,  List<String> tidList, MsMessage msMessage) {
        //MsMessage msMessage = new MsMessage();
        //对应的业务表id
        //消息使用别名发送，不存在全体推送的情况  1为使用tag, 给所有用户推送 2 别名推送
        //增加推送方式的判断
        if (mqMessage.getAppJpushType() == AppPushTypeEnum.PUSHBYTAG.getPushCode()) {
            //标签推送
            msMessage.setTagType(AppPushTypeEnum.PUSHBYTAG.getPushCode());
            msMessage.setTagList(mqMessage.getTagList());

        } else {
            msMessage.setTagType(AppPushTypeEnum.PUSHBYALIASE.getPushCode());
            //用户集合
            if (userIdList.size() > 0) {
                Long[] userIds = new Long[userIdList.size()];
                msMessage.setUserId(userIdList.toArray(userIds));
            } else {
                throw new BusinessException("别名推送人员为空");
            }

        }
        if (!StringUtils.isEmpty(mqMessage.getPushTime())) {
            msMessage.setTime(mqMessage.getPushTime());
        }
        //先检测是否有推送至websocket
        if (tidList.contains(String.valueOf(TerminalTypeEnum.TERMINALTOWEB.getTid()))) {
            msMessage.setTid(String.valueOf(TerminalTypeEnum.TERMINALTOWEB.getTid()));
            String webSocketStr = JSON.toJSONString(msMessage);
            //推送至websocket
            rabbitTemplate.convertAndSend(MqBaseConst.MS_WEB_EXCHANGE, MqBaseConst.MS_MESSAGETOWEB, webSocketStr);

        }
        //再检测是否有推送至其余APP的
        if (TerminalTypeEnum.containApp(tidList)) {
            //推送至APP
            //获取tid 推送tid 不等于1 的所有APP
            String TERMINALTOWEB = String.valueOf(TerminalTypeEnum.TERMINALTOWEB.getTid());
            for (String tid : tidList) {
                if (!tid.equals(TERMINALTOWEB)) {
                    msMessage.setTid(tid);
                    //APP地址
                    msMessage.setAppUrl(messageTemplate.getAppUrl());
                    //是否需要跳转
                    msMessage.setJumpFlag(messageTemplate.getJumpFlag());

                    //获取title
                   // msMessage.setTitle(templateMap.get("title"));
                    String jPushStr = JSON.toJSONString(msMessage);
                    log.info("发送消息到推送服务{}", jPushStr);
                    //推送至极光
                    // rabbitTemplate.convertAndSend(MS_JPUSH_EXCHANGE+"_lyj",MS_MESSAGETOJPUSH+"_lyj",jPushStr);
                    rabbitTemplate.convertAndSend(MqBaseConst.MS_JPUSH_EXCHANGE, MqBaseConst.MS_MESSAGETOJPUSH, jPushStr);
                }
            }
        }
    }

    /**
     * 将消息根据人员和接入端插入mongo
     *
     * @param messageTemplate : 消息模板详情
     * @param mqMessage       :  mq消息详情
     * @param userIdList      : 发送人详情
     * @param tidList         : 接入端集合
     * @author liyang
     * @date 2019-04-08
     */
    private void insertToMongo(MessageTemplateVO messageTemplate,MsMessage msMessage, MqMessage mqMessage, List<Long> userIdList, List<String> tidList){
        List<UserMessage> userMessagesList = new ArrayList<>();

        //拼接mongo 先循环userId
        for (int i = 0; i < userIdList.size(); i++) {

            //再循环接入端创建接入端
            for (String tid : tidList) {
                UserMessage um = new UserMessage();
                //appId
                um.setAppid(msMessage.getAppId().intValue());
                //业务表ID
                if (!StringUtils.isEmpty(mqMessage.getBusinessId())) {
                    um.setBusinessId(mqMessage.getBusinessId());
                }
                //消息内容
                um.setContent(msMessage.getContent());
                //是否已读 0 未读  1 已读
                //um.setStatus(UNREAD.getCode());
                um.setStatus(MqBaseConst.MONGO_MESSAGE_STATUS_NOT_READED);

                //userId
                um.setUserId(userIdList.get(i));
                //接入端
                um.setTid(tid);
                //创建人
                um.setCreater(mqMessage.getCreater());
                //消息类型 1消息 2待办 3通知 4公告 5已办
                um.setMsgType(msMessage.getMsgType());
                //消息类型名称
                um.setMsgTypeName(msMessage.getMsgTypeName());
                //消息创建时间
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                um.setRecTime(dateString);
                //类目ID
                um.setCategoryCode(msMessage.getCategoryCode());
               // um.setCategoryCode(messageTemplate.getCategory());
                //类目名称
                um.setCategoryName(msMessage.getCategoryName());
                //锁
                um.setVersion(mqMessage.getVersion());
                //一级菜单
                um.setLevelOneMenu(messageTemplate.getLevelOneMenu());

                um.setCategoryId(String.valueOf(messageTemplate.getCategoryId()));
                userMessagesList.add(um);
            }
        }
        //存入mongo
        //mongoTemplate.insert(userMessagesList,MONGO_MESSAGE_COLLECTION+"1");
        mongoTemplate.insert(userMessagesList, MqBaseConst.MONGO_MESSAGE_COLLECTION);
        log.info("存入mongo数据是：" + userMessagesList);
    }

    /**
     * @return : void
     * @description: 拼装消息模板
     * @author liyujun
     * @date 2020-04-08
     */
    private void assembleMsMessage(MessageTemplateVO messageTemplate, MqMessage mqMessage, MsMessage msMessage) {

        if (mqMessage.getParams().length > 0) {
            msMessage.setContent(MessageFormat.format(messageTemplate.getContext(), mqMessage.getParams()));
        } else {
            msMessage.setContent(messageTemplate.getContext());
        }
        msMessage.setAppId(messageTemplate.getAppId().longValue());
        //msMessage.setCategoryCode(String.valueOf(messageTemplate.getCategoryId()));
        msMessage.setCategoryCode(String.valueOf(messageTemplate.getCategory()));
        msMessage.setMsgType(messageTemplate.getMsgType());
        msMessage.setCategoryName(messageTemplate.getCategoryName());
        msMessage.setCategoryId(String.valueOf(messageTemplate.getCategoryId()));


        if (StringUtils.isEmpty(messageTemplate.getMsgTypeName())){
            msMessage.setMsgTypeName(MsgTypeEnum.getMsgType(messageTemplate.getMsgType()).getInfo());
            msMessage.setTitle(MsgTypeEnum.getMsgType(messageTemplate.getMsgType()).getInfo() + "(" + messageTemplate.getCategoryName() + ")");
		}else {
			msMessage.setMsgTypeName(messageTemplate.getMsgTypeName());
			msMessage.setTitle(messageTemplate.getMsgType() + "(" + messageTemplate.getCategoryName() + ")");
		}
        msMessage.setJumpFlag(messageTemplate.getJumpFlag());
    }

    /**
     * @param mqMessage         :
     * @param messageTemplateVO :
     * @return : java.util.List<java.lang.Long>
     * @description: 推送时是组织时，来组织人员数据
     * @author liyujun
     * @date 2020-04-09
     */
    private List<Long> userData(MqMessage mqMessage, MessageTemplateVO messageTemplateVO) {

        List<Long> userIdList = new ArrayList<>();
        Integer appId = messageTemplateVO.getAppId();

        mqMessage.setAppId(appId.longValue());
        //组织插入
        messageTemplateVO.setOrgIds(Arrays.asList(mqMessage.getTargetId()));
        log.info("appid{}",appId);
        log.info("组织id{}",mqMessage.getTargetId());
        if (appId.equals(ApplicationTypeEnum.APPLICATION_PB.getApplicationId())) {
            log.info("APPLICATION_PB");
            userIdList = pbUsers(mqMessage, messageTemplateVO);
        } else if (appId.equals(ApplicationTypeEnum.APPLICATION_OA.getApplicationId())) {
            log.info("APPLICATION_OA");
            userIdList = oaUser(mqMessage, messageTemplateVO);
        } else if (appId.equals(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId())) {
             log.info("志愿者");
            //志愿者应用
            userIdList = volUser(mqMessage, messageTemplateVO);
        }

        return userIdList;
    }

}
