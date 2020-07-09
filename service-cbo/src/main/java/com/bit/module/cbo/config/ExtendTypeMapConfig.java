package com.bit.module.cbo.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description:
 * @author: liyang
 * @date: 2020-06-09
 **/
@Configuration
@ConfigurationProperties(prefix = "extend-type")
@Data
public class ExtendTypeMapConfig {

    public Map<Integer,String> type;


}
