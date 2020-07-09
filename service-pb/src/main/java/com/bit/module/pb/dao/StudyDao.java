package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Study;
import com.bit.module.pb.bean.StudyExcelParam;
import com.bit.module.pb.bean.StudyMemberDetail;
import com.bit.module.pb.vo.StudyMemberDetailVO;
import com.bit.module.pb.vo.StudyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Study的Dao
 * @author zhangjie
 * @date 2019-1-11
 */
@Repository
public interface StudyDao {

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
    void delete(@Param(value = "id") Long id);
    /**
     * 查看学习计划
     */
    Study queryById(@Param(value = "id") Long id);


    /**
     * 查看学习情况记录 分页
     */
    List<StudyMemberDetail> querydetailListPage(StudyMemberDetailVO studyMemberDetailVO);
    /**
     * 查看学习情况记录 不分页
     */
    List<StudyMemberDetail> querydetail(StudyMemberDetail studyMemberDetail);

    /**
     * 查看全部学习情况记录
     */
    List<Study> queryAll();

    /**
     * 分页查询第三级机构
     */
    List<Study> listPage(StudyVO studyVO);
    /**
     * 分页查询第二极机构
     */
    List<Study> listPageForTwo(StudyVO studyVO);
    /**
     * 分页查询第一级机构
     */
    List<Study> listPageForTown(StudyVO studyVO);

    /**
     * 镇党委的学习计划
     */
    List<Study> townStudyListPage(StudyVO studyVO);



    /**
     * 导出excel 1级
     * @param studyExcelParam
     * @return
     */
    List<Study> listPageForTownExcel(StudyExcelParam studyExcelParam);
    /**
     * 导出excel 3级
     * @param studyExcelParam
     * @return
     */
    List<Study> listPageForExcel(StudyExcelParam studyExcelParam);

    /**
     * 导出excel 2级
     */
    List<Study> listPageForTwoExcel(StudyExcelParam studyExcelParam);


    /**
     * 镇党委的学习计划
     */
    List<Study> townStudyListPageForExcel(StudyExcelParam studyExcelParam);

    /**
     * app端我的学习计划分页查询
     * @param studyVO
     * @return
     */
    List<Study> appMyStudyPlanListPage(StudyVO studyVO);

    /**
     * app端首页学习计划分页查询
     * @param studyVO
     * @return
     */
    List<Study> appStudyPlanListPage(StudyVO studyVO);

    /**
     * app端首页学习计划分页查询 管理员
     * @param studyVO
     * @return
     */
    List<Study> appStudyPlanAdminListPage(StudyVO studyVO);
}
