package com.bit.module.system.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.UserRelOaOrg;
import com.bit.module.system.vo.UserRelOaOrgVO;
import org.springframework.stereotype.Repository;

/**
 * UserRelOaOrg管理的Dao
 * @author 
 *
 */
@Repository
public interface UserRelOaOrgDao {

	/**
	 * 根据条件查询UserRelOaOrg
	 * @param userRelOaOrgVO
	 * @return
	 */
	List<UserRelOaOrg> findByConditionPage(UserRelOaOrgVO userRelOaOrgVO);

	/**
	 * 查询所有UserRelOaOrg
	 * @return
	 */
	List<UserRelOaOrg> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelOaOrg
	 * @param id	 	 
	 * @return
	 */
	UserRelOaOrg findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelOaOrg
	 * @param userRelOaOrg
	 */
	void add(UserRelOaOrg userRelOaOrg);

	/**
	 * 更新UserRelOaOrg
	 * @param userRelOaOrg
	 */
	void update(UserRelOaOrg userRelOaOrg);

	/**
	 * 删除UserRelOaOrg
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 删除UserRelOaOrg
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

}
