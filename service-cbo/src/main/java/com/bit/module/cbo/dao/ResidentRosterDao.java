package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 服务名单相关dao
 * @author: liyang
 * @date: 2019-08-14
 **/
public interface ResidentRosterDao {

    /**
     * 增加居民低保服务名单
     * @param residentBasicLivingRosterVO
     */
    void addBasicLivingRoster(@Param("basicLivingRoster") ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 增加居民养老服务名单
     * @param residentHomeCareRosterVO
     */
    void addHomeCareRoster(@Param("homeCareRoster") ResidentHomeCareRosterVO residentHomeCareRosterVO);

	/**
	 * 增加居民残疾人服务名单
	 * @param residentDisableIndividualRosterVO
	 */
    void addDisableRoster(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);

	/**
	 * 增加居民特殊扶助服务名单
	 * @param residentSpecialSupportRosterVO
	 */
	void addSpecialSupportRoster(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO);

    /**
     * 修改居民低保服务名单
     * @param residentApplyBasicLivingAllowances
     */
    void modifyBasicLiving(@Param("basicLiving") ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances);

    /**
     * 修改低保服务名单表
     * @param residentBasicLivingRosterVO
     */
    void modifyLivingRoster(@Param("livingRoster") ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 修改居民养老服务名单表
     * @param residentHomeCareRosterVO
     */
    void modifyHomeCareRoster(@Param("homeRoster")ResidentHomeCareRosterVO residentHomeCareRosterVO);

    /**
     * 修改居民低保服务名单
     * @param residentApplyHomeCare
     */
    void modifyHomeCare(@Param("homeCare") ResidentApplyHomeCare residentApplyHomeCare);

	/**
	 * 修改残疾人服务名单
	 * @param residentDisableIndividualRosterVO
	 */
	void modifyDisableRoster(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);
	/**
	 * 修改居民残疾人台账
	 * @param residentApplyDisabledIndividuals
	 */
	void modifyDisable(ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals);

	/**
	 * 修改居民特殊扶助服务名单
	 * @param residentSpecialSupportRosterVO
	 */
	void modifySpecialSupportRoster(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO);

	/**
	 * 修改居民特殊扶助台账
	 * @param residentApplySpecialSupport
	 */
	void modifySpecialSupport(ResidentApplySpecialSupport residentApplySpecialSupport);

    /**
     * 获取指定事项下居民低保服务信息
     * @param residentBasicLivingRosterVO
     * @return
     */
    List<ResidentBasicLivingRosterVO> findAllBasicLivingRoster(@Param("basicLiving") ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

	/**
	 * 导出低保服务信息列表
	 * @param residentBasicLivingRosterVO 查询条件
	 * @return
	 */
	List<ResidentBasicLivingRosterExcelVO> ExportBasicLivingRoster(@Param("basicLiving") ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 获取指定事项下居家养老服务信息
     * @param residentHomeCareRosterVO
     * @return
     */
    List<ResidentHomeCareRosterVO> findAllHomeCareRoster(@Param("homeCare") ResidentHomeCareRosterVO residentHomeCareRosterVO);

	/**
	 * 导出居家养老服务名单
	 * @param residentHomeCareRosterVO
	 * @return
	 */
	List<ResidentHomeCareRosterExcelVO> exportHomeCareRoster(@Param("homeCare") ResidentHomeCareRosterVO residentHomeCareRosterVO);

	/**
	 * 获取指定事项下残疾人服务信息
	 * @param residentDisableIndividualRosterPageVO
	 * @return
	 */
    List<ResidentDisableIndividualRosterVO> findAllDisableRoster(@Param("disable")ResidentDisableIndividualRosterPageVO residentDisableIndividualRosterPageVO);

	/**
	 * 导出残疾人服务信息
	 * @param residentDisableIndividualRosterVO
	 * @return
	 */
	List<ResidentDisableIndividualRosterExcelVO> ExportDisableRoster(@Param("disable")ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);

	/**
	 * 获取指定事项下特殊扶助服务信息
	 * @param residentSpecialSupportRosterPageVO
	 * @return
	 */
	List<ResidentSpecialSupportRosterVO> findAllSpecialRoster(@Param("support")ResidentSpecialSupportRosterPageVO residentSpecialSupportRosterPageVO);

	/**
	 * 根据查询条件导出特扶服务名单
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
	List<ResidentSpecialSupportRosterExcelVO> exportToSpecialRoster(@Param("support")ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO);

    /**
     * 根据ID删除低保服务名单
     * @param id
     */
    void deleteLivingRosterById(@Param("id") Long id);

    /**
     * 根据服务名单ID删除低保明细
     * @param rosterId
     */
    void deleteLivingByRosterId(@Param("rosterId") Long rosterId);

    /**
     * 根据ID删除居家养老服务名单
     * @param id
     */
    void deleteHomeCareRosterById(@Param("id") Long id);

    /**
     * 根据服务名单ID删除居家养老明细
     * @param rosterId
     */
    void deleteHomeCareByRosterId(@Param("rosterId") Long rosterId);

	/**
	 * 根据残疾人服务名单id删除残疾人明细
	 * @param id
	 */
	void deleteDisableByRosterId(@Param("rosterId") Long id);

	/**
	 * 根据id删除残疾人明细
	 * @param id
	 */
	void deleteDisableById(@Param("id")Long id);

	/**
	 * 根据服务名单ID删除特殊扶助明细
	 * @param rosterId
	 */
	void deleteSpecialSupportByRosterId(@Param("rosterId")Long rosterId);

	/**
	 * 根据id删除特殊扶助明细
	 * @param id
	 */
	void deleteSpecialSupportById(@Param("id")Long id);

    /**
     * 根据ID查询居家养老服务名单
     * @param id
     * @return
     */
	ResidentHomeCareRoster findHomeCareRosterById(@Param("id") Long id);

    /**
     * 根据ID查询低保服务名单
     * @param id
     * @return
     */
	ResidentBasicLivingRoster findLivingRosterById(@Param("id") Long id);

	/**
	 * 根据ID查询残疾人服务名单
	 * @param id
	 * @return
	 */
	ResidentDisableIndividualRoster findDisableRosterById(@Param("id") Long id);

	/**
	 * 根据id查询特殊服务服务名单
	 * @param id
	 * @return
	 */
	ResidentSpecialSupportRoster findSpecialSupportById(@Param("id") Long id);

    /**
     * 判断能不能在低保信息转换条件
     * @param residentBasicLivingRosterVO
     * @return
     */
    Integer findConvertLivingRoster(@Param("basicLiving") ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 判断能不能在居家养老信息转换条件
     * @param residentHomeCareRosterVO
     * @return
     */
    Integer findConvertHomeCareRoster(@Param("homeCare") ResidentHomeCareRosterVO residentHomeCareRosterVO);

    /**
     * 判断能不能在残疾人信息转换条件
     * @param residentDisableIndividualRosterVO
     * @return
     */
    Integer findConvertDisableRoster(@Param(value = "disable")ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);

	/**
	 * 判断能不能在特殊帮扶信息转换条件
	 * @param residentDisableIndividualRosterVO
	 * @return
	 */
	Integer findConvertSpecialSupportRoster(@Param(value = "support")ResidentSpecialSupportRosterVO residentDisableIndividualRosterVO);

    /**
     * 根据ID查询低保申请服务明细
     * @param id
     * @return
     */
    ResidentBasicLivingRosterVO findLivingAllowanceRosterDetailById(@Param("id") Long id);

    /**
     * 根据申请ID查询低保申请明细
     * @param rosterId
     * @return
     */
    ResidentApplyBasicLivingAllowances findLivingAllowanceDetailByRosterId(@Param("rosterId") Long rosterId);

    /**
     * 根据申请ID查询低保申请明细
     * @param applyId
     * @return
     */
    ResidentApplyBasicLivingAllowances findLivingAllowanceDetailByApplyId(@Param("applyId") Long applyId);

    /**
     * 根据ID查询居家养老服务明细
     * @param id
     * @return
     */
    ResidentHomeCareRosterVO findHomeCareRosterDetailById(@Param("id") Long id);

    /**
     * 根据申请ID查询居家养老明细
     * @param rosterId
     * @return
     */
    ResidentApplyHomeCare findHomeCareDetailByRosterId(@Param("rosterId") Long rosterId);

	/**
	 * 根据id查询残疾人服务明细
	 * @param id
	 * @return
	 */
	ResidentDisableIndividualRosterVO findDisbaleDetailsById(@Param("id")Long id);

	/**
	 * 根据申请ID查询残疾人服务明细
	 * @param rosterId
	 * @return
	 */
	ResidentApplyDisabledIndividuals findDisableDetailByRosterId(@Param("rosterId") Long rosterId);

	/**
	 * 根据id查询特殊扶助服务明细
	 * @param id
	 * @return
	 */
	ResidentSpecialSupportRosterVO findSpecialSupportDetailsById(@Param("id")Long id);

	/**
	 * 根据申请ID查询特殊扶助服务明细
	 * @param rosterId
	 * @return
	 */
	ResidentApplySpecialSupport findSpecialSupportDetailByRosterId(@Param("rosterId") Long rosterId);
}
