package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Application;
import com.bit.module.oa.vo.application.ApplicationPageVO;
import com.bit.module.oa.vo.application.ApplicationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/19 14:29
 */
@Repository
public interface ApplicationDao {
    /**
     * 根据条件查询Application
     * @param applicationVO
     * @return
     */
    List<ApplicationPageVO> findByConditionPage(ApplicationVO applicationVO);
    /**
     * 通过主键查询单个Application
     * @param id
     * @return
     */
    Application findById(@Param(value = "id") Long id);

    /**
     * 通过executeId查询单个Application
     * @param executeId
     * @return
     */
    Application findByExecuteId(@Param(value = "executeId") Long executeId);
    /**
     * 批量保存Application
     * @param applications
     */
    void batchAdd(@Param(value = "applications") List<Application> applications);
    /**
     * 保存Application
     * @param application
     */
    void add(Application application);
    /**
     * 更新Application
     * @param application
     */
    void update(Application application);
    /**
     * 批量更新Application
     * @param applications
     */
    void batchUpdate(List<Application> applications);
    /**
     * 删除Application
     * @param ids
     */
    void batchDelete(List<Long> ids);
    /**
     * 删除Application
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    Integer existByExecuteIdAndUserIdAndStatus(@Param(value = "executeId") Long executeId,
                                            @Param(value = "userId") Long userId,
                                            @Param(value = "status") Integer status);

    String findNo();
}
