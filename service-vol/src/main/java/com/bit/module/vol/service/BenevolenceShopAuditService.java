package com.bit.module.vol.service;

import java.util.List;

import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.vo.BenevolenceShopVO;
import org.springframework.stereotype.Service;
import com.bit.module.vol.bean.BenevolenceShopAudit;
import com.bit.module.vol.vo.BenevolenceShopAuditVO;
import com.bit.base.vo.BaseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * BenevolenceShopAudit的Service
 * @author liuyancheng
 */
public interface BenevolenceShopAuditService {
	/**
	 * 根据条件查询BenevolenceShopAudit
	 * @param benevolenceShopVO
	 * @return
	 */
	BaseVo findByConditionPage(BenevolenceShopVO benevolenceShopVO);
	/**
	 * 查询所有BenevolenceShopAudit
	 * @param sorter 排序字符串
	 * @return
	 */
	List<BenevolenceShop> findAll(String sorter);
	/**
	 * 通过主键查询单个BenevolenceShopAudit
	 * @param id
	 * @return
	 */
	BenevolenceShop findById(Long id);

	/**
	 * 保存BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	BaseVo add(BenevolenceShop benevolenceShop);
	/**
	 * 更新BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	BaseVo update(BenevolenceShop benevolenceShop, HttpServletRequest request);
	/**
	 * 删除BenevolenceShopAudit
	 * @param id
	 */
	void delete(Long id);
}
