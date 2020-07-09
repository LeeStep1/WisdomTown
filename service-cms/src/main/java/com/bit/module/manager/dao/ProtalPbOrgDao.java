package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.bean.ProtalPbOrg;
import com.bit.module.manager.vo.ProtalPbOrgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ProtalPbOrg管理的Dao
 * @author liuyancheng
 *
 */
public interface ProtalPbOrgDao {
	/**
	 * 根据条件查询ProtalPbOrg
	 * @param protalPbOrgVO
	 * @return
	 */
	public List<ProtalPbOrg> findByConditionPage(ProtalPbOrgVO protalPbOrgVO);
	/**
	 * 查询所有ProtalPbOrg
	 * @return
	 */
	public List<ProtalPbOrg> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个ProtalPbOrg
	 * @param id	 	 
	 * @return
	 */
	public ProtalPbOrg findById(@Param(value = "id") Long id);
	/**
	 * 保存ProtalPbOrg
	 * @param protalPbOrg
	 */
	public void add(ProtalPbOrg protalPbOrg);
	/**
	 * 更新ProtalPbOrg
	 * @param protalPbOrg
	 */
	public void update(ProtalPbOrg protalPbOrg);
	/**
	 * 删除ProtalPbOrg
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 党建组织导航下栏目展示
	 * @author liyang
	 * @date 2019-05-24
	 * @param navigationId : 所属导航
	 * @param categoryId : 所属栏目
	 * @return : BaseVo
	 */
	ProtalPbOrg getPbOrgNavigationContendSql(@Param("navigationId") Long navigationId,@Param("categoryId") Long categoryId);

	/**
	 * 党建组织导航下栏目展示
	 * @author liyang
	 * @date 2019-05-24
	 * @param portalPbLeader : 筛选条件
	 * @return : List<PortalPbLeader>
	 */
	List<PortalPbLeader> getLeaderImageSql(@Param("portalPbLeader") PortalPbLeader portalPbLeader);
}
