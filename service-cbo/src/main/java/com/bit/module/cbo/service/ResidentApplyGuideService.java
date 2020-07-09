package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentApplyGuide;
import com.bit.module.cbo.vo.ResidentApplyGuideVO;

import java.util.List;

/**
 * @description: 办事指南，类别与事项 相关Service
 * @author: liyang
 * @date: 2019-08-06
 **/
public interface ResidentApplyGuideService {

    /**
     * 增加 办事指南类别
     * @author liyang
     * @date 2019-08-06
     * @param residentApplyGuideVO :  办事指南类别和事件详情
     * @return : BaseVo
     */
    BaseVo add(ResidentApplyGuideVO residentApplyGuideVO);

    /**
     * 办事指南类别和事项排序
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuideList : 办事指南类别和事项最新的顺序
     * @return : BaseVo
    */
    BaseVo sortGuid(List<ResidentApplyGuide> residentApplyGuideList);

    /**
     * 获得类别和事项列表
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuide : 查询条件
     * @return : BaseVo
     */
    BaseVo findGuide(ResidentApplyGuide residentApplyGuide);

    /**
     * 修改办事指南类别和事件
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuideVO : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(ResidentApplyGuideVO residentApplyGuideVO);

    /**
     * 修改办事指南类别和事项所属ID
     * @author liyang
     * @date 2019-08-07
     * @param id : 办事指南类别和事项ID
     * @param type ：数据类别:1 类别，0事项
     * @param enable : 是否停用：1 启用，0 停用
     * @return : BaseVo
     */
    BaseVo modifyFlg(Long id,Integer type,Integer enable);

    /**
     * 根据ID查询办事指南明细
     * @author liyang
     * @date 2019-08-08
     * @param id : id
     * @param type : 数据类别:1 类别，0事项
     * @return : BaseVo
     */
    BaseVo queryId(Long id, Integer type);

    /**
     * 查询含有补充业务的类别和事项
     * @author liyang
     * @date 2019-08-19
     * @return : BaseVo
     */
    BaseVo queryGuideRoster();
}
