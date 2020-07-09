package com.bit.module.pb.controller;

import com.alibaba.fastjson.JSON;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Conference;
import com.bit.module.pb.bean.ConferenceExcelParam;
import com.bit.module.pb.bean.SignInfo;
import com.bit.module.pb.bean.StudyRelFileInfo;
import com.bit.module.pb.service.ConferenceService;
import com.bit.module.pb.vo.ConferenceMemberDetailVO;
import com.bit.module.pb.vo.ConferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenduo
 * @create 2019-01-16 20:31
 */
@RestController
@RequestMapping("/meeting")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    /**
     * 新增记录
     * @param conferenceVO
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody ConferenceVO conferenceVO){
        conferenceService.add(conferenceVO);
        return new BaseVo();
    }
    /**
     * 更新记录
     * @param conferenceVO
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody ConferenceVO conferenceVO){
        conferenceService.update(conferenceVO);
        return new BaseVo();
    }

    /**
     * 更新记录状态
     * @param conference
     * @return
     */
    @PostMapping("/release")
    public BaseVo release(@RequestBody Conference conference){
        conferenceService.release(conference);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * app反显或单查记录信息
     * @param id
     * @return
     */
    @GetMapping("/appReflect/{id}")
    public BaseVo appReflect(@PathVariable(value = "id") Long id){
        return conferenceService.appReflect(id);
    }

    /**
     * 反显或单查记录信息
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflect(@PathVariable(value = "id") Long id){
        return conferenceService.reflect(id);
    }


    /**
     * 删除信息
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        conferenceService.delete(id);
        return new BaseVo();
    }

    /**
     * 上传资料
     * @param studyRelFileInfos
     * @return
     */
    @PostMapping("/uploadData")
    public BaseVo uploadData(@RequestBody StudyRelFileInfo studyRelFileInfos){
        return conferenceService.uploadData(studyRelFileInfos);
    }

    /**
     * 删除附件
     * @param studyRelFileInfos
     * @return
     */
    @PostMapping("/delData")
    public BaseVo delData(@RequestBody StudyRelFileInfo studyRelFileInfos){
        return conferenceService.delData(studyRelFileInfos);
    }

    /**
     * 查看会议资料
     * @param conferenceId
     * @return
     */
    @GetMapping("/queryData/{conferenceId}")
    public BaseVo queryData(@PathVariable(value = "conferenceId")Long conferenceId){
        return conferenceService.queryData(conferenceId);
    }


    /**
     * 分页查询和搜索
     * @param conferenceVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ConferenceVO conferenceVO){
        return conferenceService.listPage(conferenceVO);
    }


    /**
     * app分页查询我的会议列表
     * @param conferenceVO
     * @return
     */
    @PostMapping("/appMyListPage")
    public BaseVo appMyListPage(@RequestBody ConferenceVO conferenceVO){
        return conferenceService.appMyListPage(conferenceVO);
    }

    /**
     * app分页查询首页三会一课
     * @param conferenceVO
     * @return
     */
    @PostMapping("/appThreeLessonListPage")
    public BaseVo appThreeLessonListPage(@RequestBody ConferenceVO conferenceVO){
        return conferenceService.appThreeLessonListPage(conferenceVO);
    }


    /**
     * 到课详情
     * @param conferenceMemberDetailVO
     * @return
     */
    @PostMapping("/checkSignDetail")
    public BaseVo checkSignDetail(@RequestBody ConferenceMemberDetailVO conferenceMemberDetailVO){
        return conferenceService.checkSignDetail(conferenceMemberDetailVO);
    }



    /**
     * 根据会议id生成二维码 弃用
     * @param id
     * @return
     */
    @GetMapping("/genQrCode/{id}/{appId}")
    public void genQrCode(@PathVariable(value = "id") Long id,@PathVariable(value = "appId") Long appId, HttpServletResponse response) throws IOException {
        conferenceService.genQrCode(id,appId,response);
    }

    /**
     * 根据会议id生成二维码 返回字符串
     * @param id
     * @return
     */
    @GetMapping("/strgenQrCode/{id}/{appId}")
    public BaseVo strgenQrCode(@PathVariable(value = "id") Long id,@PathVariable(value = "appId") Long appId, HttpServletResponse response) throws IOException {
        return conferenceService.strgenQrCode(id,appId,response);
    }


    /**
     * 人员签到
     * @param signInfo
     * @return
     */
    @PostMapping("/sign")
    public BaseVo sign(@RequestBody SignInfo signInfo,HttpServletRequest request){
        return conferenceService.sign(signInfo,request);
    }


    /**
     * 导出所有会议数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(HttpServletResponse response,
                                @RequestParam(value = "theme",required = false) String theme,
                                @RequestParam(value = "beginTime",required = false) String beginTime,
                                @RequestParam(value = "endTime",required = false) String endTime,
                                @RequestParam(value = "status",required = false) Integer status,
                              @RequestParam(value = "pbId",required = false) Long pbId,
                              @RequestParam(value = "conferenceType",required = false) Integer conferenceType){
        ConferenceExcelParam conferenceExcelParam = new ConferenceExcelParam();
        conferenceExcelParam.setTheme(theme);
        conferenceExcelParam.setBeginTime(beginTime);
        conferenceExcelParam.setEndTime(endTime);
        conferenceExcelParam.setStatus(status);
        conferenceExcelParam.setPbId(pbId);
        conferenceExcelParam.setConferenceType(conferenceType);
        conferenceService.exportToExcel(response,conferenceExcelParam);
    }


    /**
     * service-file删除文件
     */
    @GetMapping("/pbDelFile/{fileId}")
    public void pbDelFile(@PathVariable(value = "fileId") Long fileId){
        conferenceService.pbDelFile(fileId);
    }

    /**
     * 取消会议
     * @param conference
     * @return
     */
    @PostMapping("/cancel")
    public BaseVo cancelById(@RequestBody Conference conference){
        return conferenceService.cancel(conference);
    }

    /**
     * 每日定时更新会议状态
     * @return
     */
    @PostMapping("/dailyUpdateConferenceStatus")
    public BaseVo dailyUpdateConferenceStatus(){
        return conferenceService.dailyUpdateConferenceStatus();
    }
    /**
     * 每日定时更新会议签到率
     * @return
     */
    @PostMapping("/dailyUpdateConferenceSignRate")
    public BaseVo dailyUpdateConferenceSignRate(){
        return conferenceService.dailyUpdateConferenceSignRate();
    }

    /**
     * 每日定时更新会议上传率
     * @return
     */
    @PostMapping("/dailyUpdateConferenceUploadRate")
    public BaseVo dailyUpdateConferenceUploadRate(){
        return conferenceService.dailyUpdateConferenceUploadRate();
    }


    @PostMapping("/print")
    public void print(HttpServletResponse response) throws IOException {
        Conference conference = new Conference();
        conference.setId(1L);
        conference.setVersion(1);
        String s = JSON.toJSONString(conference);
        response.getWriter().write(s);
    }
}
