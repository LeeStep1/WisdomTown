package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.vo.CommunityVO;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 19:37
 **/
public interface CommunityService {
	/**
	 * 批量根据社区id查询小区
	 * @param orgIds
	 * @return
	 */
	List<Community> batchSelectByOrgIds(List<Long> orgIds);

	/**
	 * app端展示所有的小区 不需要分页
	 * @return
	 */
	BaseVo appListPage();

	/**
	 * 新增小区
	 * @author liyang
	 * @date 2019-07-18
	 * @param community : 新增小区详情
	 * @return : BaseVo
	 */
	BaseVo add(Community community);

	/**
	 * 修改小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param community : 修改的详情
	 * @return : BaseVo
	*/
	BaseVo modify(Community community);

	/**
	 * 查询小区列表（分页）
	 * @author liyang
	 * @date 2019-07-19
	 * @param communityVO : 查询条件
	 * @return : BaseVo
	 */
	BaseVo findAll(CommunityVO communityVO);

	/**
	 * 根据ID 删除小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param id : 小区ID
	 * @return : BaseVo
	 */
	BaseVo delete(Long id);

	/**
	 * 根据ID查询明细
	 * @author liyang
	 * @date 2019-07-22
	 * @param id : 查询的明细
	 * @return : BaseVo
	 */
	BaseVo findById(Long id);

	/**
	 * 获取物业公司关联小区
	 * @author liyang
	 * @date 2019-07-23
	 * @param id : 物业公司ID
	 * @return : BaseVo
	*/
	BaseVo findCommunityByCompanyId(Long id);
}
