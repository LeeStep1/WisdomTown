package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Regulation;
import com.bit.module.sv.vo.RegulationPreviewVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface RegulationDao {
    List<Regulation> findByCondition(Regulation regulation);

    int deleteByPrimaryKey(Long id);

    int insert(Regulation regulation);

    int insertSelective(Regulation regulation);

    Regulation findById(Long id);

    int updateByPrimaryKeySelective(Regulation regulation);

    int updateByPrimaryKey(Regulation regulation);

    int batchAdd(@Param("list") List<Regulation> list);

    List<Regulation> findByIds(@Param("ids") Set<Long> ids);

    List<Long> findByParentId(@Param("parentId") Long parentId);

    int deleteByIdIn(@Param("ids") Set<Long> ids);

    List<RegulationPreviewVO> findAllContentByLawId(@Param("lawId") Long lawId);

    List<Long> findIdByLawId(@Param("lawId") Long lawId);

    int deleteByLawId(@Param("lawId") Long lawId);


    List<RegulationPreviewVO> getRegulationByContentIds(@Param("contentIds") Collection<Long> contentIds);

    void updateRegulationUpdateAtById(@Param("id") Long regulationId);
}