package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.service.MeetingService;
import com.bit.module.oa.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Date ： 2019/3/7 13:27
 */
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private TaskService taskService;

    /**
     * 日程列表
     * @param schedule
     * @return
     */
    @PostMapping("/list")
    public BaseVo schedule(@RequestBody Schedule schedule) {
        if (ScheduleType.MEETING.getKey().equals(schedule.getType())) {
            return new BaseVo(meetingService.findScheduleMeeting(schedule));
        }
        if (ScheduleType.TASK.getKey().equals(schedule.getType())) {
            return new BaseVo(taskService.findScheduleTask(schedule));
        }
        Set<Schedule> scheduleSet = new LinkedHashSet<>();
        scheduleSet.addAll(meetingService.findScheduleMeeting(schedule));
        scheduleSet.addAll(taskService.findScheduleTask(schedule));
        scheduleSet = scheduleSet.stream().sorted(Comparator.comparing(Schedule::getStartTime)).collect(
                Collectors.toCollection(LinkedHashSet::new));
        return new BaseVo(scheduleSet);
    }

    enum ScheduleType {
        MEETING(1, "会议"), TASK(2, "任务");
        Integer key;
        String value;

        ScheduleType(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }
    }
}
