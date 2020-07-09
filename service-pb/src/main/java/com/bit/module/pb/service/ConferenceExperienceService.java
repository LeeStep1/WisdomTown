package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ConferenceExperience;
import com.bit.module.pb.bean.ExperienceCheckName;
import com.bit.module.pb.bean.ExperienceFile;
import com.bit.module.pb.vo.ConferenceExperienceVO;
import com.bit.module.pb.vo.ConferenceVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-17 19:12
 */
public interface ConferenceExperienceService {
    /**
     * 会议心得新增
     * @param conferenceExperience
     * @return
     */
    void add(ConferenceExperience conferenceExperience);
    /**
     * 会议心得更新
     * @param conferenceExperience
     * @return
     */
    void update(ConferenceExperience conferenceExperience);
    /**
     * 会议心得分页查询
     * @param conferenceVO
     * @return
     */
    BaseVo listpage(ConferenceVO conferenceVO);
    /**
     * 查询会议的所有心得
     * @param conferenceExperienceVO
     * @return
     */
    BaseVo queryExperience(ConferenceExperienceVO conferenceExperienceVO);

    /**
     * 批量校验文件名称
     * @param experienceCheckName
     * @return
     */
    BaseVo batchCheckExperienceName(ExperienceCheckName experienceCheckName);

    /**
     * 批量上传会议心得
     * @param experienceFile
     * @return
     */
    BaseVo uploadExperience(ExperienceFile experienceFile) throws IOException;


}
