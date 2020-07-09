package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.vo.log.LogVO;

/**
 * Log的Service
 * @author codeGenerator
 */
public interface LogService {
	/**
	 * 根据条件查询Log
	 * @param logVO
	 * @return
	 */
	BaseVo findByConditionPage(LogVO logVO);
	/**
	 * 通过主键查询单个Log
	 * @param id
	 * @return
	 */
	Log findById(Long id);
	/**
	 * 保存Log
	 * @param log
	 */
	void add(Log log);
	/**
	 * 删除Log
	 * @param id
	 */
	void delete(Long id);
}
