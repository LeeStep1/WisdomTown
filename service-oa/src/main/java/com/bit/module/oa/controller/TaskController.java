package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.TaskFeedback;
import com.bit.module.oa.service.TaskService;
import com.bit.module.oa.service.impl.TaskServiceImpl;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 任务
 *
 * @autor xiaoyu.fang
 * @date 2019/2/14 13:16
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 新增任务或子任务
     *
     * @param taskParam
     * @return
     */
    @PostMapping("/create")
    public BaseVo createTask(@Validated @RequestBody TaskParam taskParam) {
        taskService.add(taskParam);
        return new BaseVo();
    }

    /**
     * 查看任务
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseVo findTaskById(@Valid @PathVariable("id") Long id) {
        FullTask fullTask = taskService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(fullTask);
        return baseVo;
    }

    /**
     * 负责人/分配人/执行人/抄送人分页接口
     *
     * @param type      1:负责人；2：分配人；3：执行人；4：抄送人
     * @param taskQuery
     * @return
     */
    @PostMapping("/common/{type}/query")
    public BaseVo queryTask(@PathVariable(value = "type") Integer type, @RequestBody TaskQuery taskQuery) {
        BaseVo baseVo = new BaseVo();
        switch (TaskServiceImpl.TaskTypeEnum.getByValue(type)) {
            case PRINCIPAL:
                baseVo = taskService.findPrincipalByConditionPage(taskQuery, false);
                break;
            case ASSIGNER:
                baseVo = taskService.findAssignerByConditionPage(taskQuery, false);
                break;
            case EXECUTOR:
                baseVo = taskService.findByExecutorConditionPage(taskQuery, false);
                break;
            case CC:
                baseVo = taskService.findByCcConditionPage(taskQuery, false);
                break;
            default:
                break;
        }
        return baseVo;
    }

    /**
     * 获取子任务分页
     *
     * @param taskQuery
     * @return
     */
    @PostMapping("/subtask/query")
    public BaseVo queryTaskChildren(@Validated(TaskQuery.FindChildren.class) @RequestBody TaskQuery taskQuery) {
        return taskService.findTaskChildrenByTaskId(taskQuery);
    }

    /**
     * 获取分配人、执行人的子节点
     *
     * @param type
     * @param superiorId
     * @return
     */
    @GetMapping("/subtask/{type}/{superiorId}")
    public BaseVo getTaskChildren(@NotNull @PathVariable(value = "type") Integer type, @NotNull @PathVariable(value = "superiorId") Long superiorId) {
        List<SimpleTask> simpleTasks = new ArrayList<>();
        switch (TaskServiceImpl.TaskTypeEnum.getByValue(type)) {
            case ASSIGNER:
                simpleTasks = taskService.findAssignerChildren(superiorId);
                break;
            case EXECUTOR:
                simpleTasks = taskService.findExecutorChildren(superiorId);
                break;
            default:
                break;
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(simpleTasks);
        return baseVo;
    }


    /**
     * 新增反馈
     *
     * @param taskFeedback
     * @return
     */
    @PostMapping("/feedback/create")
    public BaseVo createTaskFeedback(@Valid @RequestBody TaskFeedback taskFeedback) {
        taskService.createTaskFeedback(taskFeedback);
        return new BaseVo();
    }

    /**
     * 根据ID 获取反馈信息
     *
     * @param id
     * @return
     */
    @GetMapping("/feedback/{id}")
    public BaseVo getTaskFeedbackById(@PathVariable(value = "id") Long id) {
        TaskFeedback taskFeedback = taskService.findTaskFeedbackById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(taskFeedback);
        return baseVo;
    }

    /**
     * 反馈分页
     *
     * @param taskQuery
     * @return
     */
    @PostMapping("/feedback/query")
    public BaseVo findByTaskFeedbackByTaskId(@RequestBody TaskQuery taskQuery) {
        return taskService.findTaskFeedbackByConditionPage(taskQuery);
    }

    /**
     * 终止任务
     *
     * @param taskQuery
     * @return
     */
    @PostMapping("/termination")
    public BaseVo termination(@Validated @RequestBody TaskQuery taskQuery) {
        taskService.terminationById(taskQuery.getTaskId(), taskQuery.getReason());
        return new BaseVo();
    }

    /**
     * 导出Task
     *
     * @param type
     * @param taskName
     * @param taskStatus
     * @param taskType
     * @param taskStartAt
     * @param taskEndAt
     * @param response
     * @throws IOException
     * @throws ParseException
     */
    @PostMapping("/common/{type}/export")
    public void exportTask(@NotNull @PathVariable("type") Integer type, String taskName, Integer taskStatus, Integer taskType, Long taskStartAt, Long taskEndAt, HttpServletResponse response) throws IOException, ParseException {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setTaskName(taskName);
        taskQuery.setTaskStatus(taskStatus);
        taskQuery.setTaskType(taskType);
        Integer no = 0;
        if (taskStartAt == null || taskEndAt == null) {
            taskQuery.setTaskStartAt(getLastMonDate());
            taskQuery.setTaskEndAt(getNowDateShort());
        } else {
            taskQuery.setTaskStartAt(new Date(taskStartAt));
            taskQuery.setTaskEndAt(new Date(taskEndAt));
        }
        BaseVo baseVo = new BaseVo();
        switch (TaskServiceImpl.TaskTypeEnum.getByValue(type)) {
            case PRINCIPAL:
                baseVo = taskService.findPrincipalByConditionPage(taskQuery, true);
                break;
            case ASSIGNER:
                baseVo = taskService.findAssignerByConditionPage(taskQuery, true);
                break;
            case EXECUTOR:
                baseVo = taskService.findByExecutorConditionPage(taskQuery, true);
                break;
            case CC:
                baseVo = taskService.findByCcConditionPage(taskQuery, true);
                break;
            default:
                break;
        }
        List<SimpleTask> simpleTasks = (List<SimpleTask>) baseVo.getData();
        List<TaskExportTemplate> taskExportTemplates = new ArrayList<>();
        for (int i = 0; i < simpleTasks.size(); i++) {
            SimpleTask simpleTask = simpleTasks.get(i);
            TaskExportTemplate taskExportTemplate = buildTaskExportTemplate(simpleTask, no);
            taskExportTemplates.add(taskExportTemplate);
            if (simpleTask.getSuperiorId() != null) {
                TaskQuery taskQuery1 = new TaskQuery();
                taskQuery1.setTaskId(simpleTask.getId());
                List<SimpleTask> children = taskService.findTaskChildren(taskQuery1);
                children.stream().forEach(simpleTask1 -> {
                    TaskExportTemplate taskExportTemplate2 = buildTaskExportTemplate(simpleTask1, null);
                    taskExportTemplates.add(taskExportTemplate2);
                });
            }
            no += 1;
        }
        String title = TaskServiceImpl.TaskTypeEnum.getByValue(type).getPhrase();
        ExcelHandler.exportExcelFile(response, taskExportTemplates, TaskExportTemplate.class, title);
    }

    private TaskExportTemplate buildTaskExportTemplate(SimpleTask simpleTask, Integer no) {
        TaskExportTemplate taskExportTemplate = new TaskExportTemplate();
        taskExportTemplate.setNo(no == null ? null : (no + 1));
        taskExportTemplate.setTaskNo(simpleTask.getTaskNo());
        taskExportTemplate.setTaskName(simpleTask.getTaskName());
        taskExportTemplate.setTaskPrincipalName(simpleTask.getTaskPrincipalName());
        taskExportTemplate.setTaskStatus(simpleTask.getTaskStatus());
        taskExportTemplate.setTaskType(simpleTask.getTaskType());
        taskExportTemplate.setTaskStartAt(simpleTask.getTaskStartAt());
        taskExportTemplate.setTaskEndAt(simpleTask.getTaskEndAt());
        return taskExportTemplate;
    }

    /**
     * 获取上个月时间
     *
     * @return
     * @throws ParseException
     */
    private Date getLastMonDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        return format.parse(mon);
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    private Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }
}
