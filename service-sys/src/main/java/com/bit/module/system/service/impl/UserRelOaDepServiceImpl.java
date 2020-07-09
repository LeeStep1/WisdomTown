package com.bit.module.system.service.impl;

import java.util.List;

import com.bit.module.system.bean.UserRelOaDep;
import com.bit.module.system.dao.UserRelOaDepDao;
import com.bit.module.system.service.UserRelOaDepService;
import com.bit.module.system.vo.UserRelOaDepVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.base.vo.BaseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bit.base.service.BaseService;

/**
 * UserRelOaDep的Service实现类
 * @author codeGenerator
 *
 */
@Service("userRelOaDepService")
public class UserRelOaDepServiceImpl extends BaseService implements UserRelOaDepService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRelOaDepServiceImpl.class);
	
	@Autowired
	private UserRelOaDepDao userRelOaDepDao;
	
	/**
	 * 根据条件查询UserRelOaDep
	 * @param userRelOaDepVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(UserRelOaDepVO userRelOaDepVO){
		PageHelper.startPage(userRelOaDepVO.getPageNum(), userRelOaDepVO.getPageSize());
		List<UserRelOaDep> list = userRelOaDepDao.findByConditionPage(userRelOaDepVO);
		PageInfo<UserRelOaDep> pageInfo = new PageInfo<UserRelOaDep>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有UserRelOaDep
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<UserRelOaDep> findAll(String sorter){
		return userRelOaDepDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个UserRelOaDep
	 * @param id
	 * @return
	 */
	@Override
	public UserRelOaDep findById(Long id){
		return userRelOaDepDao.findById(id);
	}
	
	/**
	 * 保存UserRelOaDep
	 * @param userRelOaDep
	 */
	@Override
	public void add(UserRelOaDep userRelOaDep){
		userRelOaDepDao.add(userRelOaDep);
	}
	/**
	 * 更新UserRelOaDep
	 * @param userRelOaDep
	 */
	@Override
	public void update(UserRelOaDep userRelOaDep){
		userRelOaDepDao.update(userRelOaDep);
	}
	/**
	 * 删除UserRelOaDep
	 * @param id
	 */
	@Override
	public void delete(Long id){
		userRelOaDepDao.delete(id);
	}
}
