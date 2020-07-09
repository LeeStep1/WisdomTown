package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.DeadResident;
import com.bit.module.cbo.vo.DeadResidentVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 死亡居民信息相关service
 * @author: liyang
 * @date: 2019-07-22
 **/
public interface DeadResidentService {

    /**
     * 新增死亡居民信息
     * @author liyang
     * @date 2019-07-22
     * @param deadResident : 新增详情
     * @return : BaseVo
     */
    BaseVo add(DeadResident deadResident);

    /**
     * 修改死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param deadResident : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(DeadResident deadResident);

    /**
     * 返回死亡居民信息列表接口
     * @author liyang
     * @date 2019-07-23
     * @param deadResidentVO : 查询条件
     * @return : BaseVo
     */
    BaseVo findAll(DeadResidentVO deadResidentVO);

    /**
     * 批量导出死亡居民信息
     * @author liyang
     * @date 2019-12-06
     * @param deadResident : 查询条件
     * @return : response
     */
    void exportToExcel(DeadResident deadResident,HttpServletResponse response);

    /**
     * 返回死亡信息详情
     * @author liyang
     * @date 2019-07-23
     * @param id : id
     * @return : BaseVo
     */
    BaseVo detail(Long id);

    /**
     * 删除死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param id : 信息id
     * @return : BaseVo
     */
    BaseVo delete(Long id);

}
