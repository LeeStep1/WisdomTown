package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Plan;
import com.bit.module.sv.vo.PlanVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDao {
    int deleteById(Long id);

    int insert(Plan plan);

    int insertSelective(Plan plan);

    Plan selectById(Long id);

    int updateByIdSelective(Plan plan);

    int updateById(Plan plan);

    int batchInsert(@Param("list") java.util.List<Plan> list);

    List<Plan> findByConditionPage(PlanVO planVO);

    int increaseCompletedTaskNum(@Param("id") Long id, @Param("count") Integer count);

    int increaseTotalTaskNum(@Param("id") Long id, @Param("count") Integer count);
}