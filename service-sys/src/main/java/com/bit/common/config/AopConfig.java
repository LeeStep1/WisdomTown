package com.bit.common.config;

import com.bit.common.aop.GlobalApiAop;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置手动加载api aop 拦截
 * @author mifei
 */
@Configuration
public class AopConfig extends CachingConfigurerSupport {

    @Bean
    public GlobalApiAop getGlobalApiAop() {
        return new GlobalApiAop();
    }
}
