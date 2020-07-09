package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Meeting;
import com.bit.module.oa.bean.MeetingExecutor;
import com.bit.module.oa.bean.MeetingSummary;
import com.bit.module.oa.service.MeetingService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.meeting.MeetingExecutorPageVO;
import com.bit.module.oa.vo.meeting.MeetingExecutorVO;
import com.bit.module.oa.vo.meeting.MeetingExportVO;
import com.bit.module.oa.vo.meeting.MeetingVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Meeting的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/meeting")
public class MeetingController {
	private static final Logger logger = LoggerFactory.getLogger(MeetingController.class);
	@Autowired
	private MeetingService meetingService;


    /**
     * 会议记录分页
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody MeetingVO meetingVO) {
    	//分页对象，前台传递的包含查询的参数

        return meetingService.findByConditionPage(meetingVO);
    }

    /**
     * 会议信息导出
     * @param title
     * @param startTime
     * @param endTime
     * @param status
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestParam(name = "title", required = false) String title,
                       @RequestParam(name = "startTime", required = false) Long startTime,
                       @RequestParam(name = "endTime", required = false) Long endTime,
                       @RequestParam(name = "status", required = false) Integer status,
                       HttpServletResponse response) {
        try {
            Meeting meeting = setProperty(title, startTime, endTime, status);

            List<MeetingExportVO> exportVO = meetingService.exportByCondition(meeting);
            AtomicLong num = new AtomicLong(1L);
            exportVO.forEach(vo-> vo.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, exportVO, MeetingExportVO.class, "历史会议信息");
        } catch (IOException e) {
            logger.error("历史会议管理导出异常 : {}", e);
        }
    }

    /**
     * 会议审批分页
     *
     */
    @PostMapping("/auditPage")
    public BaseVo auditPage(@RequestBody MeetingVO meetingVO) {
    	//分页对象，前台传递的包含查询的参数

        return meetingService.findAuditResultByConditionPage(meetingVO);
    }

    /**
     * 会议审核导出
     * @param title
     * @param startTime
     * @param endTime
     * @param status
     * @param response
     */
    @PostMapping("/audit/export")
    public void auditExport(@RequestParam(name = "title", required = false) String title,
                       @RequestParam(name = "startTime", required = false) Long startTime,
                       @RequestParam(name = "endTime", required = false) Long endTime,
                       @RequestParam(name = "status", required = false) Integer status,
                       HttpServletResponse response) {
        try {
            Meeting meeting = setProperty(title, startTime, endTime, status);

            List<MeetingExportVO> exportVO = meetingService.exportAuditByCondition(meeting);
            AtomicLong num = new AtomicLong(1L);
            exportVO.forEach(vo-> vo.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, exportVO, MeetingExportVO.class, "会议审批导出信息");
        } catch (IOException e) {
            logger.error("会议审核管理导出异常 : {}", e);
        }
    }

    private Meeting setProperty(String title, Long startTime, Long endTime, Integer status) {
        Meeting meeting = new Meeting();
        meeting.setTitle(title);
        meeting.setStartTime(startTime == null ? null : new Date(startTime));
        meeting.setEndTime(endTime == null ? null : new Date(endTime));
        meeting.setStatus(status);
        return meeting;
    }

    /**
     * 执行会议分页
     * @param meetingExecutor
     * @return
     */
    @PostMapping("/executor/listPage")
    public BaseVo executorPage(@RequestBody MeetingExecutorVO meetingExecutor) {
        return meetingService.findByExecutorPage(meetingExecutor);
    }

    /**
     * 根据主键ID查询Meeting
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Meeting meeting = meetingService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(meeting);
		return baseVo;
    }
    /**
     * 新增Meeting
     *
     * @param meeting Meeting实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Meeting meeting) {
        meeting.checkAdd();
        meetingService.add(meeting);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Meeting
     *
     * @param meeting Meeting实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Meeting meeting) {
        CheckUtil.notNull(meeting.getId());
		meetingService.update(meeting);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除Meeting
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        meetingService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }


    /**
     * 发布会议任务
     *
     * @param meeting Meeting实体
     * @return
     */
    @PostMapping("/publish")
    public BaseVo publish(@RequestBody Meeting meeting) {

        meetingService.publish(meeting);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 驳回会议
     */
    @PostMapping("/reject")
    public BaseVo reject(@RequestBody MeetingVO meeting) {
        meetingService.reject(meeting);
        return new BaseVo();
    }

    /**
     * 定时驳回会议
     */
    @PostMapping("/invalid")
    public BaseVo invalid(@RequestBody Long id) {
        meetingService.invalid(id);
        return new BaseVo();
    }

    /**
     * 审核会议
     */
    @PostMapping("/audit")
    public BaseVo audit(@RequestBody Meeting meeting) {
        meetingService.audit(meeting);
        return new BaseVo();
    }

    /**
     * 取消会议
     */
    @PostMapping("/cancel")
    public BaseVo cancel(@RequestBody Meeting meeting) {
        meetingService.cancel(meeting);
        return new BaseVo();
    }

    /**
     * 延时队列调用，开始会议
     */
    @PostMapping("/start")
    public BaseVo start(@RequestBody Long id) {
        meetingService.start(id);
        return new BaseVo();
    }

    /**
     * 延时队列调用，结束会议
     */
    @PostMapping("/end")
    public BaseVo end(@RequestBody Long id) {
        meetingService.end(id);
        return new BaseVo();
    }

    /**
     * 会议签到
     * @param meetingExecutor
     * @return
     */
    @PostMapping("/checkIn")
    public BaseVo checkIn(@RequestBody MeetingExecutor meetingExecutor) {
        CheckUtil.notNull(meetingExecutor.getMeetingId());
        meetingService.checkIn(meetingExecutor);
        return new BaseVo();
    }

    /**
     * 会议签到详情
     * @param meetingId
     * @return
     */
    @GetMapping("/checkIn/{meetingId}/detail")
    public BaseVo checkInDetail(@PathVariable("meetingId") Long meetingId) {
        MeetingExecutorPageVO executor = meetingService.findCheckInExecutorByMeetingId(meetingId);
        return new BaseVo(executor);
    }

    /**
     * 提交会议离线签到表
     * @param meeting
     * @return
     */
    @PostMapping("/checkIn/offline/submit")
    public BaseVo checkInOffline(@RequestBody Meeting meeting) {
        CheckUtil.notNull(meeting.getId());
        meetingService.updateCheckInOffline(meeting);
        return new BaseVo();
    }

    /**
     * 会议摘要
     * @param meetingId
     * @return
     */
    @GetMapping("/summary/{meetingId}")
    public BaseVo findSummaryByMeetingId(@PathVariable("meetingId") Long meetingId) {
        return new BaseVo(meetingService.findSummaryByMeetingId(meetingId));
    }

    /**
     * 新增会议摘要
     * @param meetingSummary
     * @return
     */
    @PostMapping("/summary/add")
    public BaseVo addSummary(@RequestBody MeetingSummary meetingSummary) {
        meetingService.addSummary(meetingSummary);
        return new BaseVo();
    }

    /**
     * 更新会议摘要
     * @param meetingSummary
     * @return
     */
    @PostMapping("/summary/update")
    public BaseVo updateSummary(@RequestBody MeetingSummary meetingSummary) {
        meetingService.updateSummary(meetingSummary);
        return new BaseVo();
    }
}
