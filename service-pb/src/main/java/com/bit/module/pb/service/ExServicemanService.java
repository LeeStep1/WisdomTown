package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ExServiceman;
import com.bit.module.pb.vo.ExServicemanVO;

import java.util.List;
/**
 * ExServiceman的Service
 * @author codeGenerator
 */
public interface ExServicemanService {
	/**
	 * 根据条件查询ExServiceman
	 * @param exServicemanVO
	 * @return
	 */
	BaseVo findByConditionPage(ExServicemanVO exServicemanVO);
	/**
	 * 查询所有ExServiceman
	 * @param sorter 排序字符串
	 * @return
	 */
	List<ExServiceman> findAll(String sorter);
	/**
	 * 通过主键查询单个ExServiceman
	 * @param id
	 * @return
	 */
	ExServiceman findById(Long id);

	/**
	 * 批量保存ExServiceman
	 * @param exServicemans
	 */
	void batchAdd(List<ExServiceman> exServicemans);
	/**
	 * 保存ExServiceman
	 * @param exServiceman
	 */
	void add(ExServiceman exServiceman);
	/**
	 * 批量更新ExServiceman
	 * @param exServicemans
	 */
	void batchUpdate(List<ExServiceman> exServicemans);
	/**
	 * 更新ExServiceman
	 * @param exServiceman
	 */
	void update(ExServiceman exServiceman);
	/**
	 * 删除ExServiceman
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除ExServiceman
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 根据党员ID获取退伍信息
	 * @param idCard
	 * @return
	 */
	ExServiceman findByIdCard(String idCard);
}
