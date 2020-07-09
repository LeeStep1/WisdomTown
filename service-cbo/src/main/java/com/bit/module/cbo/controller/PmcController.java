package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import com.bit.module.cbo.vo.CommonVO;
import com.bit.module.cbo.service.PmcService;
import com.bit.module.cbo.vo.PmcStaffVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 物业app
 * @Author chenduo
 * @Date 2019/7/17 16:35
 **/
@RestController
@RequestMapping("/pmc")
public class PmcController {

	@Autowired
	private PmcService pmcService;

	/**
	 * 物业人员app登录
	 * @param pmcStaffModelVO
	 * @return
	 */
	@PostMapping("/appLogin")
	public BaseVo appPmcLogin(@RequestBody PmcStaffVO pmcStaffModelVO){
		return pmcService.appPmcLogin(pmcStaffModelVO);
	}

	/**
	 * 重置密码
	 * @param commonVO
	 * @return
	 */
	@PostMapping("/resetPassword")
	public BaseVo resetPassword(@RequestBody CommonVO commonVO, HttpServletRequest request){
		return pmcService.resetPassword(commonVO, request);
	}

	/**
	 * 初次更新物业密码
	 * @param commonVO
	 * @return
	 */
	@PostMapping("/updatePmcPassword")
	public BaseVo updatePmcPassword(@RequestBody CommonVO commonVO){
		return pmcService.updatePmcPassword(commonVO);
	}

	/**
	 * 物业人员校验手机号
	 * @param mobile
	 * @return
	 */
	@GetMapping("/checkMobile/{mobile}")
	public BaseVo checkMobile(@PathVariable(value = "mobile")String mobile){
		return pmcService.checkMobile(mobile);
	}

}
