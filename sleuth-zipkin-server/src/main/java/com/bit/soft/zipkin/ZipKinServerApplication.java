package com.bit.soft.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;


/**
 * 服务调用的跟踪
 *
 */
@SpringBootApplication
@EnableZipkinServer
@EnableEurekaClient
public class ZipKinServerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ZipKinServerApplication.class, args);
    }
}
