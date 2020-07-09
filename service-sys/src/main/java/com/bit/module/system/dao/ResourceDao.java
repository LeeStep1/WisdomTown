package com.bit.module.system.dao;

import com.bit.module.system.bean.Resource;
import com.bit.module.system.vo.ResourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Resource管理的Dao
 * @author liqi
 *
 */
public interface ResourceDao {
	/**
	 * 根据条件查询Resource
	 * @param resourceVO
	 * @return
	 */
	public List<Resource> findByConditionPage(ResourceVO resourceVO);

	/**
	 * 通过主键查询单个Resource
	 * @param id	 	 
	 * @return
	 */
	public Resource findById(@Param(value = "id") Long id);

	/**
	 * 保存Resource
	 * @param resource
	 */
	public void add(Resource resource);

	/**
	 * 更新Resource
	 * @param resource
	 */
	public void update(Resource resource);

	/**
	 * 批量删除Resource
	 * @param ids
	 */
	public void batchDelete(@Param(value = "ids") List<Long> ids);

	/**
	 * 删除Resource
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据参数查询所有
	 * @param resource
	 * @return
	 */
	List<Resource> findAllByParam(Resource resource);
	List<Resource> findResourceByRole(Resource resource);


	/**
	 * 根据身份查询资源
	 * @param resource
	 * @return List<Resource>
	 */
	List<Resource> findResourcesByidentity(Resource resource);

	/**
	 * 根据pid查询记录
	 * @param id
	 * @return
	 */
	List<Resource> findByPid(@Param(value = "id")Long id);

	/**
	 * 查询所有的记录
	 * @return
	 */
	List<Resource> finaAllRecords();
}
