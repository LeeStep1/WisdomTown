package com.bit.soft.configserver.dao;

import com.bit.soft.configserver.bean.ConfigProperties;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性设置Dao
 * @author liy
 */
public interface ConfigDao {

    /**
     * 增加属性
     * @param configProperties
     */
    void insertPropertiesSql(@Param("configProperties") ConfigProperties configProperties);

    /**
     * 根据ID删除设置
     * @param id
     */
    void delPropertiesSql(@Param("id") Integer id);

    /**
     * 根据ID修改设置
     * @param configProperties
     */
    void updatePropertiesSql(@Param("configProperties") ConfigProperties configProperties);

    /**
     * 查看属性设置
     * @return 属性明细
     */
    List<ConfigProperties> selectPropertiesSql();


}
