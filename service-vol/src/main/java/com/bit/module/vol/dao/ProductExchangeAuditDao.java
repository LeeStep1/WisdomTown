package com.bit.module.vol.dao;

import java.util.Collection;
import java.util.List;

import com.bit.module.vol.bean.ProductExchangeAudit;
import com.bit.module.vol.vo.ProductExchangeAuditExcelVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.vol.vo.ProductExchangeAuditVO;

/**
 * ProductExchangeAudit管理的Dao
 * @author 
 *
 */
public interface ProductExchangeAuditDao {
	/**
	 * 根据条件查询ProductExchangeAudit
	 * @param productExchangeAuditVO
	 * @return
	 */
	public List<ProductExchangeAudit> findByConditionPage(ProductExchangeAuditVO productExchangeAuditVO);
	/**
	 * 查询所有ProductExchangeAudit
	 * @return
	 */
	public List<ProductExchangeAudit> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个ProductExchangeAudit
	 * @param id	 	 
	 * @return
	 */
	public ProductExchangeAudit findById(@Param(value = "id") Long id);
	/**
	 * 保存ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	public void add(ProductExchangeAudit productExchangeAudit);
	/**
	 * 批量更新ProductExchangeAudit
	 * @param productExchangeAuditVO
	 */
	public void batchUpdate(ProductExchangeAuditVO productExchangeAuditVO);
	/**
	 * 更新ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	public void update(ProductExchangeAudit productExchangeAudit);
	/**
	 * 删除ProductExchangeAudit
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据shopId查询已审核通过的列表
	 * @param productExchangeAuditVO
	 * @return
	 */
    List<ProductExchangeAudit> findByShopId(ProductExchangeAuditVO productExchangeAuditVO);

	/**
	 * 根据参数查询
	 * @param productExchangeAuditVO
	 * @return
	 */
	List<ProductExchangeAudit> findByParam(ProductExchangeAuditExcelVO productExchangeAuditVO);

	/**
	 * 根据主键id批量查询
	 * @param productExchangeAuditVO
	 * @return
	 */
	List<ProductExchangeAudit> batchSelect(ProductExchangeAuditExcelVO productExchangeAuditVO);

	/**
	 * 根据志愿者id查询兑换记录
	 * @param id
	 * @return
	 */
    List<ProductExchangeAudit> findByVolunteerId(@Param(value = "id") Long id);

	/**
	 * 根据shopid批量查询
	 * @param productExchangeAuditVO
	 * @return
	 */
	List<ProductExchangeAudit> batchSelectByShopId(ProductExchangeAuditExcelVO productExchangeAuditVO);

    /**
     * 根据商品id查询兑换总积分
     * @param shopId
     * @return
     */
	Integer countExchangeIntegralAmountByProductId(@Param(value = "shopId")Long shopId);
}
