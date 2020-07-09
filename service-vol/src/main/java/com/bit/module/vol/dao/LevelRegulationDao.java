package com.bit.module.vol.dao;

import com.bit.module.vol.bean.LevelRegulation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author chenduo
 * @create 2019-03-20 22:38
 */
@Repository
public interface LevelRegulationDao {

    LevelRegulation queryByLevel(@Param(value = "serviceLevel") int serviceLevel);
}
