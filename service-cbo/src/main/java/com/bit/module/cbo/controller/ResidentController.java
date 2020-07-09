package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.vo.*;
import com.bit.module.cbo.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 居民的接口
 * @Author chenduo
 * @Date 2019/7/16 15:37
 **/
@RestController
@RequestMapping("/resident")
public class ResidentController {
	@Autowired
	private ResidentService residentService;

	/**
	 * app端居民登录
	 * @param residentVO
	 * @return
	 */
	@PostMapping("/appLogin")
	public BaseVo appResidentLogin(@RequestBody ResidentVO residentVO){
		return residentService.appResidentLogin(residentVO);
	}

	/**
	 * app端居民注册
	 * @param residentVO
	 * @return
	 */
	@PostMapping("/appRegister")
	public BaseVo appRegister(@RequestBody ResidentVO residentVO){
		return residentService.appRegister(residentVO);
	}

	/**
	 * 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	@GetMapping("/checkMobile/{mobile}")
	public BaseVo checkMobile(@PathVariable(value = "mobile")String mobile){
		return residentService.checkMobile(mobile);
	}

	/**
	 * 重置密码
	 * @param commonVO
	 * @return
	 */
	@PostMapping("/resetPassword")
	public BaseVo resetPassword(@RequestBody CommonVO commonVO, HttpServletRequest request){
		return residentService.resetPassword(commonVO, request);
	}

	/**
	 * 初次更新居民密码
	 * @param commonVO
	 * @return
	 */
	@PostMapping("/updateResidentPassword")
	public BaseVo updateResidentPassword(@RequestBody CommonVO commonVO){
		return residentService.updateResidentPassword(commonVO);
	}

	/**
	 * 点击房屋验证按钮出发事件
	 * @return
	 */
	@PostMapping("/beforeLocationApply")
	public BaseVo beforeLocationApply(){
		return residentService.beforeLocationApply();
	}

	/**
	 * app端房屋验证
	 * @return
	 */
	@PostMapping("/appLocationApply")
	public BaseVo appLocationApply(@RequestBody LocationApplyVO locationApplyModelVO){
		return residentService.appLocationApply(locationApplyModelVO);
	}

	/**
	 * 切换小区
	 * @param communityId
	 * @return
	 */
	@GetMapping("/switchCommunity/{communityId}")
	public BaseVo switchCommunity(@PathVariable(value = "communityId")Long communityId){
		return residentService.switchCommunity(communityId);
	}

	/**
	 * 居民信息分页查询
	 * @param residentPageModelVO
	 * @return
	 */
	@PostMapping("/listPage")
	public BaseVo listPage(@RequestBody ResidentPageVO residentPageModelVO){
		return residentService.listPage(residentPageModelVO);
	}


	/**
	 * 居民信息导出excel
	 * @param resident 查询条件
	 * @return
	 */
	@PostMapping("/exportToExcel")
	public void exportToExcel(@RequestBody Resident resident, HttpServletResponse response){
		residentService.exportToExcel(resident,response);
	}

	/**
	 * 新增居民
	 * @param residentVO
	 * @return
	 */
	@PostMapping("/add")
	public BaseVo webadd(@RequestBody ResidentVO residentVO){
		return residentService.webadd(residentVO);
	}

	/**
	 * 单查或返显居民信息
	 * @param id
	 * @return
	 */
	@GetMapping("/reflect/{id}")
	public BaseVo reflectById(@PathVariable(value = "id") Long id){
		return residentService.reflectById(id);
	}

	/**
	 * 编辑居民信息
	 * @param residentVO
	 * @return
	 */
	@PostMapping("/edit")
	public BaseVo editResident(@RequestBody ResidentVO residentVO){
		return residentService.editResident(residentVO);
	}

	/**
	 * 根据证件号码和证件类型返显居民记录
	 * @return
	 */
	@PostMapping("/copyByCardNum")
	public BaseVo copyByCardNum(@RequestBody ResidentVO residentVO){
		return residentService.copyByCardNum(residentVO);
	}

	/**
	 * 删除居民住房关联关系
	 * @param residentVO
	 * @return
	 */
	@PostMapping("/delResidentRelLocation")
	public BaseVo delResidentRelLocation(@RequestBody ResidentVO residentVO){
		return residentService.delResidentRelLocation(residentVO);
	}

	/**
	 * 获取所有居民基础信息
	 * @author liyang
	 * @date 2019-07-23
	 * @param residentPageVO : 分页
	 * @return : BaseVo
	*/
	@PostMapping("/baseResidentInfo")
	public BaseVo baseResidentInfo(@RequestBody ResidentPageVO residentPageVO){
		return residentService.baseResidentInfo(residentPageVO);
	}

}
