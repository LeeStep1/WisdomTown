package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Unit;
import com.bit.module.sv.vo.UnitVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UnitDao {
    List<Unit> findByConditionPage(UnitVO unitVO);

    int deleteByPrimaryKey(Long id);

    void insert(Unit record);

    int insertSelective(Unit record);

    Unit findById(Long id);

    int updateByPrimaryKeySelective(Unit record);

    int updateByPrimaryKey(Unit record);

    int batchAdd(@Param("list") List<Unit> list);

    List<Unit> findByType(Unit unit);

    void increaseInjuriaCount(@Param("id") Long id, @Param("count") Integer count);

    List<Unit> findByIds(@Param("ids") Collection<Long> ids);
}