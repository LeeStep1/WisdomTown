package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.service.OperationLogService;
import com.bit.module.manager.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 常用日志controller
 * @author: liyang
 * @date: 2019-05-16
 **/
@RestController
@RequestMapping(value = "/manager/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 根据条件查询日志
     * @author liyang
     * @date 2019-05-16
     * @param operationLogVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/operationLogs")
    public BaseVo operationLogs(@RequestBody OperationLogVO operationLogVO){
        return operationLogService.getOperationLogList(operationLogVO);
    }

    /**
     * 在日志表中查询操作日志内容明细
     * @author liyang
     * @date 2019-05-17
     * @param contendId : 内容ID
     * @param tableNameId : 查询的表ID
     * @return : BaseVo
     */
    @GetMapping("/operationLogs/{contendId}/{tableNameId}")
    public BaseVo operationContent(@PathVariable("contendId") Long contendId,
                                   @PathVariable("tableNameId") Integer tableNameId){
        return operationLogService.getOperationContent(contendId,tableNameId);
    }


}
