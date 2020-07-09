package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.IdentityRelRole;
import com.bit.module.system.dao.IdentityRelRoleDao;
import com.bit.module.system.service.IdentityRelRoleService;
import com.bit.module.system.vo.IdentityRelRoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IdentityRelRole的Service实现类
 * @author liqi
 *
 */
@Service("identityRelRoleService")
public class IdentityRelRoleServiceImpl extends BaseService implements IdentityRelRoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(IdentityRelRoleServiceImpl.class);
	
	@Autowired
	private IdentityRelRoleDao identityRelRoleDao;

	/**
	 * 根据条件查询IdentityRelRole
	 * @param identityRelRoleVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(IdentityRelRoleVO identityRelRoleVO){
		PageHelper.startPage(identityRelRoleVO.getPageNum(), identityRelRoleVO.getPageSize());
		List<IdentityRelRole> list = identityRelRoleDao.findByConditionPage(identityRelRoleVO);
		PageInfo<IdentityRelRole> pageInfo = new PageInfo<IdentityRelRole>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有IdentityRelRole
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<IdentityRelRole> findAll(String sorter){
		return identityRelRoleDao.findAll(sorter);
	}

	/**
	 * 通过主键查询单个IdentityRelRole
	 * @param id
	 * @return
	 */
	@Override
	public IdentityRelRole findById(Long id){
		return identityRelRoleDao.findById(id);
	}
	
	/**
	 * 保存IdentityRelRole
	 * @param identityRelRole
	 */
	@Override
	@Transactional
	public void add(IdentityRelRole identityRelRole){
		identityRelRoleDao.add(identityRelRole);
	}

	/**
	 * 更新IdentityRelRole
	 * @param identityRelRole
	 */
	@Override
	@Transactional
	public void update(IdentityRelRole identityRelRole){
		identityRelRoleDao.update(identityRelRole);
	}

	/**
	 * 删除IdentityRelRole
	 * @param ids
	 */
	@Override
	@Transactional
	public void batchDelete(List<Long> ids){
		identityRelRoleDao.batchDelete(ids);
	}

	/**
	 * 删除IdentityRelRole
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		identityRelRoleDao.delete(id);
	}

}
