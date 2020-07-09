package com.bit.module.system.dao;

import com.bit.module.system.bean.Identity;
import com.bit.module.system.vo.IdentityVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

/**
 * Identity管理的Dao
 * @author liqi
 *
 */
public interface IdentityDao {
	/**
	 * 根据条件查询Identity
	 * @param identityVO
	 * @return
	 */
	public List<Identity> findByConditionPage(IdentityVO identityVO);

	/**
	 * 业务分页 根据条件查询Identity
	 * @param identityVO
	 * @return
	 */
	public List<Identity> findRealPage(IdentityVO identityVO);

	/**
	 * 查询所有Identity
	 * @return
	 */
	public List<Identity> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个Identity
	 * @param id	 	 
	 * @return
	 */
	public Identity findById(@Param(value = "id") Long id);

	/**
	 * 保存Identity
	 * @param identity
	 */
	public void add(Identity identity);

	/**
	 * 更新Identity
	 * @param identity
	 */
	public void update(Identity identity);

	/**
	 * 删除Identity
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除Identity
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据用户id 查询身份
	 * @param userId
	 * @return
	 */
	List<Identity> findByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据appID查询identityList
	 * @param appId
	 * @return
	 */
    List<Identity> findIdentListByAppId(@Param(value = "appId")Long appId,@Param(value = "acquiesce")Integer acquiesce );

	/**
	 * 根据条件查询所有
	 * @param identity
	 * @return
	 */
	List<Identity> findAllByParam(Identity identity);

	/**
	 * 根据默认身份字段和应用id 校验  统计查询
	 * @param identity
	 * @return
	 */
    int findByAppIdAndAcqu(Identity identity);

	int findByAppIdAndName(Identity identity);

	/**
	 * 根据应用id 查询统计
	 * @param appId
	 * @return
	 */
	int findCountbyAppId(@Param(value = "appId")Integer appId);

	/**
	 * 根据默认身份字段和应用id查询
	 * @param identity
	 * @return
	 */
	Identity findByAppIdAndAcquiesce(Identity identity);

	/**
	 * 根据APPId查询默认身份
	 * @param appId
	 * @acquiesce 默认身份
	 * @return
	 */
	Identity getDefaultIdentityByAppIdSql(@Param("appId")Long appId,@Param("acquiesce") int acquiesce);
}
