package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.enums.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 枚举类 api
 * @author decai.liu
 * @create 2019-07-23
 */
@RestController
@RequestMapping("/enums")
public class EnumsController {

    @GetMapping(name = "排查结果", path = "/result")
    public BaseVo listResults() {
        BaseVo baseVo = new BaseVo();
        baseVo.setData(ResultEnum.list);
        return baseVo;
    }

    @GetMapping(name = "任务类型", path = "/task-types")
    public BaseVo listTaskTypes() {
        BaseVo baseVo = new BaseVo();
        baseVo.setData(TaskTypeEnum.list);
        return baseVo;
    }

    @GetMapping(name = "任务执行状态", path = "/task-status")
    public BaseVo listTaskStatus() {
        BaseVo baseVo = new BaseVo();
        baseVo.setData(TaskStatusEnum.list);
        return baseVo;
    }

    @GetMapping(name = "城建项目状态", path = "/project-status")
    public BaseVo listProjectStatus() {
        BaseVo baseVo = new BaseVo();
        baseVo.setData(ProjectStatusEnum.list);
        return baseVo;
    }

    @GetMapping(name = "工程类别", path = "/project-categories")
    public BaseVo listProjectCategories() {
        BaseVo baseVo = new BaseVo();
        baseVo.setData(ProjectCategoryEnum.list);
        return baseVo;
    }
}
