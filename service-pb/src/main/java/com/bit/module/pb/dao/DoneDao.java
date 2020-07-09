package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Done;
import com.bit.module.pb.vo.DoneVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Done管理的Dao
 *
 * @author
 */
@Repository
public interface DoneDao {
    /**
     * 根据条件查询Done
     *
     * @param doneVO
     * @return
     */
    List<Done> findByConditionPage(DoneVO doneVO);

    /**
     * 查询所有Done
     *
     * @return
     */
    List<Done> findAll(@Param(value = "sorter") String sorter);

    /**
     * 通过主键查询单个Done
     *
     * @param id
     * @return
     */
    Done findById(@Param(value = "id") Long id);

    /**
     * 批量保存Done
     *
     * @param dones
     */
    void batchAdd(List<Done> dones);

    /**
     * 保存Done
     *
     * @param done
     */
    void add(Done done);

    /**
     * 批量更新Done
     *
     * @param dones
     */
    void batchUpdate(List<Done> dones);

    /**
     * 更新Done
     *
     * @param done
     */
    void update(Done done);

    /**
     * 删除Done
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 删除Done
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 查询记录
     *
     * @param correlationId
     * @return
     */
    List<Done> findRecord(@Param(value = "correlationId") Long correlationId);

    /**
     * 获取党员停用的原因
     *
     * @param correlationId
     * @return
     */
    Done findOutreason(@Param(value = "correlationId") Long correlationId, @Param(value = "topic") Integer topic, @Param(value = "action") Integer action);

    /**
     * 获取最后一条记录
     *
     * @param doneVO
     * @return
     */
    Done getRelativeLastRecord(DoneVO doneVO);
}
