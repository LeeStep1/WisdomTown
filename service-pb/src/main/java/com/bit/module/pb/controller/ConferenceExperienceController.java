package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ConferenceExperience;
import com.bit.module.pb.bean.ExperienceCheckName;
import com.bit.module.pb.bean.ExperienceFile;
import com.bit.module.pb.service.ConferenceExperienceService;
import com.bit.module.pb.vo.ConferenceExperienceVO;
import com.bit.module.pb.vo.ConferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-17 19:11
 */
@RestController
@RequestMapping("/experience")
public class ConferenceExperienceController {

    @Autowired
    private ConferenceExperienceService conferenceExperienceService;

    /**
     * 会议心得新增
     * @param conferenceExperience
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody ConferenceExperience conferenceExperience){
        conferenceExperienceService.add(conferenceExperience);
        return new BaseVo();

    }

    /**
     * 会议心得更新
     * @param conferenceExperience
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody ConferenceExperience conferenceExperience){
        conferenceExperienceService.update(conferenceExperience);
        return new BaseVo();

    }

    /**
     * 会议心得分页查询
     * @param conferenceVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listpage(@RequestBody ConferenceVO conferenceVO){
        return conferenceExperienceService.listpage(conferenceVO);
    }
    /**
     * 查询会议的所有心得
     * @param conferenceExperienceVO
     * @return
     */
    @PostMapping("/queryExperience")
    public BaseVo queryExperience(@RequestBody ConferenceExperienceVO conferenceExperienceVO){

        return conferenceExperienceService.queryExperience(conferenceExperienceVO);

    }

    /**
     * 批量校验文件名称
     * @param experienceCheckName
     * @return
     */
    @PostMapping("/batchCheckExperienceName")
    public BaseVo batchCheckExperienceName(@RequestBody ExperienceCheckName experienceCheckName){
        return conferenceExperienceService.batchCheckExperienceName(experienceCheckName);
    }

    /**
     * 上传会议心得
     * @param experienceFile
     * @return
     */
    @PostMapping("/uploadExperience")
    public BaseVo batchUploadExperience(@RequestBody ExperienceFile experienceFile) throws IOException {
        return conferenceExperienceService.uploadExperience(experienceFile);
    }

}
