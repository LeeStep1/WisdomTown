package com.bit.common.config;

import com.bit.util.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:24
 */
@Configuration
public class MongoConfiguration {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Bean
    public MongoUtil mongoPageHelper() {
        return new MongoUtil(mongoTemplate);
    }
}
