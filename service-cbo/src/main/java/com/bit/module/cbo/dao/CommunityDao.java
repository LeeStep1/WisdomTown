package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.vo.CommunityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 10:01
 **/
public interface CommunityDao {
	/**
	 * 批量根据id查询小区信息
	 * @param communityIds
	 * @return
	 */
	List<Community> batchSelectByIds(@Param(value = "communityIds")List<Long> communityIds);
	/**
	 * 批量根据社区id查询小区
	 * @param orgIds
	 * @return
	 */
	List<Community> batchSelectByOrgIds(@Param(value = "orgIds") List<Long> orgIds);

	/**
	 * 根据物业公司ID查询该公司绑定小区个数
	 * @param companyId 物业ID
	 * @return
	 */
	Integer communityByPmcCountSql(Long companyId);
	/**
	 * app端展示所有的小区 不需要分页
	 * @return
	 */
	List<CommunityVO> appListPage(@Param(value = "residentId") Long residentId);

	/**
	 * 查询所有的小区
	 * @return
	 */
	List<Community> getAllCommunity();
	
	/**
	 * 新增小区
	 * @param community
	 */
	void add(@Param("community") Community community);

	/**
	 * 修改小区
	 * @param community
	 */
	void modify(@Param("community") Community community);

	/**
	 * 查询小区列表
	 * @param communityVO
	 * @return
	 */
	List<Community> findAllSql(@Param("communityVO") CommunityVO communityVO);

	/**
	 * 查询该小区生成楼宇个数
	 * @param id
	 * @return
	 */
	Integer locationCountByCommunityIdSql(Long id);

	/**
	 * 根据ID删除小区
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据社区id查询小区
	 * @param orgId
	 * @return
	 */
	List<Community> getCommunityByOrgId(@Param(value = "orgId")Long orgId);

	/**
	 * 根据id查询记录
	 * @param id
	 * @return
	 */
	Community getCommunityById(@Param(value = "id")Long id);

	/**
	 * 根据小区ID查询所属社区
	 * @return
	 */
	List<PmcStaffRelCommunity> findOrgIdsByCommunityIdSql(@Param("communityIds") List<Long> communityIds);

	/**
	 * 根据id查询明细
	 * @param id
	 * @return
	 */
	Community getCommunityDetailById(@Param(value = "id")Long id);

	/**
	 * 根据物业公司获取所属小区
	 * @param companyId
	 * @return
	 */
	List<Community> getCommunityByCompanyId(@Param(value = "companyId")Long companyId);

	/**
	 * 小区名称去重
	 * @author liyang
	 * @date 2019-07-23
	 * @param name :
	 * @return : Integer
	*/
	Integer countByNameSql(@Param("name") String name);

	/**
	 * 小区名称去重（编辑时使用）
	 * @author liyang
	 * @date 2019-07-23
	 * @param name :
	 * @return : Integer
	 */
	Integer modifyCountByNameSql(@Param("name") String name,@Param("id") Long id);

}
