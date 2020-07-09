package com.bit.module.vol.service;

import java.util.List;

import com.bit.module.vol.vo.BenevolenceShopProductVO;
import org.springframework.stereotype.Service;
import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.vo.BenevolenceShopVO;
import com.bit.base.vo.BaseVo;

import javax.servlet.http.HttpServletResponse;

/**
 * BenevolenceShop的Service
 * @author liuyancheng
 */
public interface BenevolenceShopService {
	/**
	 * 根据条件查询BenevolenceShop
	 * @param benevolenceShopVO
	 * @return
	 */
	BaseVo findByConditionPage(BenevolenceShopVO benevolenceShopVO);
	/**
	 * 查询所有BenevolenceShop
	 * @param sorter 排序字符串
	 * @return
	 */
	List<BenevolenceShop> findAll(String sorter);
	/**
	 * 通过主键查询单个BenevolenceShop
	 * @param id
	 * @return
	 */
	BenevolenceShop findById(Long id);

	/**
	 * 保存BenevolenceShop
	 * @param benevolenceShop
	 */
	void add(BenevolenceShop benevolenceShop);
	/**
	 * 更新BenevolenceShop
	 * @param benevolenceShop
	 */
	void update(BenevolenceShop benevolenceShop);
	/**
	 * 根据商家id查询商品
	 * @param benevolenceShopProductVO
	 * @return
	 */
    BaseVo getShopProduct(BenevolenceShopProductVO benevolenceShopProductVO);

	/**
	 * 导出excel
	 */
	void exportToExcel(String shopName,String contacts,Integer enable,String ids, HttpServletResponse response);
}
