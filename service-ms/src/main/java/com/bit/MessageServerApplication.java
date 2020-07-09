package com.bit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: 消息服务
 * @Author: liyujun
 * @Date: 2019-01-25
 **/
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.bit")
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@ServletComponentScan
public class MessageServerApplication {

    public static void main( String[] args )
    {
        SpringApplication.run(MessageServerApplication.class, args);
    }

}
