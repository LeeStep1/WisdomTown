package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.FlowDao;
import com.bit.module.system.service.FlowService;
import com.bit.module.system.vo.BusinessRoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("flowService")
public class FlowServiceImpl extends BaseService implements FlowService {

    @Autowired
    private FlowDao flowDao;
    @Override
    public BaseVo add(BusinessRole businessRole) {
        return null;
    }
    /**
     * 更新记录
     * @param businessRole
     * @return
     */
    @Override
    @Transactional
    public BaseVo update(BusinessRole businessRole) {
        BaseVo baseVo = new BaseVo();
        if (businessRole.getBusinessRelRoleList()!=null || businessRole.getBusinessRelRoleList().size()>0){
            //先查询是否存在这个工作流
            List<BusinessRole> businessRoles = flowDao.queryByFlowId(businessRole.getFlowId());
            if (businessRoles==null || businessRoles.size()<=0){
                baseVo.setMsg(ResultCode.PARAMETER_ERROR.getInfo());
                baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
                return baseVo;
            }
            //根据工作流批量删除
            flowDao.batchDeleteByFlowId(businessRole.getFlowId());
            List<BusinessRole> bs = new ArrayList<>();
            //批量新增
            for (BusinessRelRole role : businessRole.getBusinessRelRoleList()) {
                BusinessRole br = new BusinessRole();
                BeanUtils.copyProperties(businessRoles.get(0),br);
                br.setRoleId(role.getRoleId());
                bs.add(br);
            }
            flowDao.batchAdd(bs);
        }

        return successVo();
    }
    /**
     * 返显记录
     * @param flowId
     * @return
     */
    @Override
    public BaseVo reflectByFlowId(Integer flowId) {

        BusinessRolePage businessRolePage = new BusinessRolePage();
        //先查出基本数据
        List<BusinessRole> businessRoles = flowDao.batchSelectByFlowId(flowId);
        if (businessRoles==null || businessRoles.size()<=0){
            return new BaseVo();
        }
        BeanUtils.copyProperties(businessRoles.get(0),businessRolePage);
        //批量查询角色名称
        List<BusinessRelRole> businessRelRoles = flowDao.queryRoleNamesByFlowId(businessRoles.get(0).getFlowId());
        businessRolePage.setBusinessRelRoleList(businessRelRoles);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(businessRolePage);
        return baseVo;
    }
    /**
     * 分页查询
     * @param businessRoleVO
     * @return
     */
    @Override
    public BaseVo listPage(BusinessRoleVO businessRoleVO) {
        PageHelper.startPage(businessRoleVO.getPageNum(),businessRoleVO.getPageSize());
        List<BusinessRolePage> businessRolePageList = new ArrayList<>();
        //先查出基本数据
        List<BusinessRole> businessRoles = flowDao.listPage(businessRoleVO);
        //遍历查询角色名称
        for (BusinessRole businessRole : businessRoles) {
            List<BusinessRelRole> businessRelRoles = flowDao.queryRoleNamesByFlowId(businessRole.getFlowId());
            BusinessRolePage businessRolePage = new BusinessRolePage();
            BeanUtils.copyProperties(businessRole,businessRolePage);
            businessRolePage.setBusinessRelRoleList(businessRelRoles);
            businessRolePageList.add(businessRolePage);
        }
        PageInfo<BusinessRolePage> pageInfo = new PageInfo<BusinessRolePage>(businessRolePageList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 根据角色id的列表批量查询用户
     * @param roleIds
     * @return
     */
    @Override
    public BaseVo queryUserByRoleBatch(List<Long> roleIds) {
        List<User> users = flowDao.queryUserByRoleBatch(roleIds);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(users);
        return baseVo;
    }
}
