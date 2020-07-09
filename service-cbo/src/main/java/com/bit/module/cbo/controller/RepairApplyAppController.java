package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.RepairApply;
import com.bit.module.cbo.service.RepairApplyService;
import com.bit.module.cbo.vo.RepairApplyPageVO;
import com.bit.module.cbo.vo.RepairApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description app端故障申报
 * @Author chenduo
 * @Date 2019/8/30 9:31
 **/
@RestController
@RequestMapping("/repairApply/app")
public class RepairApplyAppController {

	@Autowired
	private RepairApplyService repairApplyService;

	/**
	 * app端故障报修
	 * @param repairApply
	 * @return
	 */
	@PostMapping("/appInsert")
	public BaseVo appInsert(@Valid @RequestBody RepairApply repairApply){
		return repairApplyService.appInsert(repairApply);
	}

	/**
	 * 取消报修
	 * @return
	 */
	@PostMapping("/cancel")
	public BaseVo cancel(@RequestBody RepairApplyVO repairApplyVO){
		return repairApplyService.cancel(repairApplyVO);
	}

	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	@GetMapping("/reflect/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return repairApplyService.reflectById(id);
	}

	/**
	 * app三端查询报修
	 * @param repairApplyPageVO
	 * @return
	 */
	@PostMapping("/appRecordListPage")
	public BaseVo appRecordListPage(@RequestBody RepairApplyPageVO repairApplyPageVO){
		return repairApplyService.appRecordListPage(repairApplyPageVO);
	}

	/**
	 * 报修记录处理
	 * @return
	 */
	@PostMapping("/applyHandle")
	public BaseVo applyHandle(@RequestBody RepairApplyVO repairApplyVO){
		return repairApplyService.applyHandle(repairApplyVO);
	}

	/**
	 * 根据token 查询小区信息
	 * @return
	 */
	@PostMapping("/getCommunityByToken")
	public BaseVo getCommunityByToken(){
		return repairApplyService.getCommunityByToken();
	}
}
