package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResidentService {
	/**
	 * app端居民登录
	 * @param residentVO
	 * @return
	 */
	BaseVo appResidentLogin(ResidentVO residentVO);

	/**
	 * app端居民注册
	 * @param residentVO
	 * @return
	 */
	BaseVo appRegister(ResidentVO residentVO);

	/**
	 * 重置密码
	 * @param commonVO
	 * @return
	 */
	BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request);
	/**
	 * app端房屋验证
	 * @return
	 */
	BaseVo appLocationApply(LocationApplyVO locationApplyModelVO);
	/**
	 * 点击房屋验证按钮出发事件
	 * @return
	 */
	BaseVo beforeLocationApply();

	/**
	 * 切换小区
	 * @param communityId
	 * @return
	 */
	BaseVo switchCommunity(Long communityId);

	/**
	 * 居民信息分页查询
	 * @param residentPageModelVO
	 * @return
	 */
	BaseVo listPage(ResidentPageVO residentPageModelVO);

	/**
	 * 居民信息导出excel
	 * @param resident 查询条件
	 * @return
	 */
	void exportToExcel(Resident resident,HttpServletResponse response);

	/**
	 * 新增居民
	 * @param residentVO
	 * @return
	 */
	BaseVo webadd(ResidentVO residentVO);

	/**
	 * 单查或返显居民信息
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);

	/**
	 * 编辑居民信息
	 * @param residentVO
	 * @return
	 */
	BaseVo editResident(ResidentVO residentVO);

	/**
	 * 根据证件号码和证件类型返显居民记录
	 * @param residentVO
	 * @return
	 */
	BaseVo copyByCardNum(ResidentVO residentVO);
	/**
	 * 删除居民住房关联关系
	 * @param residentVO
	 * @return
	 */
	BaseVo delResidentRelLocation(ResidentVO residentVO);

	/**
	 * 获取所有居民基础信息
	 * @author liyang
	 * @date 2019-07-23
	 * @param residentPageVO : 分页
	 * @return : BaseVo
	 */
	BaseVo baseResidentInfo(ResidentPageVO residentPageVO);
	/**
	 * 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	BaseVo checkMobile(String mobile);

	/**
	 * 初次更新居民密码
	 * @param commonVO
	 * @return
	 */
	BaseVo updateResidentPassword(CommonVO commonVO);
	/**
	 * 居民app 校验密码是否正确
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo checkPassword(ChangePassWordMobileVO changePassWordMobileVO);
	/**
	 * 居民app 更改密码
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changePassword(ChangePassWordMobileVO changePassWordMobileVO);
	/**
	 * 居民app 更改手机号
	 * @param changePassWordMobileVO
	 * @return
	 */
	BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO);
}
