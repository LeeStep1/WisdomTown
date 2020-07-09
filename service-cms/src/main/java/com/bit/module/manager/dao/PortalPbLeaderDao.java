package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.vo.PortalPbLeaderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalPbLeader管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalPbLeaderDao {
	/**
	 * 根据条件查询PortalPbLeader
	 * @param portalPbLeaderVO
	 * @return
	 */
	public List<PortalPbLeader> findByConditionPage(PortalPbLeaderVO portalPbLeaderVO);
	/**
	 * 查询所有PortalPbLeader
	 * @return
	 */
	public List<PortalPbLeader> findAll(@Param(value = "sorter") String sorter,@Param(value = "delStatus") Integer delStatus);
	/**
	 * 通过主键查询单个PortalPbLeader
	 * @param id	 	 
	 * @return
	 */
	public PortalPbLeader findById(@Param(value = "id") Long id);
	/**
	 * 新增领导班子头像
	 * @param portalPbLeader
	 */
	public void add(PortalPbLeader portalPbLeader);
	/**
	 * 更新PortalPbLeader
	 * @param portalPbLeader
	 */
	public void update(PortalPbLeader portalPbLeader);
	/**
	 * 删除PortalPbLeader
	 * @param portalPbLeader
	 */
	public void delete(@Param(value = "portalPbLeader") PortalPbLeader portalPbLeader);

	/**
	 * 服务类型排序
	 * @param list
	 * @return
	 */
	void batchUpdateRank(@Param(value = "list") List<PortalPbLeader> list);
}
