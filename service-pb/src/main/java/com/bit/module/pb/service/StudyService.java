package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.vo.StudyMemberDetailVO;
import com.bit.module.pb.vo.StudyVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Study的Service
 */
public interface StudyService {
    /**
     * 新增学习计划
     */
    void add(Study study);
    /**
     * 修改学习计划
     */
    void update(Study study);
    /**
     * 删除学习计划
     */
    void delete(Long id);
    /**
     * 发布学习计划
     */
    void release(Study study);

    /**
     * 镇团委发布学习计划
     * @param id
     * @return
     */
    BaseVo townRelease(Long id);
    /**
     * 查看学习计划
     */
    BaseVo appReflect(Long id);
    /**
     * 分页查询和根据条件查询（主题、发布时间范围、发布状态）共用接口
     */
    BaseVo listPage(StudyVO studyVO);

    /**
     * 分页查询和根据条件查询（主题、发布时间范围、发布状态）web端镇党委学习计划
     */
    BaseVo webTownlistPage(StudyVO studyVO);

    /**
     * 分页查询和根据条件查询（主题、发布时间范围、发布状态）app端学习计划
     */
    BaseVo appStudyPlanListPage(StudyVO studyVO);
    /**
     * app端我的学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    BaseVo appMyStudyPlanListPage(StudyVO studyVO);
    /**
     * 查看学习情况记录
     */
    BaseVo querydetail(StudyMemberDetailVO studyMemberDetailVO);

    /**
     * 导出所有学习计划数据到excel
     * @return
     */
    void exportToExcel(HttpServletResponse response,StudyExcelParam studyExcelParam);



    /**
     * app端删除学习计划与图片的关系
     * @param id
     * @return
     */
    BaseVo appdelStudyImage(Long id);
    /**
     * 添加学习计划与图片的关系
     * @param studyRelFileInfo
     * @return
     */
    BaseVo appUploadImage(StudyRelFileInfo studyRelFileInfo);

    /**
     * 单查记录
     * @param id
     * @return
     */
    BaseVo findById(Long id);



}
