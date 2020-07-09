package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.vo.PortalPbLeaderVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * PortalPbLeader的Service
 * @author liuyancheng
 */
public interface PortalPbLeaderService {
	/**
	 * 根据条件查询PortalPbLeader
	 * @param portalPbLeaderVO
	 * @return
	 */
	BaseVo findByConditionPage(PortalPbLeaderVO portalPbLeaderVO);
	/**
	 * 查询所有PortalPbLeader
	 * @return
	 */
	BaseVo findAll();
	/**
	 * 通过主键查询单个PortalPbLeader
	 * @param id
	 * @return
	 */
	PortalPbLeader findById(Long id);

	/**
	 * 新增领导班子头像
	 * @param portalPbLeader
	 */
	void add(PortalPbLeader portalPbLeader, HttpServletRequest request);
	/**
	 * 更新PortalPbLeader
	 * @param portalPbLeader
	 */
	void update(PortalPbLeader portalPbLeader);
	/**
	 * 删除PortalPbLeader
	 * @param id
	 */
	BaseVo delete(Long id, HttpServletRequest request);

	/**
	 * 领导班子头像排序
	 * @param portalPbLeaderList
	 * @return
	 */
	BaseVo portalPbLeaderSort(List<PortalPbLeader> portalPbLeaderList);
}
