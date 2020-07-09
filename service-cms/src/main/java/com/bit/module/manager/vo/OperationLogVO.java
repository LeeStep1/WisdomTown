package com.bit.module.manager.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @description: 日志bean
 * @author: liyang
 * @date: 2019-05-07
 **/
@Data
public class OperationLogVO extends BasePageVo{

    /**
     * 内容ID
     */
    private Long contentId;

    /**
     * 操作方式
     */
    private String contentCode;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 关联栏目
     */
    private String categoryName;

    /**
     * 操作账号
     */
    private String operationAccountNumber;

    /**
     * 操作员名称
     */
    private String operationAccountUser;

    /**
     * 操作类型 创建资源  发布资源  删除资源  取消发布  创建并发布
     */
    private String operationType;

    /**
     * 操作IP
     */
    private String operationAddress;

    /**
     * 操作时间
     */
    private String operationTime;

    /**
     * 操作表名
     */
    private String tableName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
