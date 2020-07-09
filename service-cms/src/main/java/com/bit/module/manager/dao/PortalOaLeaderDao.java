package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalOaLeader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalOaLeader管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalOaLeaderDao {
	/**
	 * 查询所有PortalOaLeader
	 * @return
	 */
	public List<PortalOaLeader> findAll(@Param(value = "sort")String sort,@Param(value = "delStatus")Integer delStatus);
	/**
	 * 通过主键查询单个PortalOaLeader
	 * @param id	 	 
	 * @return
	 */
	public PortalOaLeader findById(@Param(value = "id") Long id);
	/**
	 * 保存PortalOaLeader
	 * @param portalOaLeader
	 */
	public void add(PortalOaLeader portalOaLeader);
	/**
	 * 更新PortalOaLeader
	 * @param portalOaLeader
	 */
	public void update(PortalOaLeader portalOaLeader);
	/**
	 * 删除PortalOaLeader
	 * @param portalOaLeader
	 */
	public void delete(@Param(value = "portalOaLeader") PortalOaLeader portalOaLeader);

	/**
	 * 领导介绍排序
	 * @param portalOaLeaderList
	 * @return
	 */
	void batchUpdateRank(@Param(value = "list") List<PortalOaLeader> portalOaLeaderList);
}
