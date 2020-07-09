package com.bit.module.system.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.OaOrganization;
import com.bit.module.system.vo.OaOrganizationVO;
import org.springframework.stereotype.Repository;

/**
 * OaOrganization管理的Dao
 * @author 
 *
 */
@Repository
public interface OaOrganizationDao {
	/**
	 * 根据条件查询OaOrganization
	 * @param oaOrganizationVO
	 * @return
	 */
	List<OaOrganization> findByConditionPage(OaOrganizationVO oaOrganizationVO);
	/**
	 * 查询所有OaOrganization
	 * @return
	 */
	List<OaOrganization> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个OaOrganization
	 * @param id	 	 
	 * @return
	 */
	OaOrganization findById(@Param(value = "id") Long id);
	/**
	 * 保存OaOrganization
	 * @param oaOrganization
	 */
	void add(OaOrganization oaOrganization);
	/**
	 * 更新OaOrganization
	 * @param oaOrganization
	 */
	void update(OaOrganization oaOrganization);
	/**
	 * 删除OaOrganization
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);
}
