package com.bit.common.config;
import com.bit.soft.push.utils.SendMqPushUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:  推送工具类配置
 * @Author: liyujun
 * @Date: 2019-09-04
 **/
@Configuration
public class PushConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    
    /**
     * @description:  初始化发送推送工具类
     * @author liyujun
     * @date 2020-05-12
     * @return : com.bit.module.mqCore.MqUtil.SendMqPushUtil
     */
    @Bean(name = "sendMqPushUtil")
    public SendMqPushUtil initSendMqPushUtil(){

        return  new SendMqPushUtil( rabbitTemplate);
    }
}
