package com.bit.module.vol.dao;

import java.util.List;

import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.bean.BenevolenceShopAudit;
import com.bit.module.vol.vo.BenevolenceShopVO;
import org.apache.ibatis.annotations.Param;
import com.bit.module.vol.vo.BenevolenceShopAuditVO;

/**
 * BenevolenceShopAudit管理的Dao
 * @author liuyancheng
 *
 */
public interface BenevolenceShopAuditDao {
	/**
	 * 根据条件查询BenevolenceShopAudit
	 * @param benevolenceShopVO
	 * @return
	 */
	public List<BenevolenceShop> findByConditionPage(BenevolenceShopVO benevolenceShopVO);
	/**
	 * 查询所有BenevolenceShopAudit
	 * @return
	 */
	public List<BenevolenceShop> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个BenevolenceShopAudit
	 * @param id	 	 
	 * @return
	 */
	public BenevolenceShop findById(@Param(value = "id") Long id);
	/**
	 * 保存BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	public void add(BenevolenceShop benevolenceShop);
	/**
	 * 更新BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	public void update(BenevolenceShop benevolenceShop);
	/**
	 * 删除BenevolenceShopAudit
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id);

	/**
	 * 根据名称查询是否重复
	 * @param name  名称
	 * @param auditState 审核状态
	 * @param exceptAuditState 除此审核状态
	 * @return
	 */
	int findByName(@Param(value = "name") String  name);
//	int findByName(@Param(value = "name") String  name,@Param(value = "auditState")Integer auditState,@Param(value = "exceptAuditState")Integer exceptAuditState);
}
