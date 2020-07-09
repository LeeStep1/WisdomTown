package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.CommonModel;
import com.bit.module.system.vo.ChangePassWordMobileVO;
import com.bit.module.system.vo.UserLoginVO;
import com.bit.module.system.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CboService {
	/**
	 * 社区和 社区办的app登录
	 * @param userLoginVO
	 * @return
	 */
	BaseVo cboOrgAppLogin(UserLoginVO userLoginVO);

	/**
	 * 社区和 社区办的app 重置密码
	 * @param commonModel
	 * @return
	 */
	BaseVo cboOrgResetPassword(CommonModel commonModel, HttpServletRequest request);

	/**
	 * 社区app 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	BaseVo checkMobile(String mobile);


	/**
	 * 社区模板，根据社区的id，查找相关的管理员
	 * @param deps
	 * @return BaseVo
	 */
	BaseVo getUserIdsByDeps(List<Long> deps);

	/**
	 * 社区app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO);
	/**
	 * 社区app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO);
	/**
	 * 社区app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO);
}
