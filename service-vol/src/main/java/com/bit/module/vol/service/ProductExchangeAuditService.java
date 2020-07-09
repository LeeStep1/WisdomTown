package com.bit.module.vol.service;

import java.util.List;

import com.bit.module.vol.bean.ProductExchangeAudit;
import com.bit.module.vol.vo.ProductExchangeAuditVO;
import com.bit.base.vo.BaseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ProductExchangeAudit的Service
 * @author liuyancheng
 */
public interface ProductExchangeAuditService {
	/**
	 * 根据条件查询ProductExchangeAudit
	 * @param productExchangeAuditVO
	 * @return
	 */
	BaseVo findByConditionPage(ProductExchangeAuditVO productExchangeAuditVO);
	/**
	 * 根据条件查询ProductExchangeAudit 兑换确认
	 * @param productExchangeAuditVO
	 * @return
	 */
	BaseVo listPageConfirm(ProductExchangeAuditVO productExchangeAuditVO);
	/**
	 * 查询所有ProductExchangeAudit
	 * @param sorter 排序字符串
	 * @return
	 */
	List<ProductExchangeAudit> findAll(String sorter);
	/**
	 * 通过主键查询单个ProductExchangeAudit
	 * @param id
	 * @return
	 */
	ProductExchangeAudit findById(Long id);
	/**
	 * 保存ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	void add(ProductExchangeAudit productExchangeAudit);
	/**
	 * 更新ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	BaseVo update(ProductExchangeAudit productExchangeAudit, HttpServletRequest request);

	/**
	 * 领取
	 * @param productExchangeAudit
	 * @param request
	 * @return
	 */
    BaseVo updateGet(ProductExchangeAudit productExchangeAudit, HttpServletRequest request);

	/**
	 * 导出所有数据到excel
	 * @param response
	 * @return
	 */
    void exportToExcel(String productName,String shopName,String proposerName,Integer getStatus,String ids, HttpServletResponse response);

	/**
	 * 批量设置领取状态
	 * @param productExchangeAuditVO
	 * @return
	 */
	BaseVo batchGet(ProductExchangeAuditVO productExchangeAuditVO);

	/**
	 * 查看用户兑换记录
	 * @return
	 */
	BaseVo getExchangeRecord(ProductExchangeAuditVO productExchangeAuditVO);

	/**
	 * 定时查询商品统计的积分和总兑换的数量
	 */
    void timingIntegral();
}
