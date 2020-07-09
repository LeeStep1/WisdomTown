package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.vo.ResidentApplyGuideVO;
import com.bit.module.cbo.vo.ResidentApplyProgressVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 办事指南，类别与事项相关dao
 * @author: liyang
 * @date: 2019-08-06
 **/
public interface ResidentApplyGuideDao {

    /**
     * 获取办事指南类别与事项最大排序号
     * @param pid
     * @return
     */
    int getMaxSort(@Param("pid") Long pid);

    /**
     * 增加 办事指南类别
     * @param residentApplyGuideVO 办事指南类别详情
     */
    void addGuid(@Param("guide") ResidentApplyGuideVO residentApplyGuideVO);

    /**
     * 增加办事指南事项
     * @param residentApplyGuideItemsList 办事指南事项详情
     */
    void addGuidItems(@Param("items") List<ResidentApplyGuideItems> residentApplyGuideItemsList);

    /**
     * 办事指南类别和事项排序
     * @param residentApplyGuideList 办事指南类别和事项最新的顺序
     */
    void sortGuidSql(@Param("list") List<ResidentApplyGuide> residentApplyGuideList);

    /**
     * 根据查询条件查询办事指南类别和事项
     * @param residentApplyGuide 查询条件
     * @return
     */
    List<ResidentApplyGuide> findGuideSql(@Param("guide") ResidentApplyGuide residentApplyGuide);

    /**
     * 根据名称查询办事指南类别和事项重复
     * @param residentApplyGuideVO
     * @return
     */
    Integer findGuideCountByNameSql(@Param("guide") ResidentApplyGuideVO residentApplyGuideVO);


    /**
     * 修改办事指南类别和事件表
     * @param residentApplyGuide 修改详情
     */
    void modifyGuideSql(@Param("guide") ResidentApplyGuideVO residentApplyGuide);

    /**
     * 根据事项ID删除事项内容明细
     * @param guideId 事项ID
     */
    void deleteGuidItemsByGuideIdSql(@Param("guideId") Long guideId);

    /**
     * 修改办事指南类别和事项状态
     * @param residentApplyGuide 修改详情
     */
    void modifyGuideFalgSql(@Param("guide") ResidentApplyGuide residentApplyGuide);

    /**
     * 根据ID查询事项或者类别明细
     * @param id
     * @return
     */
    ResidentApplyGuideVO queryByIdSql(@Param("id") Long id);

    /**
     * 根据事项ID查询明细
     * @param guideId 事项id
     * @return
     */
    List<ResidentApplyGuideItems> queryByGuideIdSql(@Param("guideId") Long guideId);

    /**
     * 修改事项明细
     * @param residentApplyGuideItemsList
     */
    void modifyGuideItems(@Param("list") List<ResidentApplyGuideItems> residentApplyGuideItemsList);

    /**
     * 查询所有指南类别和事项
     * @return
     */
    List<ResidentApplyGuide> findAllGuide();

    /**
     * 根据事项ID查询指定明细
     * @param residentApplyGuideItems 查询条件
     * @return
     */
    List<ResidentApplyProgressVO> queryItemsByParmSql(@Param("guideItems") ResidentApplyGuideItems residentApplyGuideItems);

    /**
     * 根据字段查询低保类型
     * @param residentApplyBasicLivingAllowances
     * @return
     */
    ResidentApplyBasicLivingAllowances queryAllowancesByParmSql(@Param("allowances") ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances);

    /**
     * 根据字段查询居家养老补充业务
     * @param ResidentApplyHomeCare
     * @return
     */
    ResidentApplyHomeCare queryResidentHomeCareByParmSql(@Param("applyHomeCare")ResidentApplyHomeCare ResidentApplyHomeCare);

	/**
	 * 根据字段查询残疾人补充业务
	 * @param residentApplyDisabledIndividuals
	 * @return
	 */
	ResidentApplyDisabledIndividuals queryResidentDisableByParmSql(@Param(value = "disable")ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals);

    /**
     * 根据字段查询特殊扶助补充业务
     * @param residentApplySpecialSupport
     * @return
     */
	ResidentApplySpecialSupport queryResidentApplySpecialSupportByParmSql(@Param(value = "support")ResidentApplySpecialSupport residentApplySpecialSupport);


    /**
     * 获取所有包含补充信息的事项
     * @return
     */
    List<ResidentApplyGuide> queryGuideItemParmExtend();

    /**
     * 获取所有类别
     * @return
     */
    List<ResidentApplyGuideVO> findAllGuideForCategory();
}
