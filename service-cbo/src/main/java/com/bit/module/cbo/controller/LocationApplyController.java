package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.LocationApplyPageVO;
import com.bit.module.cbo.service.LocationApplyService;
import com.bit.module.cbo.vo.LocationApplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 13:18
 **/
@RestController
@RequestMapping("/locationApply")
public class LocationApplyController {


	@Autowired
	private LocationApplyService locationApplyService;

	/**
	 * 认证审核 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@PostMapping("/listPage")
	public BaseVo listPage(@RequestBody LocationApplyPageVO locationApplyPageVO){
		return locationApplyService.listPage(locationApplyPageVO);
	}

	/**
	 * 审核
	 * @param locationApplyModelVO
	 * @return
	 */
	@PostMapping("/audit")
	public BaseVo audit(@RequestBody LocationApplyVO locationApplyModelVO){
		return locationApplyService.audit(locationApplyModelVO);
	}

	/**
	 * app端房屋审核记录查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@PostMapping("/appApplyListPage")
	public BaseVo appApplyListPage(@RequestBody LocationApplyPageVO locationApplyPageVO){
		return locationApplyService.appApplyListPage(locationApplyPageVO);
	}

	/**
	 * 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	@GetMapping("/reflectById/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return locationApplyService.reflectById(id);
	}

	/**
	 * 居委会app 房屋认证记录 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	@PostMapping("/appOrgApplyListPage")
	public BaseVo appOrgListPage(@RequestBody LocationApplyPageVO locationApplyPageVO){
		return locationApplyService.appOrgListPage(locationApplyPageVO);
	}

	/**
	 * 居委会app审核
	 * @param locationApplyModelVO
	 * @return
	 */
	@PostMapping("/appOrgAudit")
	public BaseVo appOrgAudit(@RequestBody LocationApplyVO locationApplyModelVO){
		return locationApplyService.audit(locationApplyModelVO);
	}
	/**
	 * 居委会app 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	@GetMapping("/appOrgReflectById/{id}")
	public BaseVo appOrgReflectById(@PathVariable(value = "id")Long id){
		return locationApplyService.reflectById(id);
	}
}
