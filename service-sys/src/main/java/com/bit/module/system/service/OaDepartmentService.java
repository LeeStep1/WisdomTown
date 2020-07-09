package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.vo.OaDepartmentResultVO;
import com.bit.module.system.vo.OaDepartmentVO;

import java.util.List;
/**
 * OaDepartment的Service
 * @author codeGenerator
 */
public interface OaDepartmentService {
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	BaseVo findByConditionPage(OaDepartmentVO oaDepartmentVO);
	/**
	 * 查询所有OaDepartment
	 * @param sorter 排序字符串
	 * @return
	 */
	List<OaDepartment> findAll(String sorter);
	/**
	 * 通过主键查询单个OaDepartment
	 * @param id
	 * @return
	 */
	OaDepartment findById(Long id);
	OaDepartmentResultVO findResultById(Long id);

	/**
	 * 保存OaDepartment
	 * @param oaDepartmentResultVO
	 */
	void add(OaDepartmentResultVO oaDepartmentResultVO);
	/**
	 * 更新OaDepartment
	 * @param oaDepartmentResultVO
	 */
	void updateResult(OaDepartmentResultVO oaDepartmentResultVO);
	/**
	 * 删除OaDepartment
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据用户userId 查询
	 * @param userId
	 * @return
	 */
	BaseVo findByUserId(Long userId);

	/**
	 * 校验组织编号是否唯一
	 * @param resultVO
	 * @return
	 */
	BaseVo checkPcodeUnique(OaDepartmentResultVO resultVO);

	/**
	 * 校验组织是否有下级
	 * @param resultVO
	 * @return
	 */
	BaseVo checkdown(OaDepartmentResultVO resultVO);

	/**
	 * 查询组织结构明细
	 * @param id 要查询的组织ID
	 * @return
	 */
	BaseVo findOaDepartment(Long id);

	/**
	 * 批量查询政务组织下的用户
	 * @author liyang
	 * @date 2019-04-04
	 * @param targetIds :
	 */
	BaseVo getAllUserIdsByOaOrgIds(Long[] targetIds);

	/**
	 * 获取党建组织下所有用户
	 * @author liyang
	 * @date 2019-04-09
	 * @return : List<Long> ：用户ID集合
	 */
	List<Long> getAllUserIdsForOaOrg();
	/**
	 * 获取社区信息
	 * @return
	 */
	BaseVo getCommunity();
	/**
	 * 根据社区id批量查询
	 * @param ids
	 * @return
	 */
	List<OaDepartment> batchSelectByIds(List<Long> ids);

	/**
	 * 根据当前用户查询社区
	 * @return
	 */
	BaseVo getCommunityByToken();
}
