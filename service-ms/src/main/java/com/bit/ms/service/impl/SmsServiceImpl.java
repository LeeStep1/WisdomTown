package com.bit.ms.service.impl;

import com.bit.base.service.BaseService;
import com.bit.ms.util.SmsSenderComponent;
import com.bit.ms.bean.SmsRequest;
import com.bit.ms.bean.SmsSendLog;
import com.bit.ms.service.SmsService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * @Description:  短信发送服务
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@Service
public class SmsServiceImpl  extends BaseService implements SmsService{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SmsSenderComponent smsSenderComponent;

    /**
     * @description:  内部调用的发送短信服务
     * @author liyujun
     * @date 2019-03-20
     * @param smsRequest :
     * @return : void
     */
    @Override
    public void sendSms(SmsRequest smsRequest) {



        if(smsRequest.getPhoneNumbers().length==1){
            smsSenderComponent.sendSmsSingle(smsRequest.getTemplateCode(),smsRequest.getPhoneNumbers()[0],smsRequest.getParam());
         }else if(smsRequest.getPhoneNumbers().length>1){
            smsSenderComponent.sendMultiSender(smsRequest.getTemplateCode(),smsRequest.getPhoneNumbers(),smsRequest.getParam());

        }
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        SmsSendLog log=mapper.map(smsRequest, SmsSendLog.class);
        log.setSendTime(cn.hutool.core.date.DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        mongoTemplate.insert(log,"smsLogs");
    }
}
