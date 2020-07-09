package com.bit.soft.sms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;

import com.bit.soft.sms.bean.SmsLogPage;
import com.bit.soft.sms.bean.SmsSendLog;
import com.bit.soft.sms.common.Page;
import com.bit.soft.sms.dao.SmsOperationLogDao;
import com.bit.soft.sms.service.SmsLogService;
import lombok.extern.java.Log;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-05-22
 **/
@Service
@Log
public class SmsLogServiceImpl extends BaseService implements SmsLogService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SmsOperationLogDao smsOperationLogDao;

    /**
     * @param id :
     * @return : com.bit.base.vo.BaseVo<com.bit.sms.bean.SmsSendLog>
     * @description:
     * @author liyujun
     * @date 2020-05-26
     */
    @Override
    public BaseVo<SmsSendLog> findId(ObjectId id) {

        BaseVo rs = new BaseVo<>();
        SmsSendLog log = mongoTemplate.findById(id, SmsSendLog.class);
        rs.setData(log);
        return rs;
    }

    /**
     * @return : com.bit.base.vo.BaseVo<com.bit.sms.bean.SmsSendLog>
     * @description: 发送短信日志查询
     * @author liyujun
     * @date 2020-05-26
     */
    @Override
    public BaseVo<SmsSendLog> smsSendLogPage(SmsLogPage smsLogPage) {
        if(ObjectUtil.isNull(smsLogPage)){
            smsLogPage=new SmsLogPage();
        }
       /* List<SmsSendLog> rs=mongoTemplate.find(new Query(),SmsSendLog.class);
        long count= smsOperationLogDao.setMongoTemplate().count(new Query(),SmsSendLog.class);*/
        Query query = new Query();
        if (smsLogPage.getTerminalId() != null) {
            query.addCriteria(Criteria.where("terminalId").is(smsLogPage.getTerminalId()));
        }
        Page<SmsSendLog> page = smsOperationLogDao.tpQueryPageCount(query, smsLogPage.getPageNum(), smsLogPage.getPageSize());
        BaseVo vo = new BaseVo();
        vo.setData(page);
        return vo;
    }
}
