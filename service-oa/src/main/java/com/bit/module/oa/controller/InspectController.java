package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Inspect;
import com.bit.module.oa.enums.CountTypeEnum;
import com.bit.module.oa.service.InspectService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.inspect.*;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Inspect的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/inspect")
public class InspectController {
	private static final Logger logger = LoggerFactory.getLogger(InspectController.class);
	@Autowired
	private InspectService inspectService;
	

    /**
     * 巡检记录分页
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody InspectVO inspectVO) {
    	//分页对象，前台传递的包含查询的参数

        return inspectService.findByConditionPage(inspectVO);
    }

    /**
     * 巡检记录分页
     *
     */
    @PostMapping("/app/listPage")
    public BaseVo listPageForApp(@RequestBody InspectVO inspectVO) {
    	//分页对象，前台传递的包含查询的参数

        return inspectService.findByConditionPageForApp(inspectVO);
    }

    /**
     * 导出巡检任务文档
     * @param name
     * @param type
     * @param status
     * @param startTime
     * @param endTime
     * @param response
     */
    @PostMapping("/task/export")
    public void export(@RequestParam(name = "name", required = false) String name,
                       @RequestParam(name = "type", required = false) Integer type,
                       @RequestParam(name = "status", required = false) Integer status,
                       @RequestParam(name = "startTime", required = false) Long startTime,
                       @RequestParam(name = "endTime", required = false) Long endTime,
                       HttpServletResponse response) {
        try {
            Inspect inspect = new Inspect();
            inspect.setName(name);
            inspect.setType(type);
            inspect.setStatus(status);
            inspect.setStartTime(startTime == null ? null : new Date(startTime));
            inspect.setEndTime(endTime == null ? null : new Date(endTime));

            List<InspectTaskExport> inspectTasks =  inspectService.findExportTaskInspect(inspect);
            AtomicLong num = new AtomicLong(1L);
            inspectTasks.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, inspectTasks, InspectTaskExport.class, "巡检任务管理");
        } catch (Exception e) {
            logger.error("巡检任务管理导出异常 : {}", e);
        }
    }

    /**
     * 巡检情况管理导出
     * @param name
     * @param no
     * @param type
     * @param startTime
     * @param endTime
     * @param queryType
     * @param response
     */
    @PostMapping("/situation/export")
    public void situationExport(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "no", required = false) String no,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "startTime", required = false) Long startTime,
                                @RequestParam(name = "endTime", required = false) Long endTime,
                                @RequestParam(name = "queryType", required = false) Integer queryType,
                                HttpServletResponse response) {
        try {
            InspectExportQO inspect = new InspectExportQO();
            inspect.setName(name);
            inspect.setType(type);
            inspect.setNo(no);
            inspect.setStartTime(startTime == null ? null : new Date(startTime));
            inspect.setEndTime(endTime == null ? null : new Date(endTime));
            inspect.setQueryType(queryType);

            List<InspectSituationExport> inspectSituations =  inspectService.findExportSituationInspect(inspect);
            AtomicLong num = new AtomicLong(1L);
            inspectSituations.forEach(vo-> vo.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, inspectSituations, InspectSituationExport.class, "巡检情况管理");
        } catch (Exception e) {
            logger.error("巡检情况管理导出异常 : {}", e);
        }
    }

    /**
     * 根据主键ID查询Inspect
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        InspectExecutorVO inspect = inspectService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(inspect);
		return baseVo;
    }
    
    /**
     * 新增Inspect
     *
     * @param inspect Inspect实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Inspect inspect) {
        CheckUtil.notNull(inspect.getName());
        CheckUtil.notNull(inspect.getType());
        inspectService.add(inspect);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Inspect
     *
     * @param inspect Inspect实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Inspect inspect) {
		inspectService.update(inspect);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Inspect
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        inspectService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }


    /**
     * 发布巡检任务
     *
     * @param inspect Inspect实体
     * @return
     */
    @PostMapping("/publish")
    public BaseVo publish(@RequestBody Inspect inspect) {

        inspectService.publish(inspect);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 结束巡检任务
     * @param id
     * @return
     */
    @PostMapping("/end")
    public BaseVo end(@RequestBody Long id) {
        inspectService.end(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据类型统计巡检单
     * @param type
     * @return
     */
    @GetMapping("/{type}/count")
    public BaseVo count(@PathVariable("type") Integer type) {
        InspectCountVO count = new InspectCountVO();
        if (CountTypeEnum.ALL.getType().equals(type)) {
            count = inspectService.count(type, null, null);
        } else if (CountTypeEnum.TODAY.getType().equals(type)) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            Date startAt = today.getTime();
            today.set(Calendar.HOUR_OF_DAY, 23);
            today.set(Calendar.MINUTE, 59);
            today.set(Calendar.SECOND, 59);
            Date endAt = today.getTime();
            count = inspectService.count(type, startAt, endAt);
        }
        return new BaseVo(count);
    }
}
