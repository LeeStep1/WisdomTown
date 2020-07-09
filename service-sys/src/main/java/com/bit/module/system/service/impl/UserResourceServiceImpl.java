package com.bit.module.system.service.impl;

import java.util.List;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.module.system.bean.UserResource;
import com.bit.module.system.dao.UserResourceDao;
import com.bit.module.system.service.UserResourceService;
import com.bit.module.system.vo.UserResourceQueryVO;
import com.bit.module.system.vo.UserResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.base.vo.BaseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bit.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserResource的Service实现类
 * @author liuyancheng
 *
 */
@Service("userResourceService")
public class UserResourceServiceImpl extends BaseService implements UserResourceService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserResourceServiceImpl.class);
	
	@Autowired
	private UserResourceDao userResourceDao;
	
	/**
	 * 根据条件查询UserResource
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(UserResourceVO userResourceVO){
		PageHelper.startPage(userResourceVO.getPageNum(), userResourceVO.getPageSize());
		List<UserResource> list = userResourceDao.findByConditionPage(userResourceVO);
		PageInfo<UserResource> pageInfo = new PageInfo<UserResource>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有UserResource
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<UserResource> findAll(String sorter){
		return userResourceDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个UserResource
	 * @param id
	 * @return
	 */
	@Override
	public UserResource findById(Long id){
		return userResourceDao.findById(id);
	}
	
	/**
	 * 保存UserResource
	 * @param userResourceVO
	 */
	@Override
	@Transactional
	public void add(UserResourceVO userResourceVO){
		//批量新增
		UserInfo userInfo = getCurrentUserInfo();
		userResourceVO.setUserId(userInfo.getId());
		// 判断当前用户是否存在8条数据,如存在，则不添加
		List<Long> resourceIds = userResourceVO.getResourceIds();
		if (resourceIds.size() <= 8){
			//先删除再添加
			userResourceDao.deleteByUserId(userInfo.getId());
			userResourceDao.batchAdd(userResourceVO);
		}else {
			throw new BusinessException("最多可添加8个常用菜单！");
		}
	}
	/**
	 * 更新UserResource
	 * @param userResourceVO
	 */
	@Override
	@Transactional
	public void update(UserResourceVO userResourceVO){
		// 先根据userId删除，然后在添加
		UserInfo userInfo = getCurrentUserInfo();
		userResourceVO.setUserId(userInfo.getId());
		userResourceDao.deleteByUserId(userInfo.getId());
		userResourceDao.batchAdd(userResourceVO);
	}
	/**
	 * 删除UserResource
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		userResourceDao.delete(id);
	}

	@Override
	public BaseVo findByUserId() {
		UserInfo userInfo = getCurrentUserInfo();
		List<UserResourceQueryVO> list = userResourceDao.findByUserId(userInfo.getId());
		BaseVo baseVo = new BaseVo();
		baseVo.setData(list);
		return baseVo;
	}
}
