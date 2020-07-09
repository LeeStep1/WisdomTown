package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Risk;
import com.bit.module.oa.vo.risk.RiskExportQO;
import com.bit.module.oa.vo.risk.RiskExportVO;
import com.bit.module.oa.vo.risk.RiskVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Risk管理的Dao
 * @author 
 *
 */
@Repository
public interface RiskDao {
	/**
	 * 根据条件查询Risk
	 * @param riskVO
	 * @return
	 */
	List<Risk> findByConditionPage(RiskVO riskVO);
	/**
	 * 查询所有Risk
	 * @return
	 */
	List<Risk> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个Risk
	 * @param id
	 * @return
	 */
	Risk findById(@Param(value = "id") Long id);
	/**
	 * 批量保存Risk
	 * @param risks
	 */
	void batchAdd(List<Risk> risks);
	/**
	 * 保存Risk
	 * @param risk
	 */
	void add(Risk risk);
	/**
	 * 批量更新Risk
	 * @param risks
	 */
	void batchUpdate(List<Risk> risks);
	/**
	 * 更新Risk
	 * @param risk
	 */
	void update(Risk risk);
	/**
	 * 删除Risk
	 * @param risks
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除Risk
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	String findNo();

	List<RiskExportVO> findByConditionExportList(RiskExportQO qo);

	/**
	 * 隐患统计
	 * @param userId
	 * @param startAt
	 * @param endAt
	 * @return
	 */
	Integer countByReporterId(@Param("userId")Long userId, @Param("startAt")Date startAt, @Param("endAt")Date endAt);
}
