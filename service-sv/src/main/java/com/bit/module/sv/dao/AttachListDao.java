package com.bit.module.sv.dao;

import com.bit.module.sv.bean.AttachList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachListDao {
    int deleteById(Long id);

    int insert(AttachList attachList);

    int insertSelective(AttachList attachList);

    AttachList selectById(Long id);

    int updateByIdSelective(AttachList attachList);

    int updateById(AttachList attachList);

    List<AttachList> findAll();
}