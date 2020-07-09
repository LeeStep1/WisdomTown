package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.vo.ResidentApplyBaseExcelVO;
import com.bit.module.cbo.vo.ResidentApplyBaseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 办事台账相关dao
 * @author: liyang
 * @date: 2019-08-09
 **/
public interface ResidentApplyBaseDao {

    /**
     * 新增办事台账
     * @param residentApplyBase 新增明细
     */
    void add(@Param("applyBase") ResidentApplyBase residentApplyBase);

    /**
     * 获取所有启用的办事指南类别
     * @param status
     */
    List<ResidentApplyGuide> getGuideSql(@Param("status") Integer status);

    /**
     * 根据办事指南类别获取类别下的所有事项
     * @param type 数据类别:1 类别，0事项
     * @param pid 办事指南类别ID
     * @param status 状态  0停用  1 启用 2 草稿
     * @return
     */
    List<ResidentApplyGuide> getGuideItemsSql(@Param("type") Integer type,@Param("pid") Long pid,@Param("status") Integer status);

    /**
     * 获取社区台账列表
     * @param ResidentApplyBase
     * @return
     */
    List<ResidentApplyBase> findAllSql(@Param("base") ResidentApplyBaseVO ResidentApplyBase);

	/**
	 * 导出社区台账列表
	 * @param ResidentApplyBase
	 * @return
	 */
	List<ResidentApplyBaseExcelVO> exportToExcel(@Param("base") ResidentApplyBase ResidentApplyBase);

    /**
     * 根据ID查询台账明细
     * @param id
     * @return
     */
    ResidentApplyBaseVO findDetailById(@Param("id") Long id);

    /**
     * 根据申请ID获取已经完成的流程
     * @param applyId
     * @return
     */
    List<ResidentApplyProgress> queryHandleItemsByApplyIdSql(@Param("applyId") Long applyId);

    /**
     * 根据增加台账办事指南审核明细
     * @param residentApplyProgress 审核明细
     */
    void addHandleProcess(@Param("handleProcess") ResidentApplyProgress residentApplyProgress);

    /**
     * 判断流程是否已经被审核过
     * @param itemsId 流程ID
     * @param applyId 台账ID
     * @return
     */
    Integer getHandleProcess(@Param("itemsId") Long itemsId,@Param("applyId")Long applyId);

    /**
     * 修改台账
     * @param residentApplyBase 修改明细
     */
    void modify(@Param("base") ResidentApplyBase residentApplyBase);

    /**
     * 插入低保信息表
     * @param residentApplyBasicLivingAllowances 申请低保信息明细
     */
    void addBasicLivingAllowances(@Param("basicLiving") ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances);

    /**
     * 插入居家养老补充信息
     * @param residentApplyHomeCare
     */
    void addHomeCare(@Param("homeCare") ResidentApplyHomeCare residentApplyHomeCare);
	/**
	 * 插入残疾人申请记录
	 * @param residentApplyDisabledIndividuals
	 */
	void addDisable(ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals);

	/**
	 * 插入特殊扶助记录
	 * @param residentApplySpecialSupport
	 */
	void addSpecialSupport(ResidentApplySpecialSupport residentApplySpecialSupport);

    /**
     * 根据申请ID检测业务低保信息是否完善
     * @param applyId
     * @return
     */
    Integer getBasicLivingAllowancesCountByApplyId(@Param("applyId") Long applyId);

    /**
     * 根据申请ID检测居家养老信息是否完善
     * @param applyId
     * @return
     */
    Integer getHomeCareCountByApplyId(@Param("applyId") Long applyId);

    /**
     * 根据申请ID检测残疾人信息是否完善
     * @param applyId
     * @return
     */
    Integer getDisabledCountByApplyId(@Param("applyId") Long applyId);

	/**
	 * 根据申请ID检测特殊扶助信息是否完善
	 * @param applyId
	 * @return
	 */
	Integer getSpecialSupportCountByApplyId(@Param("applyId") Long applyId);

	/**
	 * 根据rosterId查询低保业务的扩展信息
	 * @param rosterId
	 * @return
	 */
	ResidentApplyBasicLivingAllowances queryResidentApplyBasicLivingAllowancesByRosterId(@Param(value = "rosterId")Long rosterId);
	/**
	 * 根据rosterId查询养老业务的扩展信息
	 * @param rosterId
	 * @return
	 */
	ResidentApplyHomeCare queryResidentApplyHomeCareByRosterId(@Param(value = "rosterId")Long rosterId);

	/**
	 * 根据rosterId查询残疾人业务的扩展信息
	 * @param rosterId
	 * @return
	 */
	ResidentApplyDisabledIndividuals queryResidentApplyDisabledIndividualsByRosterId(@Param(value = "rosterId")Long rosterId);

	/**
	 * 根据rosterId查询特殊扶助业务的扩展信息
	 * @param rosterId
	 * @return
	 */
	ResidentApplySpecialSupport queryResidentApplySpecialSupportByRosterId(@Param(value = "rosterId")Long rosterId);
}
