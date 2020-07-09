package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.StudyRecord;
import com.bit.module.pb.vo.StudyRecordVO;
import com.bit.module.pb.vo.StudyVO;

/**
 * @author chenduo
 * @create 2019-01-25 15:14
 */
public interface StudyRecordService {
    /**
     * 学习记录查询
     * @param studyRecordVO
     * @return
     */
    BaseVo listPage(StudyRecordVO studyRecordVO);
    /**
     * 更新学习记录时间
     * @param studyRecord
     * @return
     */
    BaseVo updateStudyTime(StudyRecord studyRecord);


}
