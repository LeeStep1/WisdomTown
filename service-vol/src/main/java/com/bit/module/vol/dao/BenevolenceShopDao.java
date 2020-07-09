package com.bit.module.vol.dao;

import java.util.List;

import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.vo.BenevolenceShopExcelVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.vol.vo.BenevolenceShopVO;

/**
 * BenevolenceShop管理的Dao
 * @author liuyancheng
 *
 */
public interface BenevolenceShopDao {
	/**
	 * 根据条件查询BenevolenceShop
	 * @param benevolenceShopVO
	 * @return
	 */
	public List<BenevolenceShop> findByConditionPage(BenevolenceShopVO benevolenceShopVO);
	/**
	 * 查询所有BenevolenceShop
	 * @return
	 */
	public List<BenevolenceShop> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个BenevolenceShop
	 * @param id	 	 
	 * @return
	 */
	public BenevolenceShop findById(@Param(value = "id") Long id);
	/**
	 * 批量保存BenevolenceShop
	 * @param benevolenceShops
	 */
	public void batchAdd(List<BenevolenceShop> benevolenceShops);
	/**
	 * 保存BenevolenceShop
	 * @param benevolenceShop
	 */
	public void add(BenevolenceShop benevolenceShop);
	/**
	 * 更新BenevolenceShop
	 * @param benevolenceShop
	 */
	public void update(BenevolenceShop benevolenceShop);
	/**
	 * 删除BenevolenceShop
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据名称查询是否重复
	 * @param benevolenceShop
	 * @return
	 */
	int findByName(BenevolenceShop benevolenceShop);

	/**
	 * 根据参数查询
	 * @param benevolenceShopVO
	 * @return
	 */
	List<BenevolenceShop> findByParam(BenevolenceShopExcelVO benevolenceShopVO);

	/**
	 * 根据id批量查询
	 * @param benevolenceShopVO
	 * @return
	 */
	List<BenevolenceShop> batchSelect(BenevolenceShopExcelVO benevolenceShopVO);

	/**
	 * 批量更新
	 * @param shops
	 */
	void batchUpdate(@Param(value = "shops") List<BenevolenceShop> shops);
}
