package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.service.ExecutorService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.executor.ExecutorExportVO;
import com.bit.module.oa.vo.executor.ExecutorVO;
import com.bit.module.oa.vo.executor.InspectExecuteDetail;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Executor的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/executor")
public class ExecutorController {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorController.class);

    @Autowired
    private ExecutorService executorService;


    /**
     * 打卡点记录分页
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ExecutorVO executorVO) {
        //分页对象，前台传递的包含查询的参数

        return executorService.findByConditionPage(executorVO);
    }

    /**
     * 查询巡检任务详情
     * @param id
     * @return
     */
    @GetMapping("query/{id}")
    public BaseVo query(@PathVariable("id") Long id) {
        InspectExecuteDetail detail = executorService.findDetail(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(detail);
        return baseVo;
    }
    /**
     * 放弃巡检任务
     *
     */
    @PostMapping("/giveUp")
    public BaseVo giveUp(@RequestBody Executor executor) {
        CheckUtil.notNull(executor.getInspectId());
        //分页对象，前台传递的包含查询的参数
        executorService.giveUp(executor);
        return new BaseVo();
    }

    /**
     * 开始巡检任务
     *
     */
    @PostMapping("/start")
    public BaseVo start(@RequestBody Executor executor) {
        CheckUtil.notNull(executor.getInspectId());
        //分页对象，前台传递的包含查询的参数
        executorService.start(executor);
        return new BaseVo();
    }

    /**
     * 结束巡检任务
     *
     */
    @PostMapping("/end")
    public BaseVo end(@RequestBody Executor executor) {
        CheckUtil.notNull(executor.getInspectId());
        //分页对象，前台传递的包含查询的参数
        executorService.end(executor);
        return new BaseVo();
    }

    /**
     * 导出巡检任务详情
     * @param inspectName
     * @param checkInStatus
     * @param applyStatus
     * @param inspectStartTime
     * @param inspectEndTime
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestParam(name = "inspectName", required = false) String inspectName,
                       @RequestParam(name = "checkInStatus", required = false) Integer checkInStatus,
                       @RequestParam(name = "applyStatus", required = false) Integer applyStatus,
                       @RequestParam(name = "inspectStartTime", required = false) Long inspectStartTime,
                       @RequestParam(name = "inspectEndTime", required = false) Long inspectEndTime,
                       HttpServletResponse response) {
        try {
            Executor toQuery = new Executor();
            toQuery.setInspectName(inspectName);
            toQuery.setCheckInStatus(checkInStatus);
            toQuery.setApplyStatus(applyStatus);
            toQuery.setInspectStartTime(inspectStartTime == null ? null : new Date(inspectStartTime));
            toQuery.setInspectEndTime(inspectEndTime == null ? null : new Date(inspectEndTime));

            List<ExecutorExportVO> executorExportVOList = executorService.findExportExecutor(toQuery);
            AtomicLong num = new AtomicLong(1L);
            executorExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, executorExportVOList, ExecutorExportVO.class, "签到信息管理");
        } catch (IOException e) {
            logger.error("签到信息管理导出异常 : {}", e);
        }
    }

}
