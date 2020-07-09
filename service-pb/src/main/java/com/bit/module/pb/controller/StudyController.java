package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.service.StudyService;
import com.bit.module.pb.vo.StudyMemberDetailVO;
import com.bit.module.pb.vo.StudyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Study的相关请求
 * @author zhangjie
 * @date 2019-1-11
 */
@RestController
@RequestMapping("/study")
public class StudyController {
    private static final Logger logger = LoggerFactory.getLogger(StudyController.class);

    @Autowired
    private StudyService studyService;
    /**
     * 新增学习计划
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Study study){
        studyService.add(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改学习计划
     * @param study
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Study study){
        studyService.update(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 删除学习计划
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        studyService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 发布学习计划
     * @param study
     * @return
     */
    @PostMapping("/release")
    public BaseVo release(@RequestBody Study study){
        studyService.release(study);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 镇团委发布学习计划
     * @param id
     * @return
     */
    @GetMapping("/townRelease/{id}")
    public BaseVo townRelease(@PathVariable(value = "id") Long id){
        return studyService.townRelease(id);
    }

    /**
     * app端查看学习计划
     * @param id
     * @return
     */
    @GetMapping("/appReflect/{id}")
    public BaseVo appReflect(@PathVariable(value = "id")Long id){
        return studyService.appReflect(id);
    }

    /**
     * web端基层学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody StudyVO studyVO){
        return studyService.listPage(studyVO);
    }

    /**
     * web端镇党委学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @PostMapping("/webTownlistPage")
    public BaseVo webTownlistPage(@RequestBody StudyVO studyVO){
        return studyService.webTownlistPage(studyVO);
    }

    /**
     * app端首页学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @PostMapping("/appStudyPlanListPage")
    public BaseVo appListPage(@RequestBody StudyVO studyVO){
        return studyService.appStudyPlanListPage(studyVO);
    }
    /**
     * app端我的学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @PostMapping("/appMyStudyPlanListPage")
    public BaseVo appMyStudyPlanListPage(@RequestBody StudyVO studyVO){
        return studyService.appMyStudyPlanListPage(studyVO);
    }


    /**
     * 查看学习情况记录
     * @param studyMemberDetailVO
     * @return
     */
    @PostMapping("/querydetail")
    public BaseVo querydetail(@RequestBody StudyMemberDetailVO studyMemberDetailVO){
        return studyService.querydetail(studyMemberDetailVO);
    }

    /**
     * 导出所有学习计划数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(HttpServletResponse response,
                                     @RequestParam(value = "theme",required = false)String theme,
                                     @RequestParam(value = "startTime",required = false)String startTime,
                                     @RequestParam(value = "endTime",required = false)String endTime,
                                     @RequestParam(value = "isRelease",required = false)Integer isRelease,
                              @RequestParam(value = "pbId",required = false)Long pbId,
                              @RequestParam(value = "excelType",required = true)Integer excelType){
        StudyExcelParam studyExcelParam = new StudyExcelParam();
        studyExcelParam.setTheme(theme);
        studyExcelParam.setStartTime(startTime);
        studyExcelParam.setEndTime(endTime);
        studyExcelParam.setIsRelease(isRelease);
        studyExcelParam.setExcelType(excelType);
        studyExcelParam.setPbId(pbId);
        studyService.exportToExcel(response,studyExcelParam);
    }





    /**
     * app端添加学习计划与图片的关系
     * @param studyRelFileInfo
     * @return
     */
    @PostMapping("/appUploadImage")
    public BaseVo appUploadImage(@RequestBody StudyRelFileInfo studyRelFileInfo){
        return studyService.appUploadImage(studyRelFileInfo);

    }

    /**
     * app端删除学习计划与图片的关系
     * @param id
     * @return
     */
    @GetMapping("/appdelStudyImage/{id}")
    public BaseVo appdelStudyImage(@PathVariable(value = "id") Long id){
        return studyService.appdelStudyImage(id);
    }

    /**
     * web端查看学习计划
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseVo findById(@PathVariable(value = "id")Long id){
        return studyService.findById(id);
    }

}
