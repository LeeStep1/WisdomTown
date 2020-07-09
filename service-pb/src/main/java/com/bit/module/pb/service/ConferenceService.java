package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Conference;
import com.bit.module.pb.bean.ConferenceExcelParam;
import com.bit.module.pb.bean.SignInfo;
import com.bit.module.pb.bean.StudyRelFileInfo;
import com.bit.module.pb.vo.ConferenceMemberDetailVO;
import com.bit.module.pb.vo.ConferenceVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenduo
 * @create 2019-01-16 21:12
 */
public interface ConferenceService {
    /**
     * 新增记录
     * @param conferenceVO
     * @return
     */
    void add(ConferenceVO conferenceVO);
    /**
     * 更新记录
     * @param conferenceVO
     * @return
     */
    void update(ConferenceVO conferenceVO);
    /**
     * 更新记录状态
     * @param conference
     * @return
     */
    void release(Conference conference);
    /**
     * 反显或单查记录信息
     * @param id
     * @return
     */
    BaseVo reflect(Long id);

    /**
     * 反显或单查记录信息
     * @param id
     * @return
     */
    BaseVo appReflect(Long id);

    /**
     * 删除信息
     * @param id
     * @return
     */
    void delete(Long id);

    /**
     * 上传资料
     * @param studyRelFileInfos
     * @return
     */
    BaseVo uploadData(StudyRelFileInfo studyRelFileInfos);

    /**
     * 删除附件
     * @param studyRelFileInfos
     * @return
     */
    BaseVo delData(StudyRelFileInfo studyRelFileInfos);
    /**
     * 查看会议资料
     * @param conferenceId
     * @return
     */
    BaseVo queryData(Long conferenceId);

    /**
     * 分页查询和搜索
     * @param conferenceVO
     * @return
     */
    BaseVo listPage(ConferenceVO conferenceVO);



    /**
     * app分页查询我的会议列表
     * @param conferenceVO
     * @return
     */
    BaseVo appMyListPage(ConferenceVO conferenceVO);

    /**
     * app分页查询首页三会一课
     * @param conferenceVO
     * @return
     */
    BaseVo appThreeLessonListPage(ConferenceVO conferenceVO);



    /**
     * 到课详情
     * @param conferenceMemberDetailVO
     * @return
     */
    BaseVo checkSignDetail(@RequestBody ConferenceMemberDetailVO conferenceMemberDetailVO);
    /**
     * 根据会议id生成二维码
     * @param id
     * @return
     */
    void genQrCode(Long id,Long appId, HttpServletResponse response) throws IOException;

    /**
     * 根据会议id生成二维码 返回字符串
     * @param id
     * @return
     */
    BaseVo strgenQrCode(Long id,Long appId, HttpServletResponse response) throws IOException;
    /**
     * 人员签到
     * @param signInfo
     * @return
     */
    BaseVo sign(SignInfo signInfo,HttpServletRequest request);

    /**
     * 导出会议记录
     */
    void exportToExcel(HttpServletResponse response,ConferenceExcelParam conferenceExcelParam);

    /**
     * service-file删除文件
     */
    void pbDelFile(Long fileId);

    /**
     * 取消会议
     * @param conference
     * @return
     */
    BaseVo cancel(Conference conference);

    /**
     * 每日定时更新会议状态
     * @return
     */
    BaseVo dailyUpdateConferenceStatus();
    /**
     * 每日定时更新会议签到率
     * @return
     */
    BaseVo dailyUpdateConferenceSignRate();

    /**
     * 每日定时更新会议上传率
     * @return
     */
    BaseVo dailyUpdateConferenceUploadRate();
}
