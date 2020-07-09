package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.LocationApply;
import com.bit.module.cbo.vo.LocationApplyPageVO;
import com.bit.module.cbo.vo.LocationApplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocationApplyDao {
	/**
	 * 根据居民id查询居民房屋认证申请记录
	 * @return
	 */
	List<LocationApply> getRecordByResidentId(@Param(value = "residentId") Long residentId);

	/**
	 * 添加住房审核记录
	 * @param locationApply
	 * @return
	 */
	int addRecord(LocationApply locationApply);

	/**
	 * 多参数查询
	 * @param locationApplyModelVO
	 * @return
	 */
	List<LocationApply> findByParam(LocationApplyVO locationApplyModelVO);

	/**
	 * 更新申请记录
	 * @param locationApplyModelVO
	 * @return
	 */
	int updateLocationApply(LocationApplyVO locationApplyModelVO);

	/**
	 * 认证审核 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	List<LocationApplyVO> listPage(LocationApplyPageVO locationApplyPageVO);

	/**
	 * 根据id查询申请记录
	 * @param id
	 * @return
	 */
	LocationApply getRecordById(@Param(value = "id") Long id);

	/**
	 * app端房屋审核记录查询
	 * @param locationApplyPageVO
	 * @return
	 */
	List<LocationApplyVO> appApplyListPage(LocationApplyPageVO locationApplyPageVO);

	/**
	 * 根据记录id查询 居民和申请记录的字段信息
	 * @return
	 */
	LocationApplyVO getRecordResidentInfoById(@Param(value = "id") Long id);
	/**
	 * 居委会app 房屋认证记录 分页查询
	 * @param locationApplyPageVO
	 * @return
	 */
	List<LocationApplyVO> appOrgListPage(LocationApplyPageVO locationApplyPageVO);
	/**
	 * 居委会app 返显房屋认证申请记录
	 * @param id
	 * @return
	 */
	LocationApplyVO appOrggetRecordResidentInfoById(@Param(value = "id") Long id);

	/**
	 * 批量添加房屋认证记录
	 * @param locationApplyList
	 * @return
	 */
	int batchAddLocationApplyRecords(@Param(value = "locationApplyList") List<LocationApply> locationApplyList);

	/**
	 * 根据居民id和社区id删除记录
	 * @param residentId
	 * @param orgId
	 * @return
	 */
	int delByResidentIdAndOrgId(@Param(value = "residentId") Long residentId, @Param(value = "orgId") Long orgId);

	/**
	 * 批量更新数据
	 * @param updateApplyList
	 * @return
	 */
	int batchUpdateRecords(@Param(value = "updateApplyList") List<LocationApply> updateApplyList);
}
