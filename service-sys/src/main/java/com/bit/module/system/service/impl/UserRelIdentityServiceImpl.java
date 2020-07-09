package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.UserRelIdentity;
import com.bit.module.system.dao.UserRelIdentityDao;
import com.bit.module.system.service.UserRelIdentityService;
import com.bit.module.system.vo.UserRelIdentityVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserRelIdentity的Service实现类
 * @author liqi
 *
 */
@Service("userRelIdentityService")
public class UserRelIdentityServiceImpl extends BaseService implements UserRelIdentityService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRelIdentityServiceImpl.class);
	
	@Autowired
	private UserRelIdentityDao userRelIdentityDao;
	
	/**
	 * 根据条件查询UserRelIdentity
	 * @param userRelIdentityVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(UserRelIdentityVO userRelIdentityVO){
		PageHelper.startPage(userRelIdentityVO.getPageNum(), userRelIdentityVO.getPageSize());
		List<UserRelIdentity> list = userRelIdentityDao.findByConditionPage(userRelIdentityVO);
		PageInfo<UserRelIdentity> pageInfo = new PageInfo<UserRelIdentity>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有UserRelIdentity
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<UserRelIdentity> findAll(String sorter){
		return userRelIdentityDao.findAll(sorter);
	}

	/**
	 * 通过主键查询单个UserRelIdentity
	 * @param id
	 * @return
	 */
	@Override
	public UserRelIdentity findById(Long id){
		return userRelIdentityDao.findById(id);
	}
	
	/**
	 * 保存UserRelIdentity
	 * @param userRelIdentity
	 */
	@Override
	@Transactional
	public void add(UserRelIdentity userRelIdentity){
		userRelIdentityDao.add(userRelIdentity);
	}

	/**
	 * 更新UserRelIdentity
	 * @param userRelIdentity
	 */
	@Override
	@Transactional
	public void update(UserRelIdentity userRelIdentity){
		userRelIdentityDao.update(userRelIdentity);
	}

	/**
	 * 删除UserRelIdentity
	 * @param ids
	 */
	@Override
	@Transactional
	public void batchDelete(List<Long> ids){
		userRelIdentityDao.batchDelete(ids);
	}

	/**
	 * 删除UserRelIdentity
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		userRelIdentityDao.delete(id,null,null);
	}

}
