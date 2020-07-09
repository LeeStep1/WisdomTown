package com.bit.module.vol.service;

import java.util.List;

import com.bit.module.vol.vo.ProductExchangeAuditVO;
import org.springframework.stereotype.Service;
import com.bit.module.vol.bean.BenevolenceShopProduct;
import com.bit.module.vol.vo.BenevolenceShopProductVO;
import com.bit.base.vo.BaseVo;

import javax.servlet.http.HttpServletResponse;

/**
 * BenevolenceShopProduct的Service
 * @author liuyancheng
 */
public interface BenevolenceShopProductService {
	/**
	 * 根据条件查询BenevolenceShopProduct
	 * @param benevolenceShopProductVO
	 * @return
	 */
	BaseVo findByConditionPage(BenevolenceShopProductVO benevolenceShopProductVO);
	/**
	 * 查询所有BenevolenceShopProduct
	 * @param sorter 排序字符串
	 * @return
	 */
	List<BenevolenceShopProduct> findAll(String sorter);
	/**
	 * 通过主键查询单个BenevolenceShopProduct
	 * @param id
	 * @return
	 */
	BenevolenceShopProduct findById(Long id);
	/**
	 * 保存BenevolenceShopProduct
	 * @param benevolenceShopProduct
	 */
	void add(BenevolenceShopProduct benevolenceShopProduct);
	/**
	 * 更新BenevolenceShopProduct
	 * @param benevolenceShopProduct
	 */
	void update(BenevolenceShopProduct benevolenceShopProduct);

	/**
	 * 领取记录
	 * @param productExchangeAuditVO
	 * @return
	 */
    BaseVo getRecord(ProductExchangeAuditVO productExchangeAuditVO);

	/**
	 * 商品批量导出
	 * @param response
	 */
	void exportToExcel(String productName,String shopName,Integer productState,String ids, HttpServletResponse response);

	/**
	 * 商品详情
	 * @param id
	 * @return
	 */
    BaseVo queryProduct(Long id);

	/**
	 * 手机端分页列表
	 * @param benevolenceShopProductVO
	 * @return
	 */
	BaseVo findPageMobile(BenevolenceShopProductVO benevolenceShopProductVO);
}
