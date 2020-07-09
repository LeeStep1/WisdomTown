package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.ExtendTypeBase;
import com.bit.module.cbo.bean.ResidentApplyBasicLivingAllowances;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 低保信息相关实现
 * @author: liyang
 * @date: 2020-06-09
 **/
@Component
public class BasicLivingAllowancesImpl extends AbstractExtendType{

    /**
     * 办事指南相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;

    /**
     * 办事台账相关dao
     */
    @Autowired
    private ResidentApplyBaseDao residentApplyBaseDao;

    /**
     * 根据台账ID获取低保信息扩展信息明细
     * @param id
     * @return
     */
    @Override
    public ExtendTypeBase getExtendTypeBase(Long id) {
        ResidentApplyBasicLivingAllowances res = new ResidentApplyBasicLivingAllowances();
        res.setApplyId(id);
        ResidentApplyBasicLivingAllowances resReturn = residentApplyGuideDao.queryAllowancesByParmSql(res);
        return resReturn;
    }

    /**
     * 低保补充业务信息
     * @author liyang
     * @date 2020-06-11
     * @param extendTypeBase : 补充业务信息详情
     * @param userInfo : 修改人信息
     * @param applyId : 申请人信息
     */
    public void addExtendInfo(ExtendTypeBase extendTypeBase, UserInfo userInfo,Long applyId) throws Exception {
        //异常判断，如果有人已经完善了该信息 直接返回
        Integer count = residentApplyBaseDao.getBasicLivingAllowancesCountByApplyId(applyId);
        if(count > Const.COUNT){
            throw new Exception("该业务信息已完善！");
        }

        //插入低保信息表
        Date now = new Date();
        ResidentApplyBasicLivingAllowances rabla = (ResidentApplyBasicLivingAllowances)extendTypeBase;
        rabla.setCreateTime(now);
        rabla.setCreateUserId(userInfo.getId());
        rabla.setUpdateTime(now);
        rabla.setUpdateUserId(userInfo.getId());
        rabla.setReleaseTime(now);
        residentApplyBaseDao.addBasicLivingAllowances(rabla);
    }
}
