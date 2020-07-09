package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.StudyRecord;
import com.bit.module.pb.service.StudyRecordService;
import com.bit.module.pb.vo.StudyRecordVO;
import com.bit.module.pb.vo.StudyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenduo
 * @create 2019-01-25 15:13
 */
@RestController
@RequestMapping("/studyRecord")
public class StudyRecordController {

    @Autowired
    private StudyRecordService studyRecordService;

    /**
     * 学习记录查询
     * @param studyRecordVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody StudyRecordVO studyRecordVO){
        return studyRecordService.listPage(studyRecordVO);
    }

    /**
     * 更新学习记录时间
     * @param studyRecord
     * @return
     */
    @PostMapping("/updateStudyTime")
    public BaseVo updateStudyTime(@RequestBody StudyRecord studyRecord){
        return studyRecordService.updateStudyTime(studyRecord);
    }
}
