package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.PbOrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PbOrganization管理的Dao
 * @author liqi
 *
 */
public interface PbOrganizationDao {

	/**
	 * 根据条件查询PbOrganization
	 * @param pbOrganizationVO
	 * @return
	 */
	public List<PbOrganization> findByConditionPage(PbOrganizationVO pbOrganizationVO);

	/**
	 * 查询所有PbOrganization
	 * @return
	 */
	public List<PbOrganization> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个PbOrganization
	 * @param id	 	 
	 * @return
	 */
	public PbOrganization findById(@Param(value = "id") Long id);

	/**
	 * 保存PbOrganization
	 * @param pbOrganization
	 */
	public void add(PbOrganization pbOrganization);

	/**
	 * 更新PbOrganization
	 * @param pbOrganization
	 */
	public void update(PbOrganization pbOrganization);

	/**
	 * 删除PbOrganization
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除PbOrganization
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据用户id查询组织
	 * @param userId
	 * @return
	 */
	List<PbOrganization> findByUserId(@Param(value = "userId")Long userId);

	/**
	 * 根据id  统计查询子集组织
	 * @param id
	 * @return
	 */
	int findCountChildListByPid(@Param(value = "id") Long id);

	/**
	 * 根据参数查询
	 * @param pbOrganization
	 * @return
	 */
	List<PbOrganization> findAllByParam(PbOrganization pbOrganization);

	/**
	 * 根据id查询子节点集合
	 * @param id
	 * @return
	 */
	List<PbOrganization> findChildListByPid(@Param(value = "id") Long id);

	/**
	 * 查询根级list
	 * @return
	 */
	List<PbOrganization> findRootPbOrgList();

	/**
	 * 保存子节点
	 * @param pbOrganization
	 */
    void addRootNode(PbOrganization pbOrganization);

	/**
	 * 保存子节点
	 * @param pbOrganization
	 */
	void addChildNode(PbOrganization pbOrganization);

	/**
	 * 查询父亲对象
	 * @param id
	 * @return
	 */
	PbOrganization findSubObjById(Long id);

	/**
	 * 查询父亲id
	 * @param id
	 * @return
	 */
	PbOrganization findPIdById(Long id);

	/**
	 * 根据pcode 查询统计
	 * @param pcode
	 * @return
	 */
	int findCountByPcode(@Param(value = "pcode") String pcode);

	/**
	 * 根据id查询组织的下级
	 * @return
	 */
	int findLowerLevelById(@Param(value = "id") String id, @Param(value = "includeItself") Boolean includeItself);

	/**
	 * 查询所有有效党建组织
	 * @return
	 */
	List<PbOrganization> findAllActive(@Param(value = "userId") Long userId);

	/**
	 * 根据组织ID批量查询userId
	 * @author liyang
	 * @date 2019-04-04
	 * @param targetIdList : 组织ID集合
	 */
	List<Long> getAllUserIdsByPbOrgIdsSql(@Param("targetIdList")List<Long> targetIdList);

	/**
	 * 批量查询指定组织指定方式注册用户
	 * @author liyang
	 * @date 2019-04-04
	 * @param messageTemplate : 组织ID集合与人员类型
	*/
	List<Long> getUserIdsByOrgIdsSql(@Param("messageTemplate") MessageTemplate messageTemplate);

	/**
	 * 获取党建组织下所有用户
	 * @author liyang
	 * @date 2019-04-09
	 * @param user : 用户筛选条件
	 * @return : List<Long> ：用户ID集合
	*/
	List<Long> getAllUserIdsForPbOrgSql(@Param("user") User user);
}
