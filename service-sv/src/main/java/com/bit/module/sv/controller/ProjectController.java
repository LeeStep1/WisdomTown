package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.enums.ProjectStatusEnum;
import com.bit.module.sv.service.ProjectService;
import com.bit.module.sv.utils.ExcelHandler;
import com.bit.module.sv.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.bit.module.sv.utils.StringUtils.getSourceByHttpServletRequest;

/**
 * 项目工程 api
 * @author decai.liu
 * @create 2019-08-27
 */
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping(name = "项目危险源清单树形结构", path = "/hazards/tree")
    public BaseVo hazardListTree(){
        return projectService.hazardListTree();
    }

    @GetMapping(name = "项目上传附件清单树形结构", path = "/attachments/tree")
    public BaseVo attachListTree(){
        return projectService.attachListTree();
    }

    @PostMapping(name = "新增工程项目", path = "/add")
    public BaseVo addProject(@RequestBody @Validated(ProjectVO.Add.class) ProjectVO projectVO,
                             HttpServletRequest request){
        projectVO.setSource(getSourceByHttpServletRequest(request));
        projectService.addProject(projectVO);
        return new BaseVo();
    }

    @PostMapping(name = "编辑工程项目", path = "/modify")
    public BaseVo modifyProject(@RequestBody @Validated(ProjectVO.Modify.class) ProjectVO projectVO){
        projectService.modifyProject(projectVO);
        return new BaseVo();
    }

    @GetMapping(name = "工程项目详情", path = "/{id}")
    public BaseVo getProject(@PathVariable("id") Long id, HttpServletRequest request){
        return projectService.findByIdAndSource(id, getSourceByHttpServletRequest(request));
    }

    @PostMapping(name = "工程项目分页查询")
    public BaseVo listProjectsWithPage(@RequestBody ProjectPageQuery pageQuery, HttpServletRequest request){
        pageQuery.setSource(getSourceByHttpServletRequest(request));
        return projectService.listProjectsWithPage(pageQuery);
    }

    @GetMapping(name = "工程项目列表", path = "/list")
    public BaseVo listProjects(@RequestParam(value = "category", required = false) Integer category,
                               @RequestParam(value = "status", required = false) Integer status,
                               HttpServletRequest request){
        return projectService.listProjectsByStatusAndCategoryAndSource(status, category, getSourceByHttpServletRequest(request));
    }

    @DeleteMapping(name = "删除工程项目", path = "/{id}")
    public BaseVo deleteProject(@PathVariable("id") Long id){
        projectService.deleteProject(id);
        return new BaseVo();
    }

    @PostMapping(name = "项目状态流转", path = "/state-flow")
    public BaseVo changeStatus(@RequestBody @Validated(ProjectVO.Modify.class) ProjectVO projectVO){
        projectService.changeStatus(projectVO.getId(), projectVO.getStatus());
        return new BaseVo();
    }

    @PostMapping(name = "暂停项目", path = "/{id}/pause")
    public BaseVo pauseProject(@PathVariable("id") Long id){
        projectService.changeStatus(id, ProjectStatusEnum.PAUSED.value);
        return new BaseVo();
    }

    @PostMapping(name = "继续项目", path = "/{id}/continue")
    public BaseVo continueProject(@PathVariable("id") Long id){
        projectService.changeStatus(id, ProjectStatusEnum.IN_PROGRESS.value);
        return new BaseVo();
    }

    @GetMapping(name = "工程项目统计", path = "/statistics")
    public BaseVo countProjects(HttpServletRequest request){
        return projectService.countProjectsBySource(getSourceByHttpServletRequest(request));
    }

    @PostMapping(name = "导出工程项目", path = "/export")
    public void exportProjects(@RequestBody RequestVO requestVO,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ProjectExportVO> list = projectService.listProjectsByIdsAndSource(
                requestVO.getIds(), getSourceByHttpServletRequest(request));
        ExcelHandler.exportExcelFile(response, list, ProjectExportVO.class, "工程项目");
    }

    @PostMapping(name = "增量查询巡检任务", path = "/increment")
    public BaseVo incrementProjects(@RequestBody IncrementalRequest incrementalRequest,
                                 HttpServletRequest httpServletRequest) {
        incrementalRequest.setSource(getSourceByHttpServletRequest(httpServletRequest));
        return projectService.incrementProjects(incrementalRequest);
    }
}
