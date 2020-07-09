package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.vo.ConferenceRecordVO;

/**
 * @author chenduo
 * @create 2019-01-25 14:04
 */
public interface ConferenceRecordService {
    /**
     * 分页查询
     * @param conferenceRecordVO
     * @return
     */
    BaseVo listPage(ConferenceRecordVO conferenceRecordVO);
}
