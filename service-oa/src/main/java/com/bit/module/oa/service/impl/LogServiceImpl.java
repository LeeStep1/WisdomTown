package com.bit.module.oa.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.dao.LogDao;
import com.bit.module.oa.service.LogService;
import com.bit.module.oa.vo.log.LogVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Log的Service实现类
 * @author codeGenerator
 *
 */
@Service("logService")
@Transactional
public class LogServiceImpl extends BaseService implements LogService{

	private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

	@Autowired
	private LogDao logDao;

	/**
	 * 根据条件查询Log
	 * @param logVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(LogVO logVO){
		PageHelper.startPage(logVO.getPageNum(), logVO.getPageSize());
		logVO.setOrderBy("create_at");
		logVO.setOrder("desc");
		List<Log> list = logDao.findByConditionPage(logVO);
		PageInfo<Log> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 通过主键查询单个Log
	 * @param id
	 * @return
	 */
	@Override
	public Log findById(Long id){
		return logDao.findById(id);
	}

	/**
	 * 保存Log
	 * @param log
	 */
	@Override
	public void add(Log log){
		logDao.add(log);
	}

	/**
	 * 删除Log
	 * @param id
	 */
	@Override
	public void delete(Long id){
		logDao.delete(id);
	}
}
