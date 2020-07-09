package com.bit.module.applylogs.repository;

import com.bit.module.applylogs.bean.ApplyLogs;

import com.bit.soft.push.payload.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @Description: 申请日志记录组件
 * @Author: liyujun
 * @Date: 2019-03-19
 **/
@Repository
public class ApplyRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * @description:  审批操作日志
     * @author liyujun
     * @date 2019-03-19
     * @param logs :
     * @return : void
     */
    public void addApplyLogs(ApplyLogs logs){

        logs.setCreateTime(new Date());
        mongoTemplate.insert(logs,"applyLogs");
    }


    /**
     * @description:  查询审批操作日志
     * @author liyujun
     * @date 2019-03-19
     * @param  :appId 应用id
     * @param  :formId 业务表单id
     * @param  : serviceid 业务id
     * @return : List<ApplyLogs>
     */
    public List<ApplyLogs> queryApplyLogsList(Integer terminalId, Integer serviceId, Long formId){
        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"operationTime")));
        Criteria c= Criteria.where("terminalId").is(terminalId);
        c.andOperator(
                Criteria.where("formId").is(formId), Criteria.where("serviceId").is(serviceId)
        );
        query.addCriteria(c);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"operationTime")));
        List<ApplyLogs> list= mongoTemplate.find(query,ApplyLogs.class,"applyLogs");
        return list;
    }

    /**
     * 更新已读未读
     * @param status
     * @param id
     */
    public void updateStatus(Integer status,String id){
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("status", status);
        mongoTemplate.updateFirst(query, update, UserMessage.class,"message");

    }
    /**
     *
     * 功能描述:
     * @author: chenduo
     * @description: 根据条件查询结果
     * @param: businessId,categoryCode,msgType
     * @return: list
     * @date: 2019-04-11 8:32
     */

    public List<UserMessage> queryParam(Long businessId,String categoryCode,Integer msgType){
        Query query = new Query();
        Criteria criteria = Criteria.where("businessId").is(businessId).and("categoryCode").is(categoryCode).and("msgType").is(msgType);
        query.addCriteria(criteria);
        List<UserMessage> list= mongoTemplate.find(query,UserMessage.class,"message");
        return list;
    }
    /**
     *
     * 功能描述:
     * @author: chenduo
     * @description: 更新mongo的msgtype
     * @param: userMessages,msgType
     * @return:
     * @date: 2019-04-11 8:40
     */

    public void updateMsgTypeAndVersion(List<UserMessage> userMessages,Integer msgType,Integer version){
        for (UserMessage userMessage : userMessages) {
            Query query = Query.query(Criteria.where("_id").is(userMessage.get_id()));
            Update update = Update.update("msgType", msgType).set("version",version+1);
            mongoTemplate.updateFirst(query, update, UserMessage.class,"message");
        }
    }
}
