package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Application;
import com.bit.module.oa.vo.application.ApplicationVO;

import java.util.List;

/**
 * Application的Service
 * @author codeGenerator
 */
public interface ApplicationService {
	/**
	 * 根据条件查询Application
	 * @param applicationVO
	 * @return
	 */
	BaseVo findByConditionPage(ApplicationVO applicationVO);
	/**
	 * 通过主键查询单个Application
	 * @param id
	 * @return
	 */
	Application findById(Long id);

	/**
	 * 批量保存Application
	 * @param applications
	 */
	void batchAdd(List<Application> applications);
	/**
	 * 保存Application
	 * @param application
	 */
	void add(Application application);
	/**
	 * 更新Application
	 * @param application
	 */
	void update(Application application);
	/**
	 * 删除Application
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Application
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	void audit(Long executeId, Integer status);

	void reject(Application application);
}
