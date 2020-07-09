package com.bit.module.system.dao;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.OaDepartmentResultVO;
import com.bit.module.system.vo.OaDepartmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OaDepartment管理的Dao
 * @author 
 *
 */
public interface OaDepartmentDao {
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	public List<OaDepartment> findByConditionPage(OaDepartmentVO oaDepartmentVO);
	/**
	 * 查询所有OaDepartment
	 * @return
	 */
	public List<OaDepartment> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个OaDepartment
	 * @param id	 	 
	 * @return
	 */
	public OaDepartment findById(@Param(value = "id") Long id);

	/**
	 * 通过主键查询单个OaDepartment
	 * @param id
	 * @return
	 */
	public OaDepartmentResultVO findResultById(@Param(value = "id") Long id);

	/**
	 * 保存OaDepartment
	 * @param oaDepartment
	 */
	public void add(OaDepartment oaDepartment);

	/**
	 * 保存OaDepartmentVO
	 * @param oaDepartmentVO
	 */
	public void addVO(OaDepartmentVO oaDepartmentVO);
	/**
	 * 保存OaDepartmentVO
	 * @param oaDepartmentResultVO
	 */
	public void addResultVO(OaDepartmentResultVO oaDepartmentResultVO);
	/**
	 * 更新OaDepartment
	 * @param oaDepartmentResultVO
	 */
	void updateResult(OaDepartmentResultVO oaDepartmentResultVO);
	/**
	 * 删除OaDepartment
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据用户userId 查询
	 * @param userId
	 * @return
	 */
    List<OaDepartment> findByUserId(@Param(value = "userId") Long userId);
    List<Long> findByUserIds(@Param(value = "userId") Long userId);

	/**
	 * 根据上级查询下级
	 * @param upLevel
	 * @return
	 */
	List<OaDepartment> findByIdLike(@Param(value = "upLevel") String upLevel,@Param(value = "temp")String temp);

	/**
	 * 查询组织编码是否唯一
	 * @param deptCode
	 * @return
	 */
	int findByCode(@Param(value = "deptCode") String deptCode);

	/**
	 * 查询组织结构明细
	 * @param id
	 * @return
	 */
	OaDepartment findOaDepartmentByIdSql(@Param("id") Long id);

	/**
	 * 根据ID查询组织结构下级明细
	 * @param id
	 * @return
	 */
	List<OaDepartment> findOaDepartmentIncloudIdSql(@Param("id") Long id);

	/**
	 * 根据用户查询机构集合
	 * @param userId
	 * @return
	 */
	List<OaDepartment> findAllActive(@Param(value = "userId")Long userId);

	/**
	 * 根据组织ID批量查询userId
	 * @author liyang
	 * @date 2019-04-04
	 * @param targetIdList : 组织ID集合
	 */
	List<Long> getAllUserIdsByOaOrgIdsSql(@Param("targetIdList") List<Long> targetIdList);

	/**
	 * 获取党建组织下所有用户
	 * @author liyang
	 * @date 2019-04-09
	 * @param : user : 用户筛选条件
	 * @return : List<Long> ：用户ID集合
	 */
	List<Long> getAllUserIdsForOaOrgSql(@Param("user") User user);

	/**
	 * 查询第一层级机构
	 * @return
	 */
	List<OaDepartment> findTopLevel();
	/**
	 * 根据社区id批量查询
	 * @param ids
	 * @return
	 */
	List<OaDepartment> batchSelectByIds(@Param(value = "ids") List<Long> ids);

	/**
	 * 根据用户id 查询用户与社区的关系
	 * @param userId
	 * @return
	 */
	List<OaDepartment> findOaDetailByUserRelId(@Param(value = "userId")Long userId);
}
