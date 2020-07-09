package com.bit.soft.push.common;

/**
 * @Description:  MQ静态类
 * @Author: Liyang
 **/
public class MqBaseConst {

    /**
     * mq日志队列
     */
    public static final String MQ_MESSAGE = "topic.syslog";

    /**
     * mq消息队列
     */
    public static final String MQ_MESSAGES = "topic.messages";

    /**
     * mq通知公告队列
     */
    public static final String MQ_MESSAGETONOCTICE = "topic.notice";

    /**
     * mq延迟队列
     */
    public static final String MQ_DELAY = "topic.delay";

    /**
     * mq日志队列交换机
     */
    public static final String MQ_SYSLOG_EXCHANGE = "topicExchange";

    /**
     * mq消息队列交换机
     */
    public static final String MQ_MESSAGES_EXCHANGE = "topicExchangeToMessages";

    /**
     * mq通知公告队列交换机
     */
    public static final String MQ_NOTICE_EXCHANGE = "topicExchangeToMessageToNotice";
    /**
     * mq延迟队列交换机
     */
    public static final String MQ_DELAY_EXCHANGE = "topicExchangeToDelay";


    /**
     * websocket推送队列
     */
    public static final String MS_MESSAGETOWEB = "topic.messageToWeb";

    /**
     * 极光推送队列
     */
    public static final String MS_MESSAGETOJPUSH = "topic.messageToJpush";

    /**
     * MS websocket 交换机
     */
    public static final String MS_WEB_EXCHANGE = "topicExchangeToWeb";

    /**
     * MS JPSUH 交换机
     */
    public static final String MS_JPUSH_EXCHANGE ="topicExchangeToJpush";
    /**
     * mongo的message表 消息状态 0未读 1已读
     */
    public static Integer MONGO_MESSAGE_STATUS_READED = 1;

    public static Integer MONGO_MESSAGE_STATUS_NOT_READED = 0;

    /**
     * 消息类mongo 表名
     */
    public static final String MONGO_MESSAGE_COLLECTION ="message";
}
