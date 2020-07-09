package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.vo.OperationLogVO;

/**
 * @description: 常用日志Service
 * @author: liyang
 * @date: 2019-05-16
 **/
public interface OperationLogService {

    /**
     * 根据条件查询日志
     * @author liyang
     * @date 2019-05-16
     * @param operationLogVO : 查询条件
     * @return : BaseVo
    */
    BaseVo getOperationLogList(OperationLogVO operationLogVO);

    /**
     * 在日志表中查询操作日志内容明细
     * @author liyang
     * @date 2019-05-17
     * @param contendId : 内容ID
     * @param tableNameId : 查询的表ID
     * @return : BaseVo
     */
    BaseVo getOperationContent(Long contendId,Integer tableNameId);
}
