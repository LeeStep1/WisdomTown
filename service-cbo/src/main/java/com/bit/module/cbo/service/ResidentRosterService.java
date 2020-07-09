package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentBasicLivingRoster;
import com.bit.module.cbo.vo.*;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.ipc.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 服务名单相关Service
 * @author: liyang
 * @date: 2019-08-14
 **/
public interface ResidentRosterService {

    /**
     * 增加居民低保服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentBasicLivingRosterVO : 低保服务名单详情
     * @return : BaseVo
     */
    BaseVo addBasicLivingRoster(Integer dataType,ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 增加居民低保服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentHomeCareRosterVO : 居民养老服务名单详情
     * @return : BaseVo
     */
    BaseVo addHomeCareRoster(Integer dataType,ResidentHomeCareRosterVO residentHomeCareRosterVO);
    /**
     * 增加居民残疾人申请服务名单
     * @param dataType
     * @param residentDisableIndividualRosterVO
     * @return
     */
    BaseVo addDisabledIndividuals(Integer dataType,ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);

	/**
	 * 增加居民特殊扶助申请服务名单
	 * @param dataType
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
    BaseVo addSpecialSupport(Integer dataType,ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO);

    /**
     * 根据事项查询服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param pageNum : 第几页
     * @param pageSize : 每页几条
     * @return : BaseVo
     */
    BaseVo findAllRoster(Long serviceId,Integer pageNum,Integer pageSize);

    /**
     * 根据事项筛选查询服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentBasicLivingRosterVO : 查询明细
     * @return : BaseVo
     */
    BaseVo findAllLivingRoster(Long serviceId,ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 根据事项查询居家养老服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentHomeCareRosterVO : 查询明细
     * @return : BaseVo
     */
    BaseVo findAllHomeCareRoster(Long serviceId,ResidentHomeCareRosterVO residentHomeCareRosterVO);

	/**
	 * 根据事项查询残疾人服务名单
	 * @param serviceId
	 * @param residentDisableIndividualRosterPageVO
	 * @return
	 */
	BaseVo findAllDisableRoster(Long serviceId,ResidentDisableIndividualRosterPageVO residentDisableIndividualRosterPageVO);
	/**
	 * 根据事项查询特殊扶助服务名单
	 * @param serviceId
	 * @param residentSpecialSupportRosterPageVO
	 * @return
	 */
	BaseVo findAllSpecialSupport(Long serviceId,ResidentSpecialSupportRosterPageVO residentSpecialSupportRosterPageVO);
    /**
     * 修改低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentBasicLivingRosterVO : 修改详情
     * @return : BaseVo
     */
    BaseVo modifyLivingRoster(ResidentBasicLivingRosterVO residentBasicLivingRosterVO);

    /**
     * 修改居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentHomeCareRosterVO : 修改详情
     * @return : BaseVo
     */
    BaseVo modifyHomeCareRoster(ResidentHomeCareRosterVO residentHomeCareRosterVO);
	/**
	 * 修改残疾人服务名单
	 * @param residentDisableIndividualRosterVO
	 * @return
	 */
    BaseVo modifyDisableRoster(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO);

	/**
	 * 修改特殊扶助服务名单
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
	BaseVo modifySpecialSupportRoster(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO);

    /**
     * 根据ID删除低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    BaseVo deleteLivingRoster(Long id);

    /**
     * 根据ID删除居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    BaseVo deleteHomeCareRoster(Long id);

	/**
	 * 根据id删除残疾人服务名单
	 * @param id
	 * @return
	 */
	BaseVo deleteDisableRoster(Long id);

	/**
	 * 根据id删除特殊扶助服务名单
	 * @param id
	 * @return
	 */
	BaseVo deleteSpecialSupportRoster(Long id);
    /**
     * 获取低保服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
     */
    BaseVo livingAllowanceDetail(Long id);

    /**
     * 获取居家养老服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
     */
    BaseVo homeCareDetail(Long id);
	/**
	 * 注销服务名单
	 * @param rosterCancelVO
	 * @return
	 */
	BaseVo revokeRoster(RosterCancelVO rosterCancelVO);
	/**
	 * 根据业务id和记录id 删除数据
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	BaseVo deleteByServiceIdAndRecordId(Integer serviceId,Long recordId);
	/**
	 * 根据业务id和记录id 查询详情
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	BaseVo serviceDetail(Integer serviceId,Long recordId);
	/**
	 * 更新服务名单的附件
	 * @param rosterCancelVO
	 * @return
	 */
	BaseVo updateRosterFile(RosterCancelVO rosterCancelVO);

	/**
	 * 根据查询条件导出低保服务名单
	 * @author liyang
	 * @date 2019-12-09
	 * @param serviceId : 事项ID
	 * @param residentBasicLivingRosterVO : 查询条件
	 */
	void basicLivingExportToExcel(Long serviceId, ResidentBasicLivingRosterVO residentBasicLivingRosterVO,HttpServletResponse response);

	/**
	 * 根据查询条件导出特扶服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentSpecialSupportRosterPageVO：查询条件
	 */
	void specialSupportExportToExcel(Long serviceId, ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO,HttpServletResponse response);

	/**
	 * 根据条件导出残疾服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentDisableIndividualRosterVO：查询条件
	 */
	void disableExportToExcel(Long serviceId, ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO,HttpServletResponse response);

	/**
	 * 根据条件导出居家养老服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentDisableIndividualRosterVO：查询条件
	 */
	void homeCareRosterExportToExcel(Long serviceId, ResidentHomeCareRosterVO residentHomeCareRosterVO,HttpServletResponse response);
}
