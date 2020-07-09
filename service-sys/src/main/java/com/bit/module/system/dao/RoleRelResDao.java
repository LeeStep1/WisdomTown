package com.bit.module.system.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.RoleRelRes;
import com.bit.module.system.vo.RoleRelResVO;
import org.springframework.stereotype.Repository;

/**
 * RoleRelRes管理的Dao
 * @author 
 *
 */
@Repository
public interface RoleRelResDao {
	/**
	 * 根据条件查询RoleRelRes
	 * @param roleRelResVO
	 * @return
	 */
	List<RoleRelRes> findByConditionPage(RoleRelResVO roleRelResVO);
	/**
	 * 查询所有RoleRelRes
	 * @return
	 */
	List<RoleRelRes> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个RoleRelRes
	 * @param id	 	 
	 * @return
	 */
	RoleRelRes findById(@Param(value = "id") Long id);
	/**
	 * 保存RoleRelRes
	 * @param roleRelRes
	 */
	void add(RoleRelRes roleRelRes);
	/**
	 * 更新RoleRelRes
	 * @param roleRelRes
	 */
	void update(RoleRelRes roleRelRes);
	/**
	 * 删除RoleRelRes
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除RoleRelRes
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);
}
