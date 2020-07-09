package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentBasicLivingRoster;
import com.bit.module.cbo.service.ResidentRosterService;
import com.bit.module.cbo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.ipc.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @description: 服务名单相关controller
 * @author: liyang
 * @date: 2019-08-14
 **/
@RestController
@RequestMapping("/ResidentRoster")
public class ResidentRosterController {

    /**
     * 居民服务名单相关Service
     */
    @Autowired
    private ResidentRosterService residentRosterService;

    /**
     * 增加居民低保服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentBasicLivingRosterVO : 居民服务名单详情
     * @return : BaseVo
    */
    @PostMapping("/addBasicLivingRoster/{dataType}")
    public BaseVo addBasicLivingRoster(@PathVariable(value = "dataType") Integer dataType ,
                                       @RequestBody ResidentBasicLivingRosterVO residentBasicLivingRosterVO){
        return residentRosterService.addBasicLivingRoster(dataType,residentBasicLivingRosterVO);
    }

    /**
     * 增加居民低保服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentHomeCareRosterVO : 居民服务名单详情
     * @return : BaseVo
     */
    @PostMapping("/addHomeCareRoster/{dataType}")
    public BaseVo addBasicLivingRoster(@PathVariable(value = "dataType") Integer dataType ,
                                       @RequestBody ResidentHomeCareRosterVO residentHomeCareRosterVO){
        return residentRosterService.addHomeCareRoster(dataType,residentHomeCareRosterVO);
    }


    /**
     * 增加居民残疾人申请服务名单
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentDisableIndividualRosterVO 居民残疾服务名单详情
     * @return
     */
    @PostMapping("/addDisabledIndividuals/{dataType}")
    public BaseVo addDisabledIndividuals(@PathVariable(value = "dataType") Integer dataType ,
                                         @RequestBody ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO){
        return residentRosterService.addDisabledIndividuals(dataType, residentDisableIndividualRosterVO);
    }

	/**
	 * 增加居民特殊扶助申请服务名单
	 * @param dataType
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
    @PostMapping("/addSpecialSupport/{dataType}")
    public BaseVo addSpecialSupport(@PathVariable(value = "dataType") Integer dataType ,
									@RequestBody ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO){
		return residentRosterService.addSpecialSupport(dataType, residentSpecialSupportRosterVO);
	}

    /**
     * 根据事项查询服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param pageNum : 第几页
     * @param pageSize : 每页几条
     * @return : BaseVo
    */
    @GetMapping("/findAllRoster/{serviceId}/{pageNum}/{pageSize}")
    public BaseVo findAllRoster(@PathVariable(value = "serviceId") Long serviceId,
                                @PathVariable(value = "pageNum") Integer pageNum,
                                @PathVariable(value = "pageSize") Integer pageSize){
        return residentRosterService.findAllRoster(serviceId,pageNum,pageSize);
    }

    /**
     * 根据事项筛选查询居民低保服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentBasicLivingRosterVO : 查询明细
     * @return : BaseVo
     */
    @PostMapping("/findAllLivingRoster/{serviceId}")
    public BaseVo findAllLivingRoster(@PathVariable(value = "serviceId") Long serviceId,
                                @RequestBody ResidentBasicLivingRosterVO residentBasicLivingRosterVO){
        return residentRosterService.findAllLivingRoster(serviceId,residentBasicLivingRosterVO);
    }

    /**
     * 根据事项查询居家养老服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentHomeCareRosterVO : 查询明细
     * @return : BaseVo
     */
    @PostMapping("/findAllHomeCareRoster/{serviceId}")
    public BaseVo findAllHomeCareRoster(@PathVariable(value = "serviceId") Long serviceId,
                                @RequestBody ResidentHomeCareRosterVO residentHomeCareRosterVO){
        return residentRosterService.findAllHomeCareRoster(serviceId,residentHomeCareRosterVO);
    }

	/**
	 * 根据事项查询残疾人服务名单
	 * @param serviceId
	 * @param residentDisableIndividualRosterPageVO
	 * @return
	 */
    @PostMapping("/findAllDisableRoster/{serviceId}")
    public BaseVo findAllDisableRoster(@PathVariable(value = "serviceId") Long serviceId,
									   @RequestBody ResidentDisableIndividualRosterPageVO residentDisableIndividualRosterPageVO){
    	return residentRosterService.findAllDisableRoster(serviceId, residentDisableIndividualRosterPageVO);
	}

	/**
	 * 根据事项查询特殊扶助服务名单
	 * @param serviceId
	 * @param residentSpecialSupportRosterPageVO
	 * @return
	 */
	@PostMapping("/findAllSpecialSupport/{serviceId}")
	public BaseVo findAllSpecialSupport(@PathVariable(value = "serviceId") Long serviceId,
										@RequestBody ResidentSpecialSupportRosterPageVO residentSpecialSupportRosterPageVO){
    	return residentRosterService.findAllSpecialSupport(serviceId, residentSpecialSupportRosterPageVO);
	}

    /**
     * 修改低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentBasicLivingRosterVO : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modifyLivingRoster")
    public BaseVo modifyLivingRoster(@RequestBody ResidentBasicLivingRosterVO residentBasicLivingRosterVO){
        return residentRosterService.modifyLivingRoster(residentBasicLivingRosterVO);
    }

    /**
     * 修改居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentHomeCareRosterVO : 修改详情
     * @return : BaseVo
     */
    @PostMapping("/modifyHomeCareRoster")
    public BaseVo modifyHomeCareRoster(@RequestBody ResidentHomeCareRosterVO residentHomeCareRosterVO){
        return residentRosterService.modifyHomeCareRoster(residentHomeCareRosterVO);
    }

	/**
	 * 修改残疾人服务名单
	 * @param residentDisableIndividualRosterVO
	 * @return
	 */
	@PostMapping("/modifyDisableRoster")
    public BaseVo modifyDisableRoster(@RequestBody ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO){
    	return residentRosterService.modifyDisableRoster(residentDisableIndividualRosterVO);
	}

	/**
	 * 修改特殊扶助服务名单
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
	@PostMapping("/modifySpecialSupportRoster")
	public BaseVo modifySpecialSupportRoster(@RequestBody ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO){
    	return residentRosterService.modifySpecialSupportRoster(residentSpecialSupportRosterVO);
	}

    /**
     * 根据ID删除低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    @DeleteMapping("/deleteLivingRoster/{id}")
    public BaseVo deleteLivingRoster(@PathVariable(value = "id") Long id){
        return residentRosterService.deleteLivingRoster(id);
    }

    /**
     * 根据ID删除居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    @DeleteMapping("/deleteHomeCareRoster/{id}")
    public BaseVo deleteHomeCareRoster(@PathVariable(value = "id") Long id){
        return residentRosterService.deleteHomeCareRoster(id);
    }

    /**
     * 根据id删除残疾人服务名单
     * @param id
     * @return
     */
    @DeleteMapping("/deleteDisableRoster/{id}")
    public BaseVo deleteDisableRoster(@PathVariable(value = "id") Long id){
		return residentRosterService.deleteDisableRoster(id);
    }

	/**
	 * 根据id删除特殊扶助服务名单
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteSpecialSupportRoster/{id}")
	public BaseVo deleteSpecialSupportRoster(@PathVariable(value = "id") Long id){
		return residentRosterService.deleteSpecialSupportRoster(id);
	}

	/**
	 * 根据业务id和记录id 删除数据
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	@GetMapping("/deleteByServiceIdAndRecordId/{serviceId}/{recordId}")
	public BaseVo deleteByServiceIdAndRecordId(@PathVariable(value = "serviceId")Integer serviceId,@PathVariable(value = "recordId")Long recordId){
		return residentRosterService.deleteByServiceIdAndRecordId(serviceId, recordId);
	}

    /**
     * 获取低保服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
    */
    @GetMapping("/livingAllowanceDetail/{id}")
    public BaseVo livingAllowanceDetail(@PathVariable Long id){
        return residentRosterService.livingAllowanceDetail(id);
    }

    /**
     * 获取居家养老服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
     */
    @GetMapping("/homeCareDetail/{id}")
    public BaseVo homeCareDetail(@PathVariable Long id){
        return residentRosterService.homeCareDetail(id);
    }


	/**
	 * 根据业务id和记录id 查询详情
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	@GetMapping("/serviceDetail/{serviceId}/{recordId}")
	public BaseVo serviceDetail(@PathVariable(value = "serviceId")Integer serviceId,@PathVariable(value = "recordId")Long recordId){
		return residentRosterService.serviceDetail(serviceId, recordId);
	}

	/**
	 * 注销服务名单
	 * @param rosterCancelVO
	 * @return
	 */
    @PostMapping("/revokeRoster")
    public BaseVo revokeRoster(@RequestBody RosterCancelVO rosterCancelVO){
    	return residentRosterService.revokeRoster(rosterCancelVO);
	}

	/**
	 * 更新服务名单的附件
	 * @param rosterCancelVO
	 * @return
	 */
	@PostMapping("/updateRosterFile")
	public BaseVo updateRosterFile(@RequestBody RosterCancelVO rosterCancelVO){
    	return residentRosterService.updateRosterFile(rosterCancelVO);
	}

	/**
	 * 根据查询条件导出低保服务名单
	 * @author liyang
	 * @date 2019-12-09
	 * @param serviceId : 事项ID
	 * @param residentBasicLivingRosterVO : 查询条件
	*/
	@PostMapping("/basicLivingExportToExcel/{serviceId}")
	public void basicLivingExportToExcel(@PathVariable(value = "serviceId") Long serviceId,
										 @RequestBody ResidentBasicLivingRosterVO residentBasicLivingRosterVO,
										 HttpServletResponse response){
		residentRosterService.basicLivingExportToExcel(serviceId,residentBasicLivingRosterVO,response);
	}

	/**
	 * 根据查询条件导出特扶服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentSpecialSupportRosterPageVO：查询条件
	*/
	@PostMapping("/specialSupportExportToExcel/{serviceId}")
	public void specialSupportExportToExcel(@PathVariable(value = "serviceId") Long serviceId,
											@RequestBody ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO,
											HttpServletResponse response){
		residentRosterService.specialSupportExportToExcel(serviceId,residentSpecialSupportRosterVO,response);
	}

	/**
	 * 根据条件导出残疾服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentDisableIndividualRosterVO：查询条件
	*/
	@PostMapping("/disableExportToExcel/{serviceId}")
	public void disableExportToExcel(@PathVariable(value = "serviceId") Long serviceId,
									   @RequestBody ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO,
									   HttpServletResponse response){
		residentRosterService.disableExportToExcel(serviceId, residentDisableIndividualRosterVO,response);
	}

	/**
	 * 根据条件导出居家养老服务名单
	 * @author liyang
	 * @date 2019-12-10
	 * @param serviceId : 事项ID
	 * @param : residentDisableIndividualRosterVO：查询条件
	 */
	@PostMapping("/homeCareRosterExportToExcel/{serviceId}")
	public void homeCareRosterExportToExcel(@PathVariable(value = "serviceId") Long serviceId,
										@RequestBody ResidentHomeCareRosterVO residentHomeCareRosterVO,
										HttpServletResponse response){
		residentRosterService.homeCareRosterExportToExcel(serviceId,residentHomeCareRosterVO,response);
	}
}
