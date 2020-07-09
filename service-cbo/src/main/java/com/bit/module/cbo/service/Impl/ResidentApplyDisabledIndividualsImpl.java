package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.ExtendTypeBase;
import com.bit.module.cbo.bean.ResidentApplyDisabledIndividuals;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 残疾人业务相关实现
 * @author: liyang
 * @date: 2020-06-09
 **/
//todo 优化代码，暂不提交
@Component
@EnableAspectJAutoProxy
public class ResidentApplyDisabledIndividualsImpl extends AbstractExtendType {

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
     * 根据台账ID获取残疾人扩展信息明细
     * @param id
     * @return
     */
    @Override
    public ExtendTypeBase getExtendTypeBase(Long id) {
        ResidentApplyDisabledIndividuals res = new ResidentApplyDisabledIndividuals();
        res.setApplyId(id);
        ResidentApplyDisabledIndividuals resDisableReturn = residentApplyGuideDao.queryResidentDisableByParmSql(res);

        return resDisableReturn;
    }

    /**
     * 残疾人补充业务信息
     * @author liyang
     * @date 2020-06-11
     * @param extendTypeBase : 补充业务信息详情
     * @param userInfo : 修改人信息
     * @param applyId : 申请人信息
     */
    @Override
    public void addExtendInfo(ExtendTypeBase extendTypeBase, UserInfo userInfo, Long applyId) throws Exception {
        //残疾人申请
        //异常判断，如果有人已经完善了该信息 直接返回
        Integer count = residentApplyBaseDao.getDisabledCountByApplyId(applyId);
        if(count > Const.COUNT){
            throw new Exception("该业务信息已完善！");
        }

        ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals = (ResidentApplyDisabledIndividuals) extendTypeBase;
        Date now = new Date();
        residentApplyDisabledIndividuals.setCreateTime(now);
        residentApplyDisabledIndividuals.setCreateUserId(userInfo.getId());
        residentApplyDisabledIndividuals.setUpdateTime(now);
        residentApplyDisabledIndividuals.setUpdateUserId(userInfo.getId());
        residentApplyBaseDao.addDisable(residentApplyDisabledIndividuals);

    }
}
