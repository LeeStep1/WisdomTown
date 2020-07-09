package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.vo.PmcStaffVO;

/**
 * @description: 物业公司员工相关service
 * @author: liyang
 * @date: 2019-07-18
 **/
public interface PmcStaffService {

    /**
     * 新增物业人员
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 物业员工信息
     * @return : BaseVo
     */
    BaseVo add(PmcStaff pmcStaff);

    /**
     * 修改物业公司员工信息
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(PmcStaff pmcStaff);

    /**
     * 返回物业人员列表
     * @author liyang
     * @date 2019-07-22
     * @param pmcStaffVO : 查询条件
     * @return : BaseVo
     */
    BaseVo findAll(PmcStaffVO pmcStaffVO);

    /**
     * 物业人员停用/启用
     * @author liyang
     * @date 2019-07-22
     * @param id : 要修改的ID
     * @param status : 要修改的状态
     * @return : BaseVo
     */
    BaseVo modifyFlg(Long id,Integer status);
}
