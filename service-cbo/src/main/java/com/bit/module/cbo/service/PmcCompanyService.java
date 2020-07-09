package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.vo.PmcCompanyVO;

/**
 * @description: 物业公司相关service
 * @author: liyang
 * @date: 2019-07-18
 **/
public interface PmcCompanyService {

    /**
     * 新增物业公司接口
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 新增物业公司详情
     * @return : BaseVo
    */
    BaseVo add(PmcCompany pmcCompany);

    /**
     * 修改物业公司
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(PmcCompany pmcCompany);

    /**
     * 物业公司列表展示
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompanyVO : 查询条件
     * @return : BaseVo
     */
    BaseVo findAll(PmcCompanyVO pmcCompanyVO);


    /**
     * 根据ID删除物业公司
     * @param id
     * @return
     */
    BaseVo deleteById(Long id);

    /**
     * 获取所有物业公司
     * @author liyang
     * @date 2019-07-18
     * @return : BaseVo
     */
    BaseVo findAllUseCompany(PmcCompany pmcCompany);

    /**
     * 修改物业公司状态
     * @author liyang
     * @date 2019-07-23
     * @param id : 物业公司ID
     * @param status : 即将成为的状态
     * @return : BaseVo
     */
    BaseVo modifyFlg(Long id,Integer status);
}
