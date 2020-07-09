package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.ExtendTypeBase;
import com.bit.module.cbo.bean.ResidentApplySpecialSupport;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 特殊扶助 相关实现
 * @author: liyang
 * @date: 2020-06-09
 **/
//todo 优化代码，暂不提交
@Component
public class ResidentApplySpecialSupportImpl extends AbstractExtendType{

    /**
     * 办事指南，类别与事项相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;

    /**
     * 办事台账相关dao
     */
    @Autowired
    private ResidentApplyBaseDao residentApplyBaseDao;

    /**
     * 根据台账ID获取特殊扶助扩展信息明细
     * @param id
     * @return
     */
    @Override
    public ExtendTypeBase getExtendTypeBase(Long id) {
        ResidentApplySpecialSupport res = new ResidentApplySpecialSupport();
        res.setApplyId(id);
        ResidentApplySpecialSupport resSpecial = residentApplyGuideDao.queryResidentApplySpecialSupportByParmSql(res);
        return resSpecial;
    }

    /**
     * 特殊扶助补充业务信息
     * @author liyang
     * @date 2020-06-11
     * @param extendTypeBase : 补充业务信息详情
     * @param userInfo : 修改人信息
     * @param applyId : 申请人信息
    */
    @Override
    public void addExtendInfo(ExtendTypeBase extendTypeBase, UserInfo userInfo, Long applyId) throws Exception {
        //特殊扶助申请
        //异常判断，如果有人已经完善了该信息 直接返回
        Integer count = residentApplyBaseDao.getSpecialSupportCountByApplyId(applyId);
        if(count > Const.COUNT){
            new Exception("该业务信息已完善！");
        }
        ResidentApplySpecialSupport residentApplySpecialSupport = (ResidentApplySpecialSupport) extendTypeBase;
        Date now = new Date();
        residentApplySpecialSupport.setCreateTime(now);
        residentApplySpecialSupport.setCreateUserId(userInfo.getId());
        residentApplySpecialSupport.setUpdateTime(now);
        residentApplySpecialSupport.setUpdateUserId(userInfo.getId());
        residentApplyBaseDao.addSpecialSupport(residentApplySpecialSupport);
    }
}
