package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.UserRelApp;
import com.bit.module.system.dao.UserRelAppDao;
import com.bit.module.system.service.UserRelAppService;
import com.bit.module.system.vo.UserRelAppVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserRelApp的Service实现类
 * @author liqi
 *
 */
@Service("UserRelAppService")
public class UserRelAppServiceImpl extends BaseService implements UserRelAppService {
	

	@Autowired
	private UserRelAppDao userRelAppDao;
	
	/**
	 * 根据条件查询UserRelApp
	 * @param UserRelAppVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(UserRelAppVO UserRelAppVO){
		PageHelper.startPage(UserRelAppVO.getPageNum(), UserRelAppVO.getPageSize());
		List<UserRelApp> list = userRelAppDao.findByConditionPage(UserRelAppVO);
		PageInfo<UserRelApp> pageInfo = new PageInfo<UserRelApp>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}


	/**
	 * 查询所有UserRelApp
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<UserRelApp> findAll(String sorter){
		return userRelAppDao.findAll(sorter);
	}

	/**
	 * 通过主键查询单个UserRelApp
	 * @param id
	 * @return
	 */
	@Override
	public UserRelApp findById(Long id){
		return userRelAppDao.findById(id);
	}
	
	/**
	 * 保存UserRelApp
	 * @param UserRelApp
	 */
	@Override
	@Transactional
	public void add(UserRelApp UserRelApp){
		userRelAppDao.add(UserRelApp);
	}

	/**
	 * 更新UserRelApp
	 * @param UserRelApp
	 */
	@Override
	@Transactional
	public void update(UserRelApp UserRelApp){
		userRelAppDao.update(UserRelApp);
	}

	/**
	 * 删除UserRelApp
	 * @param ids
	 */
	@Override
	@Transactional
	public void batchDelete(List<Long> ids){
		userRelAppDao.batchDelete(ids);
	}

	/**
	 * 批量保存
	 * @param list
	 */
	@Override
	@Transactional
	public void batchAdd(List<UserRelApp> list) {
		userRelAppDao.batchAdd(list);
	}

	/**
	 * 删除UserRelApp
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		userRelAppDao.delete(id);
	}

}
