package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Resource;
import com.bit.module.system.vo.ResourceVO;

import java.util.List;
/**
 * Resource的Service
 * @author liqi
 */
public interface ResourceService {
	/**
	 * 根据条件查询Resource
	 * @param resourceVO
	 * @return
	 */
	BaseVo findByConditionPage(ResourceVO resourceVO);

	/**
	 * 通过主键查询单个Resource
	 * @param id
	 * @return
	 */
	Resource findById(Long id);

	/**
	 * 保存Resource
	 * @param resource
	 */
	void add(Resource resource);

	/**
	 * 更新Resource
	 * @param resource
	 */
	void update(Resource resource);

	/**
	 * 删除Resource
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 批量删除Resource
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 查询所有---树---根据条件
	 * @param resource
	 * @return
	 */
    List<Resource> findAllTreeByParam(ResourceVO resource);

	/**
	 * 查询树 根据条件 有用户权限条件
	 * @param resource
	 * @return
	 */
	List<Resource> findTreeByParam(Resource resource);

	/**
	 * 根据参数 查询所有
	 * @param resource
	 * @return
	 */
    List<Resource> findAllByParam(Resource resource);

	/**
	 * 查看角色权限
	 * @param resource
	 * @return
	 */
    List<Resource> findRolePermssion(Resource resource);


	/**
	 * 查看身份下关联的资源信息
	 * @param resource（appId,terminalId,identityId ）
	 * @return List<Resource>
	 */
	List<Resource> findResourcesByidentity(Resource resource);

}
