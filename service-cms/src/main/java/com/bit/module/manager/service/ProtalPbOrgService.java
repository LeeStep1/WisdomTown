package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ProtalPbOrg;
import com.bit.module.manager.vo.ProtalPbOrgVO;

import java.util.List;
/**
 * ProtalPbOrg的Service
 * @author liuyancheng
 */
public interface ProtalPbOrgService {
	/**
	 * 根据条件查询ProtalPbOrg
	 * @param protalPbOrgVO
	 * @return
	 */
	BaseVo findByConditionPage(ProtalPbOrgVO protalPbOrgVO);
	/**
	 * 查询所有ProtalPbOrg
	 * @param sorter 排序字符串
	 * @return
	 */
	List<ProtalPbOrg> findAll(String sorter);
	/**
	 * 通过主键查询单个ProtalPbOrg
	 * @param id
	 * @return
	 */
	ProtalPbOrg findById(Long id);

	/**
	 * 新增党建组织下栏目
	 * @param protalPbOrg
	 */
	void add(ProtalPbOrg protalPbOrg);
	/**
	 * 更新ProtalPbOrg
	 * @param protalPbOrg
	 */
	void update(ProtalPbOrg protalPbOrg);
	/**
	 * 删除ProtalPbOrg
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 党建组织导航下栏目展示
	 * @author liyang
	 * @date 2019-05-24
	 * @param navigationId : 所属导航
	 * @param categoryId : 所属栏目
	 * @return : BaseVo
	 */
	BaseVo getPbOrgNavigationContend(Long navigationId,Long categoryId);

	/**
	 * 党建组织导航下栏目展示
	 * @author liyang
	 * @date 2019-05-24
	 * @return : BaseVo
	 */
	BaseVo getLeaderImage();
}
