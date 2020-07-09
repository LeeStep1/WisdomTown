package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.CivilizationBehavior;
import com.bit.module.cbo.vo.CivilizationBehaviorPageVO;

/**
 * @description: 文明行为相关service
 * @author: liyang
 * @date: 2019-08-31
 **/
public interface CivilizationBehaviorService {

    /**
     * 新增文明行为上报
     * @author liyang
     * @date 2019-08-31
     * @param civilizationBehavior : 新增详情
     * @return : BaseVo
     */
    BaseVo add(CivilizationBehavior civilizationBehavior);

    /**
     * 文明行为上报列表
     * @author liyang
     * @date 2019-08-31
     * @param civilizationBehaviorPageVO : 查询详情
     * @return : BaseVo
     */
    BaseVo findAll(CivilizationBehaviorPageVO civilizationBehaviorPageVO);

    /**
     * 修改文明行为状态
     * @author liyang
     * @date 2019-09-02
     * @param civilizationBehavior : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(CivilizationBehavior civilizationBehavior);

    /**
     * 查询文明行为明细
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    BaseVo detail(Long id);

    /**
     * 刪除上報記錄
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    BaseVo delete(Long id);
}
