package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.ResidentExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResidentExtendDao {
	/**
	 * 多参数查询
	 * @return
	 */
	List<ResidentExtend> findByParam(ResidentExtend extend);

	/**
	 * 根据居民id和社区id删除记录
	 * @param residentId
	 * @param orgId
	 * @return
	 */
	int delByResidentIdAndOrgId(@Param(value = "residentId")Long residentId,@Param(value = "orgId")Long orgId);

	/**
	 * 新增数据
	 * @param residentExtend
	 * @return
	 */
	int add(ResidentExtend residentExtend);

	/**
	 * 批量新增居民扩展信息
	 * @param residentExtendList
	 * @return
	 */
	int batchAddResidentExtend(@Param(value = "residentExtendList") List<ResidentExtend> residentExtendList);
}
