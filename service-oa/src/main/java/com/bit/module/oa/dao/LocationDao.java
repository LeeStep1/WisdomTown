package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 10:21
 */
public interface LocationDao {
    /**
     * 通过主键查询单个Location
     * @param executeIds
     * @return
     */
    List<Location> findByExecuteIds(@Param(value = "executeIds") List<Long> executeIds);
    /**
     * 保存Location
     * @param location
     */
    void add(Location location);
}
