package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.bean.ResidentRelLocation;
import com.bit.module.cbo.vo.LocationApplyPageVO;
import com.bit.module.cbo.vo.LocationApplyVO;
import com.bit.module.cbo.vo.ResidentVO;

import java.util.List;

public interface LocationApplyService {
	/**
	 * 认证审核 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	BaseVo listPage(LocationApplyPageVO locationApplyPageVO);

	/**
	 * 审核
	 * @param locationApplyModelVO
	 * @return
	 */
	BaseVo audit(LocationApplyVO locationApplyModelVO);

	/**
	 * app端房屋审核记录查询
	 * @param locationApplyPageVO
	 * @return
	 */
	BaseVo appApplyListPage(LocationApplyPageVO locationApplyPageVO);
	/**
	 * 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);
	/**
	 * 居委会app 房屋认证记录 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	BaseVo appOrgListPage(LocationApplyPageVO locationApplyPageVO);
	/**
	 * 居委会app 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	BaseVo appOrgReflectById(Long id);

	/**
	 * 插入住房信息
	 * @param residentVO 居民信息扩展类
	 * @param resident 居民信息
	 * @param userId 登录人ID
	 * @param orgId 登录人所属社区ID
	 * @param orgName	登录人所属社区名称
	 */
	void insertLocationApply(ResidentVO residentVO, Resident resident, Long userId, Long orgId, String orgName);

	/**
	 * 编辑住房信息---对应修改居民信息时
	 * @param residentVO 居民信息扩展类
	 * @param residentById 居民信息
	 * @param orgId 登录人ID
	 * @param userId 登录人所属社区ID
	 * @param orgName 登录人所属社区名称
	 * @return List<ResidentRelLocation>
	 */
	List<ResidentRelLocation> insertLocationForEditResident(ResidentVO residentVO, Resident residentById, Long orgId, Long userId, String orgName);
}
