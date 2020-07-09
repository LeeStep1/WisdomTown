package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalCategory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * PortalCategory的Service
 * @author liuyancheng
 */
public interface PortalCategoryService {
	/**
	 * 根据条件查询PortalCategory
	 * @param portalCategory
	 * @return
	 */
	BaseVo findByConditionPage(PortalCategory portalCategory);
	/**
	 * 查询所有PortalCategory
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PortalCategory> findAll(String sorter);
	/**
	 * 通过主键查询单个PortalCategory
	 * @param id
	 * @return
	 */
	PortalCategory findById(Long id);

	/**
	 * 保存PortalCategory
	 * @param portalCategory
	 */
	BaseVo add(PortalCategory portalCategory);
	/**
	 * 更新PortalCategory
	 * @param portalCategory
	 */
	void update(PortalCategory portalCategory);
	/**
	 * 删除PortalCategory
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 新增个人/法人服务类型
	 * @author liyang
	 * @date 2019-05-13
	 * @param portalCategory :
	 */
	BaseVo addServiceType(PortalCategory portalCategory);
    /**
     * 个人服务 or 法人服务类型分页查询
     * @author chenduo
     * @param portalCategory
     * @return
     */
	BaseVo serviceTypeListPage(PortalCategory portalCategory);
    /**
     * 个人服务 or 法人服务 批量更新服务排序
     * @param portalCategoryList
     * @return
     */
    BaseVo serviceSort(@RequestParam List<PortalCategory> portalCategoryList);
}
