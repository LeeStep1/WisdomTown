package com.bit.module.system.dao;

import java.util.List;

import com.bit.module.system.bean.OrgAndName;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.UserRelPbOrg;
import com.bit.module.system.vo.UserRelPbOrgVO;
import org.springframework.stereotype.Repository;

/**
 * UserRelPbOrg管理的Dao
 * @author 
 *
 */
@Repository
public interface UserRelPbOrgDao {

	/**
	 * 根据条件查询UserRelPbOrg
	 * @param userRelPbOrgVO
	 * @return
	 */
	List<UserRelPbOrg> findByConditionPage(UserRelPbOrgVO userRelPbOrgVO);

	/**
	 * 查询所有UserRelPbOrg
	 * @return
	 */
	List<UserRelPbOrg> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个UserRelPbOrg
	 * @param id	 	 
	 * @return
	 */
	UserRelPbOrg findById(@Param(value = "id") Long id);

	/**
	 * 保存UserRelPbOrg
	 * @param userRelPbOrg
	 */
	void add(UserRelPbOrg userRelPbOrg);

	/**
	 * 更新UserRelPbOrg
	 * @param userRelPbOrg
	 */
	void update(UserRelPbOrg userRelPbOrg);

	/**
	 * 批量删除
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 删除UserRelPbOrg
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 批量保存
	 * @param userRelPbOrgs
	 */
    void batchAdd(@Param(value = "list")List<UserRelPbOrg> userRelPbOrgs);

	/**
	 * 更新用户id 删除
	 * @param userId
	 */
    void delByUserId(@Param(value = "userId") Long userId);

	/**
	 * 根据组织id查询统计
	 * @param pborgId
	 * @return
	 */
	int findCountByPbId(@Param(value = "pborgId")Long pborgId);

	/**
	 * 根据用户id查询
	 * @param userId
	 * @return
	 */
    List<UserRelPbOrg> findByUserId(@Param(value = "userId")Long userId);
	/**
	 * 根据组织id  姓名 查询政务用户
	 * @param orgAndName
	 * @return
	 */
	List<User> getPbUserByName(OrgAndName orgAndName);

	/**
	 * 根据参数查询所有
	 * @param userRelPbOrg
	 * @return
	 */
	List<UserRelPbOrg> findAllByParam(UserRelPbOrg userRelPbOrg);

	/**
	 * 批量查询党建管理员的身份证
	 * @param orgIds
	 * @return
	 */
	List<String> findUserIdCardByOrgIds(@Param(value = "orgIds") List<Long> orgIds);

    /**
     * 根据党组织id查询用户身份证
     * @param orgId
     * @return
     */
	List<String> queryUserIdCardByOrgId(@Param(value = "orgId") Long orgId);

	/**
	 * 批量查询党建管理员信息
	 * @param orgIds
	 * @return
	 */
	List<UserVO> findUserByOrgIds(@Param(value = "orgIds") List<Long> orgIds);

	/**
	 * 根据组织IDS获取用户ID
	 * @param orgIds
	 * @return
	 */
	List<Long> queryUserIdByOrgids(@Param(value = "orgIds") List<String> orgIds);

}
