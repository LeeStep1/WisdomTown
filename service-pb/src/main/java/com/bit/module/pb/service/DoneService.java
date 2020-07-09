package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.vo.DoneVO;

import java.util.List;
/**
 * Done的Service
 * @author codeGenerator
 */
public interface DoneService {
	/**
	 * 根据条件查询Done
	 * @param doneVO
	 * @return
	 */
	BaseVo findByConditionPage(DoneVO doneVO);
	/**
	 * 查询所有Done
	 * @param sorter 排序字符串
	 * @return
	 */
	List<Done> findAll(String sorter);
	/**
	 * 通过主键查询单个Done
	 * @param id
	 * @return
	 */
	Done findById(Long id);

	/**
	 * 批量保存Done
	 * @param dones
	 */
	void batchAdd(List<Done> dones);
	/**
	 * 保存Done
	 * @param done
	 */
	void add(Done done);
	/**
	 * 批量更新Done
	 * @param dones
	 */
	void batchUpdate(List<Done> dones);
	/**
	 * 更新Done
	 * @param done
	 */
	void update(Done done);
	/**
	 * 删除Done
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Done
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 获取查询记录
	 * @param correlationId
	 * @return
	 */
	List<Done> findRecord(Long correlationId);

	/**
	 * 获取停用党员的停用记录
	 * @param correlationId
	 * @return
	 */
	Done findOutreason(Long correlationId);

	/**
	 * 获取最后一条记录
	 * @param doneVO
	 * @return
	 */
	Done getRelativeLastRecord(DoneVO doneVO);
}
