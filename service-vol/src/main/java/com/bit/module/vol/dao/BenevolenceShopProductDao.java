package com.bit.module.vol.dao;

import java.util.List;

import com.bit.module.vol.bean.BenevolenceShopProduct;
import com.bit.module.vol.vo.BenevolenceShopProductExcelVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.vol.vo.BenevolenceShopProductVO;

/**
 * BenevolenceShopProduct管理的Dao
 * @author liuyancheng
 *
 */
public interface BenevolenceShopProductDao {
	/**
	 * 根据条件查询BenevolenceShopProduct
	 * @param benevolenceShopProductVO
	 * @return
	 */
	public List<BenevolenceShopProduct> findByConditionPage(BenevolenceShopProductVO benevolenceShopProductVO);
	/**
	 * 查询所有BenevolenceShopProduct
	 * @return
	 */
	public List<BenevolenceShopProduct> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个BenevolenceShopProduct
	 * @param id	 	 
	 * @return
	 */
	public BenevolenceShopProduct findById(@Param(value = "id") Long id);
	/**
	 * 批量保存BenevolenceShopProduct
	 * @param benevolenceShopProducts
	 */
	public void batchAdd(List<BenevolenceShopProduct> benevolenceShopProducts);
	/**
	 * 保存BenevolenceShopProduct
	 * @param benevolenceShopProduct
	 */
	public void add(BenevolenceShopProduct benevolenceShopProduct);
	/**
	 * 批量更新BenevolenceShopProduct
	 * @param products
	 */
	public void batchUpdate(@Param(value = "products") List<BenevolenceShopProduct> products);
	/**
	 * 更新BenevolenceShopProduct
	 * @param benevolenceShopProduct
	 */
	public void update(BenevolenceShopProduct benevolenceShopProduct);
	/**
	 * 删除BenevolenceShopProduct
	 * @param ids
	 */
	public void batchDelete(List<Long> ids);
	/**
	 * 删除BenevolenceShopProduct
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据商家id查询商品
	 * @param benevolenceShopProductVO
	 * @return
	 */
    List<BenevolenceShopProduct> findByShopId(BenevolenceShopProductVO benevolenceShopProductVO);

	/**
	 * 根据参数查询
	 * @param benevolenceShopProductVO
	 * @return
	 */
	List<BenevolenceShopProduct> findByParam(BenevolenceShopProductExcelVO benevolenceShopProductVO);

	/**
	 * 根据id批量查询
	 * @param benevolenceShopProductVO
	 * @return
	 */
	List<BenevolenceShopProduct> batchSelect(BenevolenceShopProductExcelVO benevolenceShopProductVO);
}
