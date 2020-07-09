package com.bit.common.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description:  居民申请业务的操作对比历史记录表
 * @Author: liyujun
 * @Date: 2019-09-18
 **/
@Data
public class ResidentApplyLog <T>{

    private String _id;

    private Date creatime;

    private Long operationUserId;

    private String operationUserName;

    private Long rosterId;

    private T obj;

    public ResidentApplyLog(){

    }

    public ResidentApplyLog(Date creatime,Long operationUserId, String operationUserName, Long rosterId,T obj) {
            this.creatime=creatime;
            this.operationUserId=operationUserId;
            this.operationUserName=operationUserName;
            this.rosterId=rosterId;
            this.setObj(obj);

    }
   /* public ResidentApplyLog(T t) {
        this.obj = t;
    }*/
}
