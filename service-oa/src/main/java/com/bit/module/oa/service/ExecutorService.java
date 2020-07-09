package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.vo.executor.ExecutorExportVO;
import com.bit.module.oa.vo.executor.ExecutorVO;
import com.bit.module.oa.vo.executor.InspectExecuteDetail;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/18 16:51
 */
public interface ExecutorService {
    /**
     * 根据条件查询Executor
     * @param executorVO
     * @return
     */
    BaseVo findByConditionPage(ExecutorVO executorVO);
    /**
     * 放弃巡检任务
     * @param executor
     */
    void giveUp(Executor executor);

    /**
     * 开始巡检任务
     * @param executor
     */
    void start(Executor executor);

    /**
     * 结束巡检任务
     * @param executor
     */
    void end(Executor executor);

    /**
     *
     * @param id
     * @return
     */
    InspectExecuteDetail findDetail(Long id);

    /**
     * 导出
     * @param toQuery
     * @return
     */
    List<ExecutorExportVO> findExportExecutor(Executor toQuery);
}
