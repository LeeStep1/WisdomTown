package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.service.ResidentService;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 居民app专用接口
 * @Author chenduo
 * @Date 2019/8/29 14:51
 **/
@RestController
@RequestMapping("/resident/app")
public class ResidentAppController {
	@Autowired
	private ResidentService residentService;


	/**
	 * 居民app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/checkPassword")
	public BaseVo checkPassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return residentService.checkPassword(changePassWordMobileVO);
	}

	/**
	 * 居民app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changePassword")
	public BaseVo changePassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return residentService.changePassword(changePassWordMobileVO);
	}

	/**
	 * 居民app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changeMobile")
	public BaseVo changeMobile(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return residentService.changeMobile(changePassWordMobileVO);
	}

}
