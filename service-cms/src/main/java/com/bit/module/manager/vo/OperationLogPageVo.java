package com.bit.module.manager.vo;

import com.bit.module.manager.bean.OperationLog;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: liyang
 * @date: 2019-05-17
 **/
@Data
public class OperationLogPageVo {

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 每页几条
     */
    private Integer pageSize;

    /**
     * 内容展示
     */
    private List<OperationLog> operationLogList;
}
