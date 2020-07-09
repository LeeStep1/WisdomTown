package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.module.vol.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenduo
 * @create 2019-03-14 13:20
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/dailyVolJob")
    public void dailyVolJob(){
        jobService.dailyUpdateCamapignStatus();
        jobService.dailuUpdateVolunteer();
        jobService.dailyUpdateStation();
        jobService.dailyUpdateCamapignData();
        jobService.dailyUpdateBoard();
    }

    /**
     * 更新活动状态
     */
    @PostMapping("/dailyUpdateCamapignStatus")
    public void dailyUpdateCamapignStatus(){
        jobService.dailyUpdateCamapignStatus();
    }

    /**
     * 更新志愿者信息
     */
    @PostMapping("/dailuUpdateVolunteer")
    public void dailuUpdateVolunteer(){
        jobService.dailuUpdateVolunteer();
    }

    /**
     * 更新站点信息
     */
    @PostMapping("/dailyUpdateStation")
    public void dailyUpdateStation(){
        jobService.dailyUpdateStation();
    }

    /**
     * 更新志愿者等级
     */
    @PostMapping("/dailyUpdateVolunteerLevel")
    public void dailyUpdateVolunteerLevel(){
        jobService.dailyUpdateVolunteerLevel();
    }

    /**
     * 更新排行榜
     */
    @PostMapping("/dailyUpdateBoard")
    public void dailyUpdateBoard(){
        jobService.dailyUpdateBoard();
    }

    /**
     * 更新活动人数时间和捐款数据
     */
    @PostMapping("/dailyUpdateCamapignData")
    public void dailyUpdateCamapignData(){
        jobService.dailyUpdateCamapignData();
    }

}





