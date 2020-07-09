package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Risk;
import com.bit.module.oa.vo.risk.RiskExportQO;
import com.bit.module.oa.vo.risk.RiskExportVO;
import com.bit.module.oa.vo.risk.RiskVO;

import java.util.Date;
import java.util.List;

/**
 * Risk的Service
 * @author codeGenerator
 */
public interface RiskService {
	/**
	 * 根据条件查询Risk
	 * @param riskVO
	 * @return
	 */
	BaseVo findByConditionPage(RiskVO riskVO);
	/**
	 * 通过主键查询单个Risk
	 * @param id
	 * @return
	 */
	Risk findDetailById(Long id);
	/**
	 * 保存Risk
	 * @param risk
	 */
	void add(Risk risk);
	/**
	 * 更新Risk
	 * @param risk
	 */
	void feedback(Risk risk);

    List<RiskExportVO> exportByConditionPage(RiskExportQO qo);
}
