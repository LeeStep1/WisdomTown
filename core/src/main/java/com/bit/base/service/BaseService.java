package com.bit.base.service;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.common.ResultCode;
import com.bit.utils.thread.RequestThreadBinder;

/**
 * @Description:
 * @Author: mifei
 * @Date: 2018-10-18
 **/
public class BaseService {


	private static SuccessVo successVo = new SuccessVo();
	/**
	 * 得到成功Vo,不用每次都new,提高性能
	 * @return
	 */
	public BaseVo successVo(){
		return successVo;
	}

	/**
	 * 优雅抛出异常
	 * @param resultCode 返回结果枚举
	 * @return
	 */
	public void throwBusinessException(ResultCode resultCode) {
		throw new BusinessException(resultCode.getInfo(),resultCode.getCode());
	}
	/**
	 * 优雅抛出异常
	 * @param exceptionMsg 异常错误信息
	 * @return
	 */
	public void throwBusinessException(String exceptionMsg) {
		throw new BusinessException(exceptionMsg);
	}

	/**
	 * 得到当前用户
	 * @return
	 */
	public UserInfo getCurrentUserInfo() {
		String user = RequestThreadBinder.getUser();
		UserInfo userInfo = (UserInfo) JSON.parseObject(user, UserInfo.class);
		return userInfo;
	}
}
