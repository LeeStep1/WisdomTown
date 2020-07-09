package com.bit.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;

/**
 * @Description:  文件服务
 * @Author: liyujun
 * @Date: 2018-10-11
 **/
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.bit.file.dao")
@EnableAsync
@SpringBootApplication
public class FileServiceApplication {

    @Value("${fastdfs.maxFileSize}")
    private String maxFileSize;

    @Value("${fastdfs.maxRequestSize}")
    private String maxRequestSize;

    public static void main( String[] args )
    {
        SpringApplication.run(FileServiceApplication.class, args);
    }

    /**
     * 设置FastDFS的文件上传大小限制值
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(this.maxFileSize);
        factory.setMaxRequestSize(this.maxRequestSize);

        return factory.createMultipartConfig();
    }
}
