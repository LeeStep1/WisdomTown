package com.bit.module.pb.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ExServiceman;
import com.bit.module.pb.dao.ExServicemanDao;
import com.bit.module.pb.service.ExServicemanService;
import com.bit.module.pb.vo.ExServicemanVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ExServiceman的Service实现类
 * @author codeGenerator
 *
 */
@Service("exServicemanService")
public class ExServicemanServiceImpl extends BaseService implements ExServicemanService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExServicemanServiceImpl.class);
	
	@Autowired
	private ExServicemanDao exServicemanDao;
	
	/**
	 * 根据条件查询ExServiceman
	 * @param exServicemanVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(ExServicemanVO exServicemanVO){
		PageHelper.startPage(exServicemanVO.getPageNum(), exServicemanVO.getPageSize());
		List<ExServiceman> list = exServicemanDao.findByConditionPage(exServicemanVO);
		PageInfo<ExServiceman> pageInfo = new PageInfo<ExServiceman>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有ExServiceman
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<ExServiceman> findAll(String sorter){
		return exServicemanDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个ExServiceman
	 * @param id
	 * @return
	 */
	@Override
	public ExServiceman findById(Long id){
		return exServicemanDao.findById(id);
	}
	
	/**
	 * 批量保存ExServiceman
	 * @param exServicemans
	 */
	@Override
	public void batchAdd(List<ExServiceman> exServicemans){
		exServicemanDao.batchAdd(exServicemans);
	}
	/**
	 * 保存ExServiceman
	 * @param exServiceman
	 */
	@Override
	public void add(ExServiceman exServiceman){
		exServicemanDao.add(exServiceman);
	}
	/**
	 * 批量更新ExServiceman
	 * @param exServicemans
	 */
	@Override
	public void batchUpdate(List<ExServiceman> exServicemans){
		exServicemanDao.batchUpdate(exServicemans);
	}
	/**
	 * 更新ExServiceman
	 * @param exServiceman
	 */
	@Override
	public void update(ExServiceman exServiceman){
		exServicemanDao.update(exServiceman);
	}
	/**
	 * 删除ExServiceman
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		exServicemanDao.batchDelete(ids);
	}

	@Override
	public ExServiceman findByIdCard(String idCard) {
		return exServicemanDao.findByIdCard(idCard);
	}

	/**
	 * 删除ExServiceman
	 * @param id
	 */
	@Override
	public void delete(Long id){
		exServicemanDao.delete(id);
	}
}
