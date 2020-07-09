package com.bit.module.sv.dao;

import com.bit.module.sv.bean.HazardList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HazardListDao {
    int deleteById(Long id);

    int insert(HazardList hazardList);

    int insertSelective(HazardList hazardList);

    HazardList selectById(Long id);

    int updateByIdSelective(HazardList hazardList);

    int updateById(HazardList hazardList);

    List<HazardList> findAll();
}