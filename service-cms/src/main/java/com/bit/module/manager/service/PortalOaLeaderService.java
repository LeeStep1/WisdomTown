package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalOaLeader;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * PortalOaLeader的Service
 * @author liuyancheng
 */
public interface PortalOaLeaderService {
	/**
	 * 通过主键查询单个PortalOaLeader
	 * @param id
	 * @return
	 */
	PortalOaLeader findById(Long id);

	/**
	 * 保存PortalOaLeader
	 * @param portalOaLeader
	 */
	void add(PortalOaLeader portalOaLeader, HttpServletRequest request);
	/**
	 * 更新PortalOaLeader
	 * @param portalOaLeader
	 */
	void update(PortalOaLeader portalOaLeader);
	/**
	 * 删除PortalOaLeader
	 * @param id
	 */
	void delete(Long id, HttpServletRequest request);

	/**
	 * 获取领导介绍内容列表
	 * @author liyang
	 * @date 2019-05-15
	 * @param staionId : 站点ID
	 * @param categoryId : 栏目ID
	 * @return : BaseVo
	 */
	BaseVo getLeaderIntroduce(Long staionId,Long categoryId);

	/**
	 * 领导介绍排序
	 * @param portalOaLeaderList
	 * @return
	 */
	BaseVo serviceSort(List<PortalOaLeader> portalOaLeaderList);
}
