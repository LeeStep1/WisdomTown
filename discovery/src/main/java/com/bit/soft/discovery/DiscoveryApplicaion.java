package com.bit.soft.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by lyj
 */

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class})
@EnableEurekaServer
public class DiscoveryApplicaion {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplicaion.class, args);
    }
}
