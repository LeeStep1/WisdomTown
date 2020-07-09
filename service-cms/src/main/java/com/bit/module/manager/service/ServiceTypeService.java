package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.vo.ServiceTypeVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * ServiceType的Service
 * @author liuyancheng
 */
public interface ServiceTypeService {
	/**
	 * 查询所有ServiceType
	 * @return
	 */
	List<ServiceType> findAll();
	/**
	 * 通过主键查询单个ServiceType
	 * @param id
	 * @return
	 */
	ServiceType findById(Long id);

	/**
	 * 保存ServiceType
	 * @param serviceTypeVo
	 */
	void add(ServiceTypeVO serviceTypeVo, HttpServletRequest request);
	/**
	 * 更新ServiceType
	 * @param serviceType
	 */
	void update(ServiceType serviceType);
	/**
	 * 删除ServiceType
	 * @param id
	 */
	void delete(Long id,HttpServletRequest request);
    /**
     * 根据栏目ID查询服务类型
     * @param categoryId
     * @return
     */
	BaseVo queryBycategoryId(Long categoryId);
    /**
     * 办事指南服务类型排序
     * @param serviceTypeList
     * @return
     */
	BaseVo serviceTypeSort(List<ServiceType> serviceTypeList);
}
