package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.service.ConferenceRecordService;
import com.bit.module.pb.vo.ConferenceRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenduo
 * @create 2019-01-25 13:32
 */
@RestController
@RequestMapping("/conferenceRecord")
public class ConferenceRecordController {

    @Autowired
    private ConferenceRecordService conferenceRecordService;

    /**
     * 分页查询
     * @param conferenceRecordVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ConferenceRecordVO conferenceRecordVO){
        return conferenceRecordService.listPage(conferenceRecordVO);
    }
}
