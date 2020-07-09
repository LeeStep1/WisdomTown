package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import com.bit.module.cbo.vo.CommonVO;

import javax.servlet.http.HttpServletRequest;

public interface AppCommonService {
	/**
	 * 三个app重置密码
	 * @param commonVO
	 * @return
	 */
	BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request);
	/**
	 * 居民 和 物业 app更换密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changePassWord(ChangePassWordMobileVO changePassWordMobileVO);

	/**
	 * 居民 和 物业 app更换手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO);
}
