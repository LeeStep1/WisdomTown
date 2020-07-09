package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Company;
import com.bit.module.oa.vo.company.CompanyPageVO;
import com.bit.module.oa.vo.company.CompanyVO;
import com.bit.module.oa.vo.company.SimpleCompanyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Company管理的Dao
 * @author 
 *
 */
@Repository
public interface CompanyDao {
	/**
	 * 根据条件查询Company
	 * @param companyVO
	 * @return
	 */
	List<CompanyPageVO> findByConditionPage(CompanyVO companyVO);

	/**
	 * 根据条件查询Company
	 * @param nature
	 * @param name
	 * @return
	 */
	List<CompanyPageVO> findByCondition(@Param("nature") String nature, @Param("name") String name);
	/**
	 * 查询所有Company
	 * @return
	 */
	List<SimpleCompanyVO> findAll(@Param(value="sorter")String sorter);
	/**
	 * 通过主键查询单个Company
	 * @param id
	 * @return
	 */
	CompanyPageVO findById(@Param(value="id")Long id);
	/**
	 * 批量保存Company
	 * @param companys
	 */
	void batchAdd(List<Company> companys);
	/**
	 * 保存Company
	 * @param company
	 */
	void add(Company company);
	/**
	 * 批量更新Company
	 * @param companys
	 */
	void batchUpdate(List<Company> companys);
	/**
	 * 更新Company
	 * @param company
	 */
	void update(Company company);
	/**
	 * 删除Company
	 * @param companys
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除Company
	 * @param id
	 */
	void delete(@Param(value="id")Long id);

    void updateStatus(Long id, Integer status);

}
