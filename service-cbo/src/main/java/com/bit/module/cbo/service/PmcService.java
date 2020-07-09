package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import com.bit.module.cbo.vo.CommonVO;
import com.bit.module.cbo.vo.PmcStaffVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface PmcService {
	/**
	 * 物业人员app登录
	 * @param pmcStaffModelVO
	 * @return
	 */
	BaseVo appPmcLogin(PmcStaffVO pmcStaffModelVO);
	/**
	 * 重置密码
	 * @param commonVO
	 * @return
	 */
	BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request);
	/**
	 * 初次更新物业密码
	 * @param commonVO
	 * @return
	 */
	BaseVo updatePmcPassword(CommonVO commonVO);

	/**
	 * 物业人员校验手机号
	 * @param mobile
	 * @return
	 */
	BaseVo checkMobile(String mobile);
	/**
	 * 物业app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO);

	/**
	 * 物业app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO);
	/**
	 * 物业app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO);

	/**
	 * 物业app 删除物业员工
	 * @param id
	 * @return
	 */
	BaseVo delPmcStaff(Long id);
}
