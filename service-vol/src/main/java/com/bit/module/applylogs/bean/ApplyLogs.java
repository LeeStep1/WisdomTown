package com.bit.module.applylogs.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description:  日志实体类，用于存储审核业务的日志
 * @Author: liyujun
 * @Date: 2019-03-19
 **/
@Data
public class ApplyLogs {

    /**应用id**/
    private   Integer terminalId;

    /**业务的ID**/
    private  Integer serviceId ;

    /**业务的审批数据的id**/
    private  Long formId;

    /**操作人名称**/
    private String operUserName;

    /**操作人ID**/
    private Long operUserId;

    /**操作内容**/
    private String operationContent;

    /**操作code**/
    private Integer operationCode;

    /**操作名称**/
    private String operationName;

    /**操作时间**/
    private String operationTime;

    /**数据记录时间时间**/
    private Date createTime;

    /**适配业务的扩展字段**/
    private String param;

    public ApplyLogs (Integer terminalId,
                      Integer serviceId,
                      Long formId,
                      Long operUserId,
                      String operUserName,
                      String operationName,
                      Date createTime,
                      Integer operationCode,
                      String operationTime,
                      String operationContent,
                      String param){

        this.terminalId=terminalId;
        this.serviceId=serviceId;
        this.formId=formId;
        this.serviceId=serviceId;
        this.operUserName=operUserName;
        this.operationName=operationName;
        this.createTime=createTime;
        this.operationCode=operationCode;
        this.operationTime=operationTime;
        this.operationContent=operationContent;
        this.param=param;
        this.operUserId=operUserId;

    }

    public ApplyLogs() {
    }
}
