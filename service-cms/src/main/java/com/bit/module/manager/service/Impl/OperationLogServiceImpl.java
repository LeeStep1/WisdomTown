package com.bit.module.manager.service.Impl;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.*;
import com.bit.module.manager.dao.*;
import com.bit.module.manager.service.OperationLogService;
import com.bit.module.manager.vo.OperationLogPageVo;
import com.bit.module.manager.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.bit.common.Const.OPERATIONLOGTABLE;

/**
 * @description: 常用日志
 * @author: liyang
 * @date: 2019-05-16
 **/
@Service
public class OperationLogServiceImpl implements OperationLogService {

    /**
     * mongo 工具类
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 服务类型数据库管理
     */
    @Autowired
    private ServiceTypeDao serviceTypeDao;

    /**
     * 领导班子头像数据库管理
     */
    @Autowired
    private PortalPbLeaderDao portalPbLeaderDao;

    /**
     * 领导介绍数据库管理
     */
    @Autowired
    private PortalOaLeaderDao portalOaLeaderDao;

    /**
     * 党建组织模板数据库管理
     */
    @Autowired
    private ProtalPbOrgDao protalPbOrgDao;

    /**
     * 内容表数据库管理
     */
    @Autowired
    private PortalContentDao portalContentDao;

    /**
     * 根据条件查询日志
     * @author liyang
     * @date 2019-05-16
     * @param operationLogVO : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo getOperationLogList(OperationLogVO operationLogVO) {

        Sort sort = Sort.by(Sort.Direction.DESC, "operationTime");
        Pageable pageable = PageRequest.of(operationLogVO.getPageNum()-1, operationLogVO.getPageSize(), sort);

        Query query = new Query();

        //账号模糊匹配
        if(operationLogVO.getOperationAccountNumber() != null && !("").equals(operationLogVO.getOperationAccountNumber()) ){
            Pattern pattern=Pattern.compile("^.*"+operationLogVO.getOperationAccountNumber()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("operationAccountNumber").regex(pattern));
        }

        //操作员模糊匹配
        if(operationLogVO.getOperationAccountUser() != null && !("").equals(operationLogVO.getOperationAccountUser()) ){
            Pattern pattern=Pattern.compile("^.*"+operationLogVO.getOperationAccountUser()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("operationAccountUser").regex(pattern));
        }

        //操作类型匹配
        if(operationLogVO.getContentCode() != null && !("").equals(operationLogVO.getContentCode()) ){
            query.addCriteria(Criteria.where("contentCode").is(operationLogVO.getContentCode()));
        }

        //操作时间范围
        if(operationLogVO.getStartTime() != null && !("").equals(operationLogVO.getStartTime()) ){
            query.addCriteria(Criteria.where("operationTime").gte(operationLogVO.getStartTime()).lt(operationLogVO.getEndTime()));
        }

        //计算总数
        long total = mongoTemplate.count(query, OperationLog.class,OPERATIONLOGTABLE);

        //查询结果集
        List<OperationLog> operationLogList = mongoTemplate.find(query.with(pageable), OperationLog.class,OPERATIONLOGTABLE);

        //返回分页
        OperationLogPageVo operationLogPage = new OperationLogPageVo();
        operationLogPage.setOperationLogList(operationLogList);
        operationLogPage.setTotal(total);
        operationLogPage.setPageNum(operationLogVO.getPageNum());
        operationLogPage.setPageSize(operationLogVO.getPageSize());

        BaseVo baseVo = new BaseVo();
        baseVo.setData(operationLogPage);

        return baseVo;
    }

    /**
     * 在日志表中查询操作日志内容明细
     * @author liyang
     * @date 2019-05-17
     * @param contendId : 内容ID
     * @param tableNameId : 查询的表ID
     * @return : BaseVo
     */
    @Override
    public BaseVo getOperationContent(Long contendId, Integer tableNameId) {
        BaseVo baseVo = new BaseVo();
        //获取表名
        switch (tableNameId){

            //服务类型内容表
            case 1:
                ServiceType serviceType = serviceTypeDao.findById(contendId);
                baseVo.setData(serviceType);
                break;

            //领导班子头像表
            case 2:
                PortalPbLeader portalPbLeader = portalPbLeaderDao.findById(contendId);
                baseVo.setData(portalPbLeader);
                break;

            //领导介绍表
            case 3:
                PortalOaLeader portalOaLeader = portalOaLeaderDao.findById(contendId);
                baseVo.setData(portalOaLeader);
                break;

            //党建组织模板
            case 4:
                ProtalPbOrg protalPbOrg = protalPbOrgDao.findById(contendId);
                baseVo.setData(protalPbOrg);
                break;

            //普通内容表
            case 5:
                PortalContent portalContent = portalContentDao.findById(contendId);
                baseVo.setData(portalContent);
                break;
        }

        return baseVo;
    }
}
