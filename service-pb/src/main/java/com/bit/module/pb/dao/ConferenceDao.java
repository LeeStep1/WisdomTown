package com.bit.module.pb.dao;

import com.bit.module.pb.bean.*;
import com.bit.module.pb.vo.ConferenceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-01-16 21:16
 */
@Repository
public interface ConferenceDao {

    /**
     * 新增会议记录
     * @param conference
     */
    void add(Conference conference);

    /**
     * 更新会议记录
     * @param conference
     */
    void update(Conference conference);

    /**
     * 删除会议记录
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 单查会议
     * @param id
     * @return
     */
    Conference queryById(@Param(value = "id") Long id);

    /**
     * 分页查询会议记录
     * @param conferenceVO
     * @return
     */
    List<Conference> listPage(ConferenceVO conferenceVO);

    /**
     * 分页查询会议记录 orgtype =3
     * @param conferenceVO
     * @return
     */
    List<Conference> listPageOrgTypeThree(ConferenceVO conferenceVO);

    /**
     * 分页查询会议记录 四级机构
     * @param conferenceVO
     * @return
     */
    List<Conference> listPageLevelFour(ConferenceVO conferenceVO);
    /**
     * 分页查询会议记录 镇党委
     * @param conferenceVO
     * @return
     */
    List<Conference> listPageTown(ConferenceVO conferenceVO);

    /**
     * 根据党组织筛选
     * @param conferenceVO
     * @return
     */
    List<Conference> listPageForTwo(ConferenceVO conferenceVO);

    /**
     * 分页查询会议记录
     * @param conferenceVO
     * @return
     */
    List<Conference> appMyListPage(ConferenceVO conferenceVO);

    /**
     * app分页查询首页三会一课
     * @param conferenceVO
     * @return
     */
    List<Conference> appThreeLessonListPage(ConferenceVO conferenceVO);

    /**
     * app分页查询首页三会一课 三级机构党员使用
     * @param conferenceVO
     * @return
     */
    List<Conference> appThreeLessonAdminListPage(ConferenceVO conferenceVO);



    /**
     * 查询会议签到情况
     * @param conferenceMemberDetail
     * @return
     */
    List<ConferenceMemberDetail> queryRecordsByConferenceId(ConferenceMemberDetail conferenceMemberDetail);

    /**
     * 查询计算出勤率
     * @param id
     * @return
     */
    Integer calculateSignRate(@Param(value = "id") Long id);

    /**
     * 查询全部数据
     * @return
     */
    List<Conference> queryAll();


    /**
     * 查询所有的会议记录
     * @return
     */
    List<Conference> queryAllConferences();

    /**
     * 批量更新记录
     * @param conferenceListForUpdate
     */
    void batchUpdate(@Param(value = "conferenceListForUpdate") List<Conference> conferenceListForUpdate);

    /**
     * 分页查询会议记录
     * @param conferenceExcelParam
     * @return
     */
    List<Conference> listPageForExcel(ConferenceExcelParam conferenceExcelParam);

    /**
     * 导出excel 1级
     * @param conferenceExcelParam
     * @return
     */
    List<Conference> listPageForTownExcel(ConferenceExcelParam conferenceExcelParam);

    /**
     * 导出excel 2级
     * @param conferenceExcelParam
     * @return
     */
    List<Conference> listPageForTwoExcel(ConferenceExcelParam conferenceExcelParam);

    /**
     * 批量更新会议
     * @param updateList
     */
    void batchUpdateConferenceSignRate(@Param(value = "updateList") List<Conference> updateList);

    /**
     * 批量更新会议状态
     * @param updateList
     */
    void batchUpdateConferenceStatus(@Param(value = "updateList") List<Conference> updateList);

    /**
     * 批量更新会议上传率
     * @param updateList
     */
    void batchUpdateConferenceUploadRate(@Param(value = "updateList") List<Conference> updateList);

    /**
     * 按照参数查询
     * @param conference
     * @return
     */
    List<Conference> findByParam(Conference conference);
}
