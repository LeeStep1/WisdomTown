package com.bit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Desc
 *
 * @author mifei
 * @date 2019-07-15
 */
@SpringBootApplication(scanBasePackages = "com.bit")
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.bit.module.*.dao")
@ServletComponentScan
@EnableAsync
@EnableHystrix
public class ServiceCboApplication {
    public static void main(String[] args) {
        
        SpringApplication.run(ServiceCboApplication.class, args);
    }
}