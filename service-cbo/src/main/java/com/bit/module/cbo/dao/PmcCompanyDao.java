package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.vo.PmcCompanyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 物业公司相关dao
 * @author: liyang
 * @date: 2019-07-18
 **/
public interface PmcCompanyDao {

    /**
     * 新增物业公司
     * @param pmcCompany
     */
    void add(@Param("pmcCompany") PmcCompany pmcCompany);

    /**
     * 修改物业公司信息
     * @param pmcCompany
     */
    void modify(@Param("pmcCompany") PmcCompany pmcCompany);

    /**
     * 查询物业公司列表
     * @return
     */
    List<PmcCompany> findAllSql(@Param("pmcCompanyVO") PmcCompanyVO pmcCompanyVO);

    /**
     * 查询物业公司列表
     * @return
     */
    Integer findCompanyByNameSql(@Param("name") String name);

    /**
     * 查询物业公司列表(修改时使用)
     * @return
     */
    Integer modifyFindCompanyByNameSql(@Param("name") String name,@Param("id") Long id);


    /**
     * 查询物业公司列表
     * @return
     */
    List<PmcCompany> findPmcCompanySql(@Param("pmcCompany") PmcCompany pmcCompany);

    /**
     * 根据ID删除物业公司
     * @return
     */
    void delete(Long id);

    /**
     * 根据id查询物业公司信息
     * @param id
     * @return
     */
    PmcCompany getPmcCompanyById(@Param(value = "id")Long id);
}
