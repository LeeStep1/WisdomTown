package com.bit.module.pb.dao;


import com.bit.module.pb.bean.Conference;
import com.bit.module.pb.bean.ConferenceExperience;
import com.bit.module.pb.bean.ConferenceExperienceFile;
import com.bit.module.pb.vo.ConferenceExperienceVO;
import com.bit.module.pb.vo.ConferenceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-01-17 19:14
 */
@Repository
public interface ConferenceExperienceDao {

    /**
     * 添加会议心得记录
     * @param conferenceExperience
     */
    void add(ConferenceExperience conferenceExperience);

    /**
     * 更新会议心得记录
     * @param conferenceExperience
     */
    void update(ConferenceExperience conferenceExperience);

    /**
     * 分页查询心得记录
     * @param conferenceVO
     * @return
     */
    List<Conference> listPage(ConferenceVO conferenceVO);

    /**
     * 查询党员心得
     * @param conferenceExperienceVO
     * @return
     */
    List<ConferenceExperienceFile> queryExperience(ConferenceExperienceVO conferenceExperienceVO);

    /**
     * 计算心得上传率
     * @param id
     * @return
     */
    Integer calculateUploadRate(@Param(value = "id") Long id);
    /**
     * 根据会议id查询党员id
     * @param conferenceId
     * @return
     */
    List<Long> queryByConferenceId(@Param(value = "conferenceId") Long conferenceId);

    /**
     * 批量新增
     * @param conferenceExperiences
     */
    void batchAdd(@Param(value = "conferenceExperiences") List<ConferenceExperience> conferenceExperiences);

    /**
     * 根据会议id和用户id查询记录
     * @param conferenceExperience
     * @return
     */
    ConferenceExperience findByConferenceIdAndUserId(ConferenceExperience conferenceExperience);

    /**
     * 批量更新心得记录
     * @param passList
     */
    void batchUpdateExperience(@Param(value = "passList") List<ConferenceExperience> passList);
}
