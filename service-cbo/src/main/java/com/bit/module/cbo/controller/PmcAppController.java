package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.service.PmcService;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 物业app专用接口
 * @Author chenduo
 * @Date 2019/8/29 14:49
 **/
@RestController
@RequestMapping("/pmc/app")
public class PmcAppController {
	@Autowired
	private PmcService pmcService;


	/**
	 * 物业app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/checkPassword")
	public BaseVo checkPassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return pmcService.checkPassword(changePassWordMobileVO);
	}

	/**
	 * 物业app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changePassword")
	public BaseVo changePassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return pmcService.changePassword(changePassWordMobileVO);
	}

	/**
	 * 物业app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changeMobile")
	public BaseVo changeMobile(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return pmcService.changeMobile(changePassWordMobileVO);
	}

	/**
	 * 物业app 删除物业员工
	 * @param id
	 * @return
	 */
	@GetMapping("/delPmcStaff/{id}")
	public BaseVo delPmcStaff(@PathVariable(value = "id")Long id){
		return pmcService.delPmcStaff(id);
	}
}
