package com.bit.module.manager.dao;

import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.vo.ServiceTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ServiceType管理的Dao
 * @author liuyancheng
 *
 */
public interface ServiceTypeDao {
	/**
	 * 查询所有ServiceType
	 * @return
	 */
	public List<ServiceType> findAll(@Param(value = "sorter") String sorter,@Param(value = "delStatus")Integer delStatus);
	/**
	 * 通过主键查询单个ServiceType
	 * @param id	 	 
	 * @return
	 */
	public ServiceType findById(@Param(value = "id") Long id);
	/**
	 * 保存ServiceType
	 * @param serviceTypeVo
	 */
	public void add(ServiceTypeVO serviceTypeVo);
	/**
	 * 更新ServiceType
	 * @param serviceType
	 */
	public void update(ServiceType serviceType);
	/**
	 * 删除ServiceType
	 * @param serviceType
	 */
	public void delete(@Param(value = "serviceType") ServiceType serviceType);

	/**
	 * 根据栏目ID查询服务类型
	 * @param categoryId
	 * @return
	 */
	List<ServiceType> queryByCategoryId(@Param(value = "categoryId") Long categoryId);
    /**
     * 服务类型排序
     * @param list
     * @return
     */
    void batchUpdateRank(@Param(value = "list") List<ServiceType> list);

}
