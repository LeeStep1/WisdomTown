package com.bit.module.sv.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mybatis Generator 2019/07/16
 */
@Repository
public interface UnitIndustryDao {
    int insert(@Param("unitId") Long unitId,
               @Param("industryCodes") List<String> industryCodes);

    List<String> findByUnitId(Long unitId);

    void deleteByUnitId(Long unitId);
}