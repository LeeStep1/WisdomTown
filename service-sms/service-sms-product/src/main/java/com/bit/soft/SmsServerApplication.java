package com.bit.soft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: 消息服务
 * @Author: liyujun
 * @Date: 2019-01-25
 **/
@EnableCaching
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class},scanBasePackages = "com.bit.*")
@EnableDiscoveryClient
@EnableEurekaClient
@EnableTransactionManagement
@ServletComponentScan
public class SmsServerApplication {

    public static void main( String[] args )
    {
        SpringApplication.run(SmsServerApplication.class, args);
    }

}
