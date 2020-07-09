package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Log;
import com.bit.module.oa.vo.company.CompanyVO;
import com.bit.module.oa.vo.log.LogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/13 16:18
 */
public interface LogDao {
    /**
     * 根据条件查询Log
     * @param
     * @return
     */
    List<Log> findByConditionPage(LogVO logVO);
    /**
     * 通过主键查询单个Log
     * @param id
     * @return
     */
    Log findById(@Param(value = "id") Long id);
    /**
     * 保存Log
     * @param log
     */
    void add(Log log);
    /**
     * 删除Log
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     *
     * @param refId
     * @param type
     * @return
     */
    List<Log> findByRefIdAndType(@Param(value = "refId") Long refId, @Param(value = "type") Integer type);

}
