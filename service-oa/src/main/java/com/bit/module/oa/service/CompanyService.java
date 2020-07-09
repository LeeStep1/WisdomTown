package com.bit.module.oa.service;

import java.util.List;

import com.bit.module.oa.vo.company.CompanyExportVO;
import com.bit.module.oa.vo.company.CompanyPageVO;
import com.bit.module.oa.vo.company.SimpleCompanyVO;
import com.bit.module.oa.bean.Company;
import com.bit.module.oa.vo.company.CompanyVO;
import com.bit.base.vo.BaseVo;
/**
 * Company的Service
 * @author codeGenerator
 */
public interface CompanyService {
	/**
	 * 根据条件查询Company
	 * @param companyVO
	 * @return
	 */
	BaseVo findByConditionPage(CompanyVO companyVO);
	/**
	 * 查询所有Company
	 * @param sorter 排序字符串
	 * @return
	 */
	List<SimpleCompanyVO> findAll(String sorter);
	/**
	 * 通过主键查询单个Company
	 * @param id
	 * @return
	 */
	CompanyPageVO findById(Long id);

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
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除Company
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 *
	 * @param id
	 * @param status
	 */
    void convertStatus(Long id, Integer status);

    List<CompanyExportVO> findExportCompany(String nature, String name);

}
