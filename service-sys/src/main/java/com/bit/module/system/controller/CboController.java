package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.CommonModel;
import com.bit.module.system.bean.User;
import com.bit.module.system.service.CboService;
import com.bit.module.system.vo.ChangePassWordMobileVO;
import com.bit.module.system.vo.UserLoginVO;
import com.bit.module.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 17:17
 **/
@RestController
@RequestMapping("/cbo")
public class CboController {

	@Autowired
	private CboService cboService;

	/**
	 * 社区和 社区办的app登录
	 * @param userLoginVO
	 * @return
	 */
	@PostMapping("/cboOrgAppLogin")
	public BaseVo cboOrgAppLogin(@RequestBody UserLoginVO userLoginVO){
		return cboService.cboOrgAppLogin(userLoginVO);
	}

	/**
	 * 社区和 社区办的app 重置密码
	 * @param commonModel
	 * @return
	 */
	@PostMapping("/cboOrgResetPassword")
	public BaseVo cboOrgResetPassword(@RequestBody CommonModel commonModel,HttpServletRequest request){
		return cboService.cboOrgResetPassword(commonModel,request);
	}

	/**
	 * 社区根据社区IDS查询出用户
	 * @param list
	 * @return
	 */
	@PostMapping("/getUserIdsByDeps}")
	public BaseVo getUserIdsByDeps(@RequestBody List<Long>list){
		return cboService.getUserIdsByDeps(list);
	}

	/**
	 * 社区app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/checkPassword")
	public BaseVo checkPassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return cboService.checkPassword(changePassWordMobileVO);
	}

	/**
	 * 社区app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changePassword")
	public BaseVo changePassword(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return cboService.changePassword(changePassWordMobileVO);
	}

	/**
	 * 社区app 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	@GetMapping("/checkMobile/{mobile}")
	public BaseVo checkMobile(@PathVariable(value = "mobile")String mobile){
		return cboService.checkMobile(mobile);
	}

	/**
	 * 社区app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	@PostMapping("/changeMobile")
	public BaseVo changeMobile(@RequestBody ChangePassWordMobileVO changePassWordMobileVO){
		return cboService.changeMobile(changePassWordMobileVO);
	}
}
