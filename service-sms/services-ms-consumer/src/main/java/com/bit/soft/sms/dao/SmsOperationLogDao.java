package com.bit.soft.sms.dao;


import com.bit.soft.sms.bean.SmsSendLog;
import com.bit.soft.sms.config.MongodbBaseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @Description:  发送淑
 * @Author: liyujun
 * @Date: 2020-05-26
 **/
@Repository
public class SmsOperationLogDao extends MongodbBaseRepository<SmsSendLog,ObjectId > {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MongoTemplate setMongoTemplate() {
        return this.mongoTemplate;
    }
}
