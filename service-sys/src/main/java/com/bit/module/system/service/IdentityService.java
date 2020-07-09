package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Identity;
import com.bit.module.system.vo.IdentityVO;

import java.util.List;
/**
 * Identity的Service
 * @author liqi
 */
public interface IdentityService {
	/**
	 * 根据条件查询Identity
	 * @param identityVO
	 * @return
	 */
	BaseVo findByConditionPage(IdentityVO identityVO);

	/**
	 * 根据条件查询Identity   业务分页
	 * @param identityVO
	 * @return
	 */
	BaseVo findRealPage(IdentityVO identityVO);

	/**
	 * 查询所有Identity
	 * @param sorter 排序字符串
	 * @return
	 */
	List<Identity> findAll(String sorter);

	/**
	 * 通过主键查询单个Identity
	 * @param id
	 * @return
	 */
	Identity findById(Long id);


    BaseVo checkNameUnique(Identity identity);

    /**
	 * 保存Identity
	 * @param identity
	 */
	void add(Identity identity);

	/**
	 * 更新Identity
	 * @param identity
	 */
	void update(Identity identity);

	/**
	 * 删除Identity
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除Identity
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 根据主键ID查询Identity  业务查询
	 * @param id
	 * @return
	 */
	Identity findBykey(Long id);

	/**
	 * 校验删除  大于0不能删除  ---查询和身份有关系的数据
	 * @param id
	 * @return
	 */
	BaseVo checkCountNexus(Long id);

	/**
	 * 根据appID查询identityList
	 * @param appId
	 * @return
	 */
    List<Identity> findIdentListByAppId(Long appId);

	/**
	 * 根据条件查询all
	 * @param identity
	 */
	List<Identity> findAllByParam(Identity identity);

	/**
	 * 校验默认身份  --一个应用下只有一个默认身份
	 * @param identity
	 * @return
	 */
    BaseVo checkAcquiesce(Identity identity);

	/**
	 * 根据appID查询默认身份
	 * @param appId
	 * @return
	 */
	Identity getDefaultIdentityByAppId(Long appId);

}
