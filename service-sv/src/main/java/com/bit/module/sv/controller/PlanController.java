package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.enums.ResultEnum;
import com.bit.module.sv.service.PlanService;
import com.bit.module.sv.service.RectificationNoticeService;
import com.bit.module.sv.service.TaskService;
import com.bit.module.sv.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.bit.module.sv.utils.StringUtils.getSourceByHttpServletRequest;

/**
 * 巡检计划 api
 *
 * @author decai.liu
 * @create 2019-07-18
 */
@RestController
@RequestMapping()
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RectificationNoticeService rectificationNoticeService;

    // -------------------------------巡检计划---------------------------------------------------------------------------
    @PostMapping(name = "新增巡检计划(自动创建巡检任务)", path = "/plans/add")
    public BaseVo addPlan(@RequestBody @Validated(PlanVO.Add.class) PlanVO planVO, HttpServletRequest request) {
        planVO.setSource(getSourceByHttpServletRequest(request));
        return planService.addPlan(planVO);
    }

    @PostMapping(name = "巡检计划分页查询", path = "/plans")
    public BaseVo listPlans(@RequestBody PlanVO planVO, HttpServletRequest request) {
        planVO.setSource(getSourceByHttpServletRequest(request));
        return planService.listPlans(planVO);
    }

    @DeleteMapping(name = "删除巡检计划", path = "/plans/{id}")
    public BaseVo deletePlan(@PathVariable("id") Long id) {
        return planService.deleteById(id);
    }

    @GetMapping(name = "巡检计划详情", path = "/plans/{id}")
    public BaseVo selectPlanById(@PathVariable("id") Long id) {
        return planService.selectById(id);
    }

    @PostMapping(name = "编辑巡检计划", path = "/plans/modify")
    public BaseVo modifyPlan(@RequestBody @Validated(PlanVO.Modify.class) PlanVO planVO, HttpServletRequest request) {
        planVO.setSource(getSourceByHttpServletRequest(request));
        return planService.modifyById(planVO);
    }

    // -------------------------------巡检任务---------------------------------------------------------------------------
    @PostMapping(name = "开始巡检", path = "/tasks/{id}/start")
    public BaseVo startTask(@PathVariable("id") Long id) {
        return taskService.startTask(id);
    }

    @DeleteMapping(name = "删除巡检任务", path = "/tasks/{id}")
    public BaseVo deleteTask(@PathVariable("id") Long id) {
        return taskService.deleteById(id);
    }

    @PostMapping(name = "根据计划分页查询巡检任务", path = "/tasks")
    public BaseVo listTasksByPlanId(@RequestBody @Validated(TaskVO.PageSearch.class) TaskVO taskVO,
                                    HttpServletRequest request) {
        taskVO.setSource(getSourceByHttpServletRequest(request));
        return taskService.listTasks(taskVO);
    }

    @GetMapping(name = "巡检任务详情", path = "/tasks/{id}")
    public BaseVo getTaskById(@PathVariable("id") Long id, HttpServletRequest request) {
        return taskService.selectByIdWithAll(id, getSourceByHttpServletRequest(request));
    }

    @PostMapping(name = "编辑巡检任务", path = "/tasks/modify")
    public BaseVo modifyTask(@RequestBody @Validated(TaskVO.Modify.class) TaskVO taskVO) {
        return taskService.modifyTask(taskVO);
    }

    @PostMapping(name = "编辑任务排查单(排查通过)", path = "/checklists/modify")
    public BaseVo modifyChecklist(@RequestBody @Validated Checklist checklist) {
        return taskService.modifyChecklist(checklist);
    }

    @GetMapping(name = "检查n天后到期的巡检任务", path = "/tasks/daily-push-expire-task")
    public BaseVo dailyPushExpireTask() {
        return taskService.dailyPushExpireTask();
    }

    @GetMapping(name = "检查n天后到期的复查任务", path = "/tasks/daily-push-expire-review-task")
    public BaseVo dailyPushExpireReviewTask() {
        return taskService.dailyPushExpireReviewTask();
    }

    @PostMapping(name = "增量查询巡检任务", path = "/tasks/increment")
    public BaseVo incrementTasks(@RequestBody IncrementalRequest incrementalRequest,
                                 HttpServletRequest httpServletRequest) {
        incrementalRequest.setSource(getSourceByHttpServletRequest(httpServletRequest));
        return taskService.incrementTasks(incrementalRequest);
    }

    @PostMapping(name = "编辑任务排查单包括整改通知单", path = "/checklists-notices/modify")
    public BaseVo modifyChecklist(@RequestBody @Validated ChecklistNoticeVO checklistNoticeVO) {
        Checklist checklist = new Checklist();
        BeanUtils.copyProperties(checklistNoticeVO, checklist);
        BaseVo baseVo = taskService.modifyChecklist(checklist);
        if (checklist.getResult() == ResultEnum.PASS.value) {
            return baseVo;
        }
        RectificationNotice notice = new RectificationNotice();
        BeanUtils.copyProperties(checklistNoticeVO, notice);
        notice.setId(null);
        notice.setResult(checklistNoticeVO.getRectifyResult());
        if (checklistNoticeVO.getRectifyId() == null) {
            notice.setTaskId(checklistNoticeVO.getId());
            return rectificationNoticeService.addRectificationNotice(notice);
        }
        notice.setId(checklistNoticeVO.getRectifyId());
        return rectificationNoticeService.modifyRectificationNotice(notice);
    }

    @GetMapping(name = "本月任务待办", path = "/tasks/statistics")
    public BaseVo taskStatistics(@RequestParam(defaultValue = "true", required = false) Boolean personal,
                                 HttpServletRequest request) {
        return taskService.taskStatistics(personal, getSourceByHttpServletRequest(request));
    }

    // -------------------------------整改通知书-------------------------------------------------------------------------
    @GetMapping(name = "根据巡检任务查询整改通知书", path = "/rectification-notices/{taskId}")
    public BaseVo getRectificationNoticeByTaskId(@PathVariable("taskId") Long taskId) {
        return rectificationNoticeService.selectByTaskId(taskId);
    }

    @PostMapping(name = "新增整改通知书", path = "/rectification-notices/add")
    public BaseVo addRectificationNotice(@RequestBody @Validated RectificationNotice notice) {
        return rectificationNoticeService.addRectificationNotice(notice);
    }

    @PostMapping(name = "编辑整改通知书", path = "/rectification-notices/modify")
    public BaseVo modifyRectificationNotice(@RequestBody @Validated RectificationNotice notice) {
        return rectificationNoticeService.modifyRectificationNotice(notice);
    }

    @PostMapping(name = "增量查询企业违规违法记录", path = "/rectification-notices/increment")
    public BaseVo incrementRectificationNotices(
            @RequestBody @Validated(IncrementalRequest.IllegalSearch.class) IncrementalRequest incrementalRequest,
            HttpServletRequest httpServletRequest) {
        incrementalRequest.setSource(getSourceByHttpServletRequest(httpServletRequest));
        return rectificationNoticeService.incrementRectificationNotices(incrementalRequest);
    }

    // -------------------------------复查排查单-------------------------------------------------------------------------
    @PostMapping(name = "新增复查排查单", path = "/review-checklists/add")
    public BaseVo addReviewChecklist(@RequestBody @Validated(ReviewChecklist.Add.class) ReviewChecklist reviewChecklist) {
        return taskService.addReviewChecklist(reviewChecklist);
    }

    @GetMapping(name = "根据任务查询复查排查单", path = "/review-checklists/{taskId}")
    public BaseVo getReviewChecklistByTaskId(@PathVariable("taskId") Long taskId) {
        return taskService.selectByRefId(taskId);
    }

    @PostMapping(name = "编辑复查排查单", path = "/review-checklists/modify")
    public BaseVo modifyReviewChecklist(HttpServletRequest httpServletRequest,
                                        @RequestBody @Validated(ReviewChecklist.Modify.class) ReviewChecklist reviewChecklist) {
        return taskService.modifyReviewChecklist(reviewChecklist, getSourceByHttpServletRequest(httpServletRequest));
    }

    // ------------------------ 导出 -----------------------------------------------------------------------------------
    @GetMapping(name = "数据导出", path = "/tasks/{id}/export")
    public BaseVo exportTaskById(@PathVariable("id") Long id) {
        return taskService.exportById(id);
    }
}
