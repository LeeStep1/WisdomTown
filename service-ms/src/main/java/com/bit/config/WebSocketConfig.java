package com.bit.config;

import com.bit.ms.feign.SysServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @Description: 配置类，包括推送的单点
 * @Author: liyujun
 * @Date: 2019-01-25
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private SysServiceFeign sysServiceFeign;


    /**
     * @param config :
     * @return : void
     * @description: 配置
     * @author liyujun
     * @date 2019-01-25
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //客户端只可以订阅这两个前缀的主题
        config.enableSimpleBroker("/topic", "/msg");

    }


    /**
     * @param registry :
     * @return : null
     * @description: 配置的接入端
     * @author liyujun
     * @date 2019-01-25
     */
    @Override
    public void
    registerStompEndpoints(StompEndpointRegistry registry) {

        //路径"/webServer"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务,并可以限制接入端的地址
        registry.addEndpoint("/webServer").setAllowedOrigins("*").withSockJS();
    }

    /**
     * @param registration :
     * @return : void
     * @description: 增加客户端验证
     * @author liyujun
     * @date 2019-01-25
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //判断是否首次连接请求
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    //todo 后期加验证逻辑
                    String token = accessor.getNativeHeader("token").get(0);
                    String tid = accessor.getNativeHeader("tid").get(0);
                    //调用sys验证token
                    Boolean flag = sysServiceFeign.verifyToken(token,Integer.parseInt(tid));
                    if (flag) {

                        return message;
                    }

                    return null;
                }
                //不是首次连接，已经成功登陆
                return message;
            }
        });
    }
}


