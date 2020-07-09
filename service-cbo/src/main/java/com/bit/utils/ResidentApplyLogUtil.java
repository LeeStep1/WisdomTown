package com.bit.utils;

import com.bit.common.bean.ResidentApplyLog;
import com.bit.common.enumerate.ResidentApplyTypeEumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * @Description:  用于记录各个业务的日志版本
 * @Author: liyujun
 * @Date: 2019-09-18
 **/
@Component
public class ResidentApplyLogUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description:
     * @author liyujun
     * @date 2019-09-18
     * @param typeEumn :
     * @param residentApplyLog :
     * @return : void
     */
    public void addApplyLog(ResidentApplyTypeEumn typeEumn,ResidentApplyLog residentApplyLog){
        mongoTemplate.insert(residentApplyLog,typeEumn.getTable());
    }
    /**
     * @description:  查询日志，根据人员名单id
     * @author liyujun
     * @date 2019-09-18
     * @param typeEumn :
     * @param rosterId :
     * @return : List<ResidentApplyLog>
     */
    public List<ResidentApplyLog>  getLogs(ResidentApplyTypeEumn typeEumn,Long rosterId,Class target){

        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"creatime")));
        Criteria c= Criteria.where("rosterId").is(rosterId);
        query.addCriteria(c);

        List<ResidentApplyLog> list= mongoTemplate.find(query, target,typeEumn.getTable());
        return list;
    }



}
