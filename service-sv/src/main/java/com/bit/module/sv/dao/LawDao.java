package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Law;
import com.bit.module.sv.vo.LawVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LawDao {
    int deleteByPrimaryKey(Long id);

    int insert(Law law);

    int insertSelective(Law law);

    Law selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Law law);

    int updateByPrimaryKeyWithBLOBs(Law law);

    int updateByPrimaryKey(Law law);

    int updateLawUpdateAtById(@Param("id") Long id);

    int batchInsert(@Param("list") List<Law> list);

    List<Law> findByCondition(LawVO lawVO);

    List<Law> findAll(@Param("range") Integer range);
}