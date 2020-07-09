package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDao {
    int deleteById(Long id);

    int insert(Item item);

    int insertSelective(Item item);

    Item selectById(Long id);

    int updateByIdSelective(Item item);

    int updateById(Item item);

    int batchInsert(@Param("list") List<Item> list);

    List<Item> findBySourceAndName(@Param("source") Integer source, @Param("name") String name);

    List<Item> findAllBySource(Integer range);

    List<Item> findByIdIn(@Param("list") List<Long> itemIds);
}