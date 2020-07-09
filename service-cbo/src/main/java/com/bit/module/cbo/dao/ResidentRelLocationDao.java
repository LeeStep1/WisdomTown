package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.ResidentRelLocation;
import com.bit.module.cbo.vo.ResidentVO;
import com.bit.module.cbo.vo.ResidentRelLocationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResidentRelLocationDao {
	/**
	 * 多参数查询
	 * @param residentRelLocation
	 * @return
	 */
	List<ResidentRelLocation> findByParam(ResidentRelLocation residentRelLocation);

	/**
	 * 多参数社区id集合批量查询
	 * @param residentRelLocationVO
	 * @return
	 */
	List<ResidentRelLocation> findByParamBatchOrgIds(ResidentRelLocationVO residentRelLocationVO);


	/**
	 * 多参数查询社区
	 * @param residentRelLocation
	 * @return
	 */
	List<ResidentRelLocationVO> findByParamWithLocation(ResidentRelLocation residentRelLocation);

	/**
	 * 新增记录
	 * @param residentRelLocation
	 * @return
	 */
	int add(ResidentRelLocation residentRelLocation);

	/**
	 * 根据居民id和社区id删除记录
	 * @param residentId
	 * @param orgId
	 * @return
	 */
	int delByResidentIdAndOrgId(@Param(value = "residentId")Long residentId,@Param(value = "orgId")Long orgId);

	/**
	 * 批量新增居民住房信息
	 * @param residentRelLocations
	 * @return
	 */
	int batchAddResidentRelLocation(@Param(value = "residentRelLocations") List<ResidentRelLocation> residentRelLocations);

	/**
	 * 根据id删除记录
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id") Long id);

	/**
	 * 根据id查询居民住房关联关系记录
	 * @param id
	 * @return
	 */
	ResidentRelLocation getResidentRelLocationById(@Param(value = "id") Long id);

	/**
	 * 根据房屋id查询关联关系
	 * @param roomIds
	 * @return
	 */
	List<ResidentRelLocation> batchSelectRecordByAddressId(@Param(value = "roomIds") List<Long> roomIds);

	/**
	 * 根据房屋id查询住户信息
	 * @param residentRelLocationModelVO
	 * @return
	 */
	List<ResidentVO> getResidentInfoByAddressId(ResidentRelLocationVO residentRelLocationModelVO);

	/**
	 * 根据居民id查询住房信息
	 * @param residentId
	 * @return
	 */
	List<ResidentRelLocationVO> getAddressByResidentId(@Param(value = "residentId") Long residentId,@Param(value = "communityId")Long communityId);
}
