package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.service.RepairApplyService;
import com.bit.module.cbo.vo.RepairApplyPageVO;
import com.bit.module.cbo.vo.RepairApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description web端故障申报
 * @Author chenduo
 * @Date 2019/8/30 9:31
 **/
@RestController
@RequestMapping("/repairApply/web")
public class RepairApplyWebController {
	@Autowired
	private RepairApplyService repairApplyService;

	/**
	 * web 端故障申报分页查询
	 * @param repairApplyPageVO
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody RepairApplyPageVO repairApplyPageVO){
		return repairApplyService.webListPage(repairApplyPageVO);
	}

	/**
	 * web端返显记录
	 * @param id
	 * @return
	 */
	@GetMapping("/reflect/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return repairApplyService.reflectById(id);
	}

	/**
	 * web端报修记录处理
	 * @param repairApplyVO
	 * @return
	 */
	@PostMapping("/applyHandle")
	public BaseVo applyHandle(@RequestBody RepairApplyVO repairApplyVO){
		return repairApplyService.applyHandle(repairApplyVO);
	}
}
