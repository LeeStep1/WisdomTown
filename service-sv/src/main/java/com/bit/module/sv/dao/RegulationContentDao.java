package com.bit.module.sv.dao;

import com.bit.module.sv.bean.RegulationContent;
import com.bit.module.sv.vo.RegulationContentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RegulationContentDao {
    int deleteByPrimaryKey(Long id);

    int insert(RegulationContent record);

    RegulationContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RegulationContent record);

    List<RegulationContent> findByRegulationId(@Param("regulationId") Long regulationId);

    int deleteByRegulationId(@Param("regulationId") Long regulationId);

    int deleteByRegulationIdIn(@Param("regulationIds") Collection<Long> regulationIds);

    /**
     * 正文分页
     * @param content
     * @return
     */
    List<RegulationContent> findByConditionPage(RegulationContentVO content);
}