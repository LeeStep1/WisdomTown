package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.PartnerOrg;
import com.bit.module.vol.vo.PartnerOrgVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenduo
 * @create 2019-03-19 13:30
 */
public interface PartnerOrgService {
    /**
     * 添加共建单位审核记录
     * @param partnerOrg
     * @return
     */
    BaseVo add(PartnerOrg partnerOrg,HttpServletRequest request);

    /**
     * 更新共建单位
     * @param partnerOrg
     * @return
     */
    BaseVo update(PartnerOrg partnerOrg,HttpServletRequest request);
    /**
     * 反显记录
     * @param id
     * @return
     */
    BaseVo reflectById(Long id);

    /**
     * 分页查询审核记录
     * @param partnerOrgVO
     * @return
     */
    BaseVo listPage(PartnerOrgVO partnerOrgVO);
    /**
     * 获取资料
     * @param id
     * @return
     */
    BaseVo getdataById(Long id);


    BaseVo getTest();

}
