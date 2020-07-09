package com.bit.module.pb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.consts.ApplicationTypeEnum;
import com.bit.common.enumerate.*;

import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.feign.FileServiceFeign;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.ConferenceService;
import com.bit.module.pb.vo.ConferenceMemberDetailVO;
import com.bit.module.pb.vo.ConferenceVO;
import com.bit.module.pb.vo.FileInfoVO;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.BarcodeFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenduo
 * @create 2019-01-16 21:13
 */
@Service("conferenceService")
@Slf4j
public class ConferenceImpl extends BaseService implements ConferenceService {

    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private StudyRelFileInfoDao studyRelFileInfoDao;
    @Autowired
    private PartyMemberDao partyMemberDao;
    @Autowired
    private ConferenceRecordDao conferenceRecordDao;
    @Autowired
    private ConferenceExperienceDao conferenceExperienceDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private StudyDao studyDao;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private RateUtil rateUtil;
    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
    private StatusUtil statusUtil;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;


    /**
     * 新增记录
     *
     * @param conferenceVO
     * @return
     */
    @Override
    @Transactional
    public void add(ConferenceVO conferenceVO) {
        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        Conference conference = new Conference();
        BeanUtils.copyProperties(conferenceVO, conference);
        String startTime = conferenceVO.getStartTime();
        Integer lasttime = conferenceVO.getLastTime();
        try {
            conference.setStartTime(DateUtil.parse(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        conference.setStatus(ConferenceStatusEnum.STATUS_DRAFT.getCode());
        //设置党组织
        conference.setPbId(userInfo.getPbOrgId());
        conference.setCreateUserId(userInfo.getId());
        conference.setCreateUserName(userInfo.getUsername());
        conference.setCreateRealName(userInfo.getRealName());
        conference.setCreateTime(new Date());

        List<Long> orgids = new ArrayList<>();
        List<Long> memberIds = new ArrayList<>();

        //如果全选了
        if (conferenceVO.getAllOrgId() != null) {
            String s = RadixUtil.toFullBinaryString(Long.valueOf(pbOrgId));
            int level = RadixUtil.getlevel(s);
            if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()) {
                List<PartyMember> membersByBiggerThanOrgId = partyMemberDao.findMembersByBiggerThanOrgId(Long.valueOf(pbOrgId));
                for (PartyMember partyMember : membersByBiggerThanOrgId) {
                    memberIds.add(partyMember.getId());
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()) {
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(pbOrgId, true);
                for (Organization organization : subordinatesById) {
                    orgids.add(Long.valueOf(organization.getId()));
                }
                List<PartyMember> partyMembers = partyMemberDao.batchFindMemberByOrgIds(orgids);
                for (PartyMember partyMember : partyMembers) {
                    memberIds.add(partyMember.getId());
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()) {
                List<Long> memberIdsByOrgId = partyMemberDao.findMemberIdsByOrgId(Long.valueOf(pbOrgId));
                memberIds.addAll(memberIdsByOrgId);
            }

            if (CollectionUtils.isNotEmpty(memberIds)) {
                StringBuffer attendPersons = new StringBuffer(memberIds.size());
                for (Long memberId : memberIds) {
                    attendPersons = attendPersons.append(memberId);
                    attendPersons = attendPersons.append(",");
                }
                String persons = attendPersons.toString();
                persons = persons.substring(0, persons.length() - 1);
                conference.setAttendPerson(persons);
            }
        }

        try {
            Date sd = DateUtil.parse(startTime);
            Long sdtime = sd.getTime();
            Long end = sdtime + lasttime;
            Date ed = new Date(end);
            conference.setEndTime(ed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //设置出勤率
        conference.setSignRate(Const.SIGN_RATE_DEFAULT);
        //设置上传率
        conference.setUploadRate(Const.UPLOAD_RATE_DEFAULT);
        conference.setVersion(Const.INIT_VERSION);
        conferenceDao.add(conference);
    }

    /**
     * 更新记录
     *
     * @param conferenceVO
     * @return
     */
    @Override
    @Transactional
    public void update(ConferenceVO conferenceVO) {

        Conference conference = new Conference();
        BeanUtils.copyProperties(conferenceVO, conference);
        try {
            conference.setStartTime(DateUtil.parse(conferenceVO.getStartTime()));
            Date sd = DateUtil.parse(conferenceVO.getStartTime());
            Long sdtime = sd.getTime();
            Long end = sdtime + conferenceVO.getLastTime();
            Date ed = new Date(end);
            conference.setEndTime(ed);
            conference.setLastTime(conferenceVO.getLastTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        conference.setUpdateUserId(userInfo.getId());
        conference.setUpdateUserName(userInfo.getUsername());
        conference.setUpdateRealName(userInfo.getRealName());
        conference.setUpdateTime(new Date());

        List<Long> orgids = new ArrayList<>();
        List<Long> memberIds = new ArrayList<>();

        //如果全选了
        if (conferenceVO.getAllOrgId() != null) {
            String s = RadixUtil.toFullBinaryString(Long.valueOf(pbOrgId));
            int level = RadixUtil.getlevel(s);
            if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()) {
                List<PartyMember> membersByBiggerThanOrgId = partyMemberDao.findMembersByBiggerThanOrgId(Long.valueOf(pbOrgId));
                if (CollectionUtils.isNotEmpty(membersByBiggerThanOrgId)) {
                    for (PartyMember partyMember : membersByBiggerThanOrgId) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()) {
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(pbOrgId, true);
                if (CollectionUtils.isNotEmpty(subordinatesById)) {
                    for (Organization organization : subordinatesById) {
                        orgids.add(Long.valueOf(organization.getId()));
                    }
                }
                List<PartyMember> partyMembers = partyMemberDao.batchFindMemberByOrgIds(orgids);
                if (CollectionUtils.isNotEmpty(partyMembers)) {
                    for (PartyMember partyMember : partyMembers) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()) {
                List<Long> memberIdsByOrgId = partyMemberDao.findMemberIdsByOrgId(Long.valueOf(pbOrgId));
                if (CollectionUtils.isNotEmpty(memberIdsByOrgId)) {
                    memberIds.addAll(memberIdsByOrgId);
                }
            }

            if (CollectionUtils.isNotEmpty(memberIds)) {
                StringBuffer attendPersons = new StringBuffer(memberIds.size());
                for (Long memberId : memberIds) {
                    attendPersons = attendPersons.append(memberId);
                    attendPersons = attendPersons.append(",");
                }
                String persons = attendPersons.toString();
                persons = persons.substring(0, persons.length() - 1);
                conference.setAttendPerson(persons);
            }
        }
        conferenceDao.update(conference);
    }

    /**
     * 更新记录状态
     *
     * @param conference
     * @return
     */
    @Override
    @Transactional
    public void release(Conference conference) {

        Conference obj = conferenceDao.queryById(conference.getId());
        if (!obj.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
            throwBusinessException("该记录不是草稿不能发布");
        }
        if (obj != null) {
            //判断会议时间是否过期
            Date startTime = obj.getStartTime();
            Date now = new Date();
            if (startTime.before(now)) {
                throwBusinessException("会议已过期");
            }

            Conference cf = new Conference();
            cf.setId(conference.getId());
            cf.setReleaseTime(new Date());
            cf.setStatus(conference.getStatus());
            UserInfo userInfo = getCurrentUserInfo();
            cf.setUpdateUserId(userInfo.getId());
            cf.setUpdateTime(new Date());
            cf.setUpdateUserName(userInfo.getUsername());
            cf.setUpdateRealName(userInfo.getRealName());
            cf.setVersion(obj.getVersion());
            conferenceDao.update(cf);
            if (StringUtil.isEmpty(obj.getAttendPerson())) {
                throwBusinessException("没选择参会人员");
            }
            List<Long> memberIds = Arrays.asList(obj.getAttendPerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            //如果需要签到
            if (obj.getIsSign().equals(BooleanEnum.YES.getCode())) {
                //批量添加到conferenceRecord表中
                List<ConferenceRecord> conferenceRecords = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    for (Long aLong : memberIds) {
                        ConferenceRecord cr = new ConferenceRecord();
                        cr.setUserId(aLong);
                        cr.setConferenceId(conference.getId());
                        cr.setSignSituation(BooleanEnum.NO.getCode());
                        cr.setSignCondition(null);
                        cr.setSignTime(null);
                        cr.setLateReason(null);
                        conferenceRecords.add(cr);
                    }
                    conferenceRecordDao.batchAdd(conferenceRecords);
                }
            }

            //如果需要上传心得
            if (obj.getIsUploadExperience().equals(BooleanEnum.YES.getCode())) {
                //批量添加到conferenceExperience表中
                List<ConferenceExperience> conferenceExperienceList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    for (Long aLong : memberIds) {
                        ConferenceExperience conferenceExperience = new ConferenceExperience();
                        conferenceExperience.setConferenceId(obj.getId());
                        conferenceExperience.setUserId(aLong);
                        conferenceExperience.setFileDetail("");
                        conferenceExperience.setFileId(null);
                        conferenceExperience.setUploadTime(null);
                        conferenceExperience.setStatus(StatusEnum.NO.getCode());
                        conferenceExperienceList.add(conferenceExperience);
                    }
                    conferenceExperienceDao.batchAdd(conferenceExperienceList);
                }
            }

            //是发布状态并且需要通知
            if (conference.getStatus().equals(StudyReleaseStatusEnum.RELEASED.getCode()) && obj.getIsNotice().equals(BooleanEnum.YES.getCode())) {
                // 发送mq消息
              /*  MqMessage mqMessage = new MqMessage();
                mqMessage.setBusinessId(conference.getId());
                mqMessage.setTemplateId(Long.valueOf(MessageTemplateEnum.THREE_LESSION.getId()));*/
                //确定推送的人员
                List<String> idcards = partyMemberDao.batchSelectByIds(memberIds);
                if (idcards != null && idcards.size() > 0)/*{
                    List<Long> longList = sysServiceFeign.batchSelectByCardId(idcards);
                    if (longList!=null && longList.size()>0){
                        Long[] targetId = new Long[longList.size()];
                        longList.toArray(targetId);
                        mqMessage.setTargetId(targetId);
                        mqMessage.setTargetUserType(TargetTypeEnum.ALL.getCode()+"");
                        Organization byId = organizationDao.findById(obj.getPbId().toString());
                        if (byId!=null){
                            mqMessage.setCreater(byId.getName());
                        }
                        mqMessage.setTargetType(TargetTypeEnum.USER.getCode());
                        String[] params = {DateUtil.format(obj.getStartTime()),obj.getPlace()};
                        mqMessage.setParams(params);
                       // mqMessageUtil.assembleMqMessage(rabbitTemplate,mqMessage);
                    }
                }*/ {
                    List<Long> longList = sysServiceFeign.batchSelectByCardId(idcards);
                    if (longList != null && longList.size() > 0) {
                        Long[] targetId = new Long[longList.size()];
                        longList.toArray(targetId);
                        Organization byId = organizationDao.findById(obj.getPbId().toString());
                        String creater = null;
                        if (byId != null) {
                            creater = byId.getName();
                        }
                        String[] params = {DateUtil.format(obj.getStartTime()), obj.getPlace()};
                        pushUserMessageByAlias(MessageTemplateEnum.THREE_LESSION,targetId, params, creater);
                    }
                }
            }
        }
    }
    /**
     * @description: 发送MQ消息提醒
     * @author liyujun
     * @date 2020-04-15
     * @param targetId :
     * @param params :
     * @param creater :
     * @return : void
     */
    private void pushUserMessageByAlias(MessageTemplateEnum messageTemplateEnum, Long[] targetId, String[] params, String creater) {
        MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(messageTemplateEnum, targetId, params, creater, new Date(), null);
        sendMqPushUtil.sendMqMessage(mqSendMessage);
    }

    /**
     * 反显或单查记录信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo reflect(Long id) {
        Conference obj = conferenceDao.queryById(id);
        if (obj == null) {
            throwBusinessException("该记录不存在");
        }
        Organization byId = organizationDao.findById(obj.getPbId().toString());
        if (byId == null) {
            obj.setName("");
        } else {
            obj.setName(byId.getName());
        }

        Study study = studyDao.queryById(obj.getStudyId());
        if (study == null) {
            obj.setStudyTheme("");
        } else {
            obj.setStudyTheme(study.getTheme());
        }
        Date endTime = obj.getEndTime();
        String ss = DateUtil.date2String(endTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
        ss = ss.substring(11, ss.length());
        obj.setEnd(ss);
        //返显参会人员
        if (StringUtil.isNotEmpty(obj.getAttendPerson())) {
            List<Long> listIds = Arrays.asList(obj.getAttendPerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<PartyMemberOrgName> detailByMemberIds = partyMemberDao.findDetailByMemberIds(listIds);
            obj.setPartyMemberOrgNameList(detailByMemberIds);
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(obj);
        return baseVo;
    }

    /**
     * 反显或单查记录信息
     *
     * @param id
     * @return
     */
    @Override
    public BaseVo appReflect(Long id) {
        Conference obj = conferenceDao.queryById(id);
        Organization byId = organizationDao.findById(obj.getPbId().toString());
        obj.setName(byId.getName());
        if (obj.getStudyId() != null) {
            Study study = studyDao.queryById(obj.getStudyId());
            if (study != null) {
                obj.setStudyTheme(study.getTheme());
                //设置学习计划附件
                StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
                studyRelFileInfo.setStudyId(obj.getStudyId());
                studyRelFileInfo.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
                List<StudyRelFileInfo> list = studyRelFileInfoDao.findByParam(studyRelFileInfo);
                List<FileInfo> fileInfos = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(list)) {
                    for (StudyRelFileInfo relFileInfo : list) {
                        BaseVo b1 = fileServiceFeign.findById(relFileInfo.getFileId());
                        if (b1.getData() != null) {
                            String s = JSON.toJSONString(b1.getData());
                            FileInfo f1 = JSON.parseObject(s, FileInfo.class);
                            f1.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
                            fileInfos.add(f1);
                        }
                    }
                }

                //设置学习计划的附件
                if (CollectionUtils.isNotEmpty(fileInfos)) {
                    obj.setFileInfos(fileInfos);
                }
            }
        }

        BaseVo baseVo = new BaseVo();
        ConferenceAndImages conferenceAndImages = new ConferenceAndImages();
        if (obj != null) {
            //计算签到率
            Integer count = conferenceDao.calculateSignRate(id);
            String person = obj.getAttendPerson();
            String[] strs = person.split(",");
            obj.setSignRate(rateUtil.signRate(count, strs.length));
            if (obj.getEndTime() != null) {
                Date endTime = obj.getEndTime();
                String ss = DateUtil.date2String(endTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
                ss = ss.substring(11, ss.length());
                obj.setEnd(ss);
            }

            Integer at = obj.getAheadTime();
            double atime = at.doubleValue() / 1000 / 60 / 60;
            obj.setAtime(atime);
            if (!obj.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode()) && !obj.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                obj.setStatus(statusUtil.setConferenceStatus(obj.getStartTime(), obj.getEndTime()));
            }

            conferenceAndImages.setConference(obj);
            //得到会议附件
            StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
            studyRelFileInfo.setStudyId(id);
            List<StudyRelFileInfo> list = studyRelFileInfoDao.findByParam(studyRelFileInfo);
            List<FileInfo> fileInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)) {
                for (StudyRelFileInfo relFileInfo : list) {
                    BaseVo b1 = fileServiceFeign.findById(relFileInfo.getFileId());
                    if (b1.getData() != null) {
                        String s = JSON.toJSONString(b1.getData());
                        FileInfo f1 = JSON.parseObject(s, FileInfo.class);
                        fileInfos.add(f1);
                    }

                }
            }
            if (CollectionUtils.isNotEmpty(fileInfos)) {
                for (FileInfo fileInfo : fileInfos) {
                    if (fileInfo != null) {
                        fileInfo.setPath(fileInfo.getPath());
                    }

                }
                conferenceAndImages.setFileInfos(fileInfos);
            }
        } else {
            throwBusinessException("没有这条记录");
        }


        //拿出当前用户的签到信息
        //从token中拿到登陆用户信息
        UserInfo userInfo = getCurrentUserInfo();

        ConferenceRecord conferenceRecord = new ConferenceRecord();
        PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(userInfo.getIdcard());
        if (byIdCardAndStatus != null) {
            conferenceRecord.setUserId(byIdCardAndStatus.getId());
        }
        conferenceRecord.setConferenceId(id);
        List<ConferenceRecord> conferenceRecords = conferenceRecordDao.queryByParam(conferenceRecord);
        if (conferenceRecords != null && conferenceRecords.size() == Const.RESULT_SIZE_ONLY_ONE) {
            conferenceAndImages.setSignSituation(conferenceRecords.get(0).getSignSituation());
        }


        //查出来没签到的
        ConferenceMemberDetail obj1 = new ConferenceMemberDetail();
        obj1.setSignSituation(StatusEnum.NO.getCode());
        obj1.setConferenceId(id);
        List<ConferenceMemberDetail> conferenceMemberDetail1 = conferenceDao.queryRecordsByConferenceId(obj1);
        conferenceAndImages.setNotSignMembers(conferenceMemberDetail1);
        //查出来签到的
        obj1.setSignSituation(StatusEnum.YES.getCode());
        List<ConferenceMemberDetail> conferenceMemberDetail2 = conferenceDao.queryRecordsByConferenceId(obj1);
        conferenceAndImages.setSignMembers(conferenceMemberDetail2);

        baseVo.setData(conferenceAndImages);
        return baseVo;
    }

    /**
     * 删除信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Conference obj = conferenceDao.queryById(id);
        if (obj != null) {
            //先查出来关联纪录
            StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
            studyRelFileInfo.setStudyId(id);
            studyRelFileInfo.setType(FileTypeEnum.CONFERENCE_FILE.getCode());
            List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(studyRelFileInfo);
            if (byParam.size() != 0 && byParam != null) {
                //如果结果集数量==1
                if (byParam.size() == Const.RESULT_SIZE_ONLY_ONE) {
                    studyRelFileInfoDao.delete(byParam.get(0).getId());
                } else {
                    //如果结果集为复数 就直接批量删除
                    List<Long> ids = new ArrayList<>();
                    for (StudyRelFileInfo sf : byParam) {
                        ids.add(sf.getId());
                    }
                    studyRelFileInfoDao.batchDel(ids);
                }
            }
            conferenceDao.delete(id);
        } else {
            throwBusinessException("没有这条记录");
        }
    }

    /**
     * 上传资料
     *
     * @param studyRelFileInfos
     * @return
     */
    @Override
    @Transactional
    public BaseVo uploadData(StudyRelFileInfo studyRelFileInfos) {
        //拿id查询会议
        Long conferenceId = studyRelFileInfos.getStudyId();
        Conference obj = conferenceDao.queryById(conferenceId);
        if (obj == null) {
            throwBusinessException("会议记录不存在");
        }
        int status = statusUtil.setConferenceStatus(obj.getStartTime(), obj.getEndTime());
        if (status != ConferenceStatusEnum.STATUS_END.getCode() && status != ConferenceStatusEnum.STATUS_RUNNING.getCode()) {
            throwBusinessException("会议状态错误");
        }
        StudyRelFileInfo temp = new StudyRelFileInfo();
        temp.setType(FileTypeEnum.CONFERENCE_FILE.getCode());
        temp.setFileId(studyRelFileInfos.getFileId());
        temp.setStudyId(studyRelFileInfos.getStudyId());
        studyRelFileInfoDao.add(temp);
        return successVo();
    }

    /**
     * 删除附件
     *
     * @param studyRelFileInfos
     * @return
     */
    @Override
    public BaseVo delData(StudyRelFileInfo studyRelFileInfos) {
        Long conferenceId = studyRelFileInfos.getStudyId();
        Long fileId = studyRelFileInfos.getFileId();
        if (conferenceId == null || fileId == null) {
            throwBusinessException("会议id和文件id不能为空");
        }
        StudyRelFileInfo temp = new StudyRelFileInfo();
        temp.setType(FileTypeEnum.CONFERENCE_FILE.getCode());
        temp.setFileId(fileId);
        temp.setStudyId(conferenceId);
        List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(temp);
        if (byParam != null && byParam.size() == 1) {
            Long mainId = byParam.get(0).getId();
            studyRelFileInfoDao.delete(mainId);
        }
        return successVo();
    }

    /**
     * 查看会议资料
     *
     * @param conferenceId
     * @return
     */
    @Override
    public BaseVo queryData(Long conferenceId) {
        BaseVo baseVo = new BaseVo();
        Conference obj = conferenceDao.queryById(conferenceId);
        if (obj == null) {
            throwBusinessException("会议记录不存在");
        }
        StudyRelFileInfo temp = new StudyRelFileInfo();
        temp.setType(FileTypeEnum.CONFERENCE_FILE.getCode());
        temp.setStudyId(conferenceId);
        List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(temp);
        if (CollectionUtils.isNotEmpty(byParam)) {
            List<Long> fileIds = byParam.stream().map(p -> p.getFileId()).collect(Collectors.toList());
            if (fileIds != null && fileIds.size() > 0) {
                FileInfoVO fileInfoVO = new FileInfoVO();
                fileInfoVO.setFileIds(fileIds);
                BaseVo b1 = fileServiceFeign.findByIds(fileInfoVO);
                if (b1.getData() != null) {
                    String ss = JSON.toJSONString(b1.getData());
                    List<FileInfo> fileInfos = JSON.parseArray(ss, FileInfo.class);
                    baseVo.setData(fileInfos);
                }
            }
        }
        return baseVo;
    }


    /**
     * 分页查询和搜索
     * 一级机构--->二级 + 三级
     * 二级机构--->自己 + 三级
     * 三级机构 ：如果是党支部 orgtype=3 --->自己
     * 如果不是党支部  --->自己+下级
     * 四级机构--->自己
     *
     * @param conferenceVO
     * @return
     */
    @Override
    public BaseVo listPage(ConferenceVO conferenceVO) {
        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        conferenceVO.setNowDate(new Date());

        List<Long> idList = new ArrayList<>();
        List<Conference> conferences = new ArrayList<>();
        BaseVo baseVo = new BaseVo();

        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);

        if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()) {
            if (conferenceVO.getPbId() == null) {
                List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
                if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)) {
                    for (Organization organization : orgByBiggerThanOrgId) {
                        idList.add(Long.valueOf(organization.getId()));
                    }
                }
            } else {
                idList.add(conferenceVO.getPbId());
            }
            conferenceVO.setIdList(idList);
            PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
            conferences = conferenceDao.listPageTown(conferenceVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()) {
            List<Long> noids = new ArrayList<>();
            if (conferenceVO.getPbId() == null) {
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), false);
                if (CollectionUtils.isNotEmpty(subordinatesById)) {
                    for (Organization organization : subordinatesById) {
                        noids.add(Long.valueOf(organization.getId()));
                    }
                }
                noids.add(pbOrgId);
                idList.add(pbOrgId);
            } else {
                //判断选择的党组织是不是自己 如果是就要加上草稿 如果不是不加草稿
                if (conferenceVO.getPbId().equals(pbOrgId)) {
                    conferenceVO.setIsMeFlag(BooleanEnum.YES.getCode());
                } else {
                    conferenceVO.setIsMeFlag(BooleanEnum.NO.getCode());
                }
                idList.add(conferenceVO.getPbId());
            }
            conferenceVO.setIdList(idList);
            conferenceVO.setNoidList(noids);
            PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
            conferences = conferenceDao.listPageForTwo(conferenceVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()) {
            List<Long> noids = new ArrayList<>();
            Organization byId = organizationDao.findById(pbOrgId.toString());
            if (byId == null) {
                throwBusinessException("该组织不存在");
            }
            //如果是党支部 orgtype=3  自己
            if (byId.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())) {
                idList.add(pbOrgId);
                conferenceVO.setIdList(idList);
                PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                conferences = conferenceDao.listPageOrgTypeThree(conferenceVO);
            } else {
                //如果不是党支部 orgtype=3  自己+下级
                idList.add(pbOrgId);
                List<OrganizationInfoVO> directSubordinatesInfoById = organizationDao.findDirectSubordinatesInfoById(pbOrgId.toString());
                if (CollectionUtils.isNotEmpty(directSubordinatesInfoById)) {
                    for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById) {
                        noids.add(Long.valueOf(organizationInfoVO.getId()));
                    }
                }
                noids.add(pbOrgId);
                conferenceVO.setIdList(idList);
                conferenceVO.setNoidList(noids);
                PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                conferences = conferenceDao.listPage(conferenceVO);
            }

        }
        if (level == PartyOrgLevelEnum.LEVEL_FOUR.getCode()) {
            List<Long> noids = new ArrayList<>();
            idList.add(pbOrgId);

            conferenceVO.setIdList(idList);
            conferenceVO.setNoidList(noids);
            PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
            conferences = conferenceDao.listPageLevelFour(conferenceVO);
        }
        //遍历结果集 设置状态
        for (Conference conference : conferences) {
            if (!conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode()) && !conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                conference.setStatus(statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime()));
            }
            Date endTime = conference.getEndTime();
            String ss = DateUtil.date2String(endTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            ss = ss.substring(11, ss.length());
            conference.setEnd(ss);
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                conference.setReleaseTime(null);
            }
        }
        PageInfo<Conference> pageInfo = new PageInfo<Conference>(conferences);
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * app分页查询我的会议列表
     *
     * @param conferenceVO
     * @return
     */
    @Override
    public BaseVo appMyListPage(ConferenceVO conferenceVO) {
        UserInfo userInfo = getCurrentUserInfo();
        String pborgId = userInfo.getPbOrgId();
        PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(userInfo.getIdcard());
        if (byIdCardAndStatus != null) {
            conferenceVO.setUserId(byIdCardAndStatus.getId());
        }
        conferenceVO.setNowDate(new Date());
        List<Long> idList = new ArrayList<>();
        //查询出我的上级机构
        Organization directSuperiorById = organizationDao.findDirectSuperiorById(pborgId.toString(), null);
        if (directSuperiorById != null) {
            String s1 = RadixUtil.toFullBinaryString(Long.valueOf(pborgId));
            int ll = RadixUtil.getlevel(s1);
            if (ll != 1) {
                idList.add(Long.valueOf(directSuperiorById.getId()));
            }
        }
        idList.add(Long.valueOf(pborgId));
        conferenceVO.setIdList(idList);
        //根据党组织查询会议记录
        PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
        List<Conference> conferenceList = conferenceDao.appMyListPage(conferenceVO);
        if (CollectionUtils.isNotEmpty(conferenceList)) {
            for (Conference conference : conferenceList) {
                if (!conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode()) && !conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                    conference.setStatus(statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime()));
                }
            }
        }
        PageInfo<Conference> pageInfo = new PageInfo<Conference>(conferenceList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * app分页查询首页三会一课
     * 一级机构--->二级 + 三级
     * 二级机构--->自己 + 三级
     * 三级机构 ：如果是党支部 orgtype=3 --->自己
     * 如果不是党支部  --->自己+下级
     * 四级机构--->自己+上级
     *
     * @param conferenceVO
     * @return
     */
    @Override
    public BaseVo appThreeLessonListPage(ConferenceVO conferenceVO) {
        UserInfo userInfo = getCurrentUserInfo();
        //自己的部门
        Long pborgId = Long.valueOf(userInfo.getPbOrgId());
        //计算自己部门的等级
        String tt = RadixUtil.toFullBinaryString(pborgId);
        int level = RadixUtil.getlevel(tt);

        List<Conference> conferenceList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        Date now = new Date();
        conferenceVO.setNowDate(now);
        //查询当前身份
        BaseVo byId = sysServiceFeign.findById(userInfo.getCurrentIdentity());
        if (byId.getData() != null) {
            String s = JSON.toJSONString(byId.getData());
            Identity identity = JSON.parseObject(s, Identity.class);
            //如果是党员 能看自己+二级
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.MEMBER.getCode())) {
                idList.add(pborgId);
                Organization directSuperiorById = organizationDao.findDirectSuperiorById(pborgId.toString(), null);
                if (directSuperiorById != null) {
                    String s1 = RadixUtil.toFullBinaryString(pborgId);
                    int ll = RadixUtil.getlevel(s1);
                    if (ll != 1) {
                        idList.add(Long.valueOf(directSuperiorById.getId()));
                    }
                }
                conferenceVO.setIdList(idList);
                PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(userInfo.getIdcard());
                if (byIdCardAndStatus != null) {
                    conferenceVO.setUserId(byIdCardAndStatus.getId());
                }
                PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                conferenceList = conferenceDao.appThreeLessonListPage(conferenceVO);
            }
            //如果是管理员
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.ADMIN.getCode())) {
                if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()) {
                    //能看 2级 + 3级
                    if (conferenceVO.getPbId() == null) {
                        List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pborgId);
                        if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)) {
                            for (Organization organization : orgByBiggerThanOrgId) {
                                idList.add(Long.valueOf(organization.getId()));
                            }
                        }
                    } else {
                        idList.add(conferenceVO.getPbId());
                    }
                    conferenceVO.setIdList(idList);
                    PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                    conferenceList = conferenceDao.appThreeLessonAdminListPage(conferenceVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()) {
                    //能看 自己 + 3级
                    if (conferenceVO.getPbId() == null) {
                        List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), true);
                        if (CollectionUtils.isNotEmpty(subordinatesById)) {
                            for (Organization organization : subordinatesById) {
                                idList.add(Long.valueOf(organization.getId()));
                            }
                        }
                    } else {
                        idList.add(conferenceVO.getPbId());
                    }
                    conferenceVO.setIdList(idList);
                    PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                    conferenceList = conferenceDao.appThreeLessonAdminListPage(conferenceVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()) {
                    //如果orgType是2 or 4 查询自己+下级
                    //如果orgtype是3 查询自己+上级 + (过滤条件)
                    //20190814 与需求沟通先不查询上级

                    Organization byIdOrg = organizationDao.findById(pborgId.toString());
                    if (byIdOrg == null) {
                        throwBusinessException("该组织不存在");
                    }
                    //如果orgType是2 or 4 查询自己+下级
                    if (byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_TWO.getCode()) || byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_FOUR.getCode())) {
                        idList.add(pborgId);
                        List<OrganizationInfoVO> directSubordinatesInfoById = organizationDao.findDirectSubordinatesInfoById(pborgId.toString());
                        if (CollectionUtils.isNotEmpty(directSubordinatesInfoById)) {
                            for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById) {
                                idList.add(Long.valueOf(organizationInfoVO.getId()));
                            }
                        }
                        conferenceVO.setIdList(idList);
                    } else if (byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())) {
                        //如果是党支部 orgtype=3  自己+上级 + (过滤条件)
                        idList.add(pborgId);
//						Organization up = organizationDao.findDirectSuperiorById(pborgId.toString(),null);
//						if (up!=null){
//							idList.add(Long.valueOf(up.getId()));
//						}
                        conferenceVO.setIdList(idList);
                    } else {
                        throwBusinessException("机构错误");
                    }

                    PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                    conferenceList = conferenceDao.appThreeLessonAdminListPage(conferenceVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_FOUR.getCode()) {
                    //自己+上级
                    //20190814 与需求沟通先不查询上级
                    idList.add(pborgId);
//					Organization up = organizationDao.findDirectSuperiorById(pborgId.toString(),null);
//					if (up!=null){
//						idList.add(Long.valueOf(up.getId()));
//					}
                    conferenceVO.setIdList(idList);
                    PageHelper.startPage(conferenceVO.getPageNum(), conferenceVO.getPageSize());
                    conferenceList = conferenceDao.appThreeLessonAdminListPage(conferenceVO);
                }

            }

            for (Conference conference : conferenceList) {
                if (conference.getSignSituation() == null) {
                    conference.setSignSituation(StatusEnum.NO.getCode());
                }
                if (!conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode()) && !conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                    conference.setStatus(statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime()));
                }
            }
        }

        PageInfo<Conference> pageInfo = new PageInfo<Conference>(conferenceList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }


    /**
     * 到课详情
     *
     * @param conferenceMemberDetailVO
     * @return
     */
    @Override
    public BaseVo checkSignDetail(ConferenceMemberDetailVO conferenceMemberDetailVO) {
        Long id = conferenceMemberDetailVO.getId();
        Conference conference = conferenceDao.queryById(id);
        if (conference.getIsSign().equals(BooleanEnum.NO.getCode())) {
            throwBusinessException("此会议不需要签到");
        }


        PageHelper.startPage(conferenceMemberDetailVO.getPageNum(), conferenceMemberDetailVO.getPageSize());

        //查出来没签到的
        ConferenceMemberDetail obj1 = new ConferenceMemberDetail();
        obj1.setSignSituation(conferenceMemberDetailVO.getSignSituation());
        obj1.setConferenceId(conferenceMemberDetailVO.getId());
        List<ConferenceMemberDetail> conferenceMemberDetail1 = conferenceDao.queryRecordsByConferenceId(obj1);


        PageInfo<ConferenceMemberDetail> pageInfo = new PageInfo<>(conferenceMemberDetail1);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }


    /**
     * 取消会议
     *
     * @param conference
     * @return
     */
    @Override
    @Transactional
    public BaseVo cancel(Conference conference) {
        Conference obj = conferenceDao.queryById(conference.getId());
        if (obj == null) {
            throwBusinessException("该会议不存在");
        }
        if (!obj.getStatus().equals(ConferenceStatusEnum.STATUS_NOT_START.getCode())) {
            throwBusinessException("此会议不能取消");
        }
        //判断开会前2小时
        Date starttime = obj.getStartTime();
        Date now = new Date();
        double time = starttime.getTime() - now.getTime();
        double gap = time / 1000 / 60 / 60;
        if (gap <= new Double(2)) {
            throwBusinessException("距离会议开始不到2小时,会议不能取消");
        }
        if (obj.getIsSign().equals(BooleanEnum.YES.getCode())) {
            //如果要求签到 conferenceRecord里删除这次会议的参会记录
            conferenceRecordDao.delByConferenceId(conference.getId());
        }

        Conference upd = new Conference();
        upd.setId(conference.getId());
        upd.setStatus(ConferenceStatusEnum.STATUS_CANCELED.getCode());
        upd.setCancelReaseon(conference.getCancelReaseon());
        conferenceDao.update(upd);

        // 发送推送 无论是否选择通知 都要推送
        List<Long> memberIds = Arrays.asList(obj.getAttendPerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(memberIds))/*{
            // 发送mq消息
            MqMessage mqMessage = new MqMessage();
            mqMessage.setBusinessId(conference.getId());
            mqMessage.setTemplateId(Long.valueOf(MessageTemplateEnum.MEETING_CANCEL.getId()));
            //确定推送的人员
            List<String> idcards = partyMemberDao.batchSelectByIds(memberIds);
            if (CollectionUtils.isNotEmpty(idcards)){
                List<Long> longList = sysServiceFeign.batchSelectByCardId(idcards);
                if (longList!=null && longList.size()>0){
                    Long[] targetId = new Long[longList.size()];
                    longList.toArray(targetId);
                    mqMessage.setTargetId(targetId);
                    mqMessage.setTargetUserType(TargetTypeEnum.ALL.getCode()+"");
                    Organization byId = organizationDao.findById(obj.getPbId().toString());
                    if (byId!=null){
                        mqMessage.setCreater(byId.getName());
                    }
                    mqMessage.setTargetType(TargetTypeEnum.USER.getCode());
                    String[] params = {DateUtil.format(obj.getStartTime()),conference.getCancelReaseon()};
                    mqMessage.setParams(params);
                    mqMessageUtil.assembleMqMessage(rabbitTemplate,mqMessage);
                }
            }
        }*/ {
            // 发送mq消息
            //确定推送的人员
            List<String> idcards = partyMemberDao.batchSelectByIds(memberIds);
            if (CollectionUtils.isNotEmpty(idcards)) {
                List<Long> longList = sysServiceFeign.batchSelectByCardId(idcards);
                if (longList != null && longList.size() > 0) {
                    Long[] targetId = new Long[longList.size()];
                    longList.toArray(targetId);
                    Organization byId = organizationDao.findById(obj.getPbId().toString());
                    String creater = null;
                    if (byId != null) {
                        creater = byId.getName();

                    }
                    String[] params = {DateUtil.format(obj.getStartTime()), conference.getCancelReaseon()};
                    pushUserMessageByAlias(MessageTemplateEnum.MEETING_CANCEL,targetId, params, creater);
                 /*   MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.MEETING_CANCEL, targetId, params, creater, new Date(), null);
                    sendMqPushUtil.sendMqMessage(mqSendMessage);*/
                }
            }

        }

        return new BaseVo();
    }


    /**
     * 根据会议id生成二维码 弃用
     *
     * @param id
     * @return
     */
    @Override
    public void genQrCode(Long id, Long appId, HttpServletResponse response) throws IOException {
        Conference conference = conferenceDao.queryById(id);
        Map<String, Object> map = new HashMap<>();

        String str = "appId:" + appId + ",module:wt_app_pb,id:" + conference.getId();
        map.put("key", str);
        try {

            //生成二维码

            String jsonStr = JSONObject.toJSONString(map);
            QrCodeUtil.encode(jsonStr, "png", BarcodeFormat.QR_CODE, 300, 300, null, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据会议id生成二维码  返回字符串
     *
     * @param id
     * @return
     */
    @Override
    public BaseVo strgenQrCode(Long id, Long appId, HttpServletResponse response) throws IOException {
        Conference conference = conferenceDao.queryById(id);
        if (conference == null) {
            throwBusinessException("会议不存在");
        }

        JSONObject json = new JSONObject();
        json.put("appId", appId.toString());
        json.put("module", Const.SIGN_MODULE);
        json.put("conferenceId", conference.getId().toString());
        String str = json.toString();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(str);
        return baseVo;
    }

    @Override
    public BaseVo sign(SignInfo signInfo, HttpServletRequest request) {
        BaseVo baseVo = new BaseVo();
        if (signInfo.getAppId().intValue()!=ApplicationTypeEnum.APPLICATION_PB.getApplicationId().intValue()) {
            throwBusinessException("接入端错误");
        }
        if (!signInfo.getModule().equals( Const.SIGN_MODULE)) {
            throwBusinessException("模块错误");
        }
        UserInfo userInfo = getCurrentUserInfo();

        Long conferenceId = signInfo.getId();
        String idcard = userInfo.getIdcard();
        PartyMember member = partyMemberDao.findByCondition(null, idcard);
        Long userId = member.getId();
        Long pbId = Long.valueOf(userInfo.getPbOrgId());
        Conference conference = conferenceDao.queryById(conferenceId);
        if (conference == null) {
            throwBusinessException("会议不存在");
        }


        if (pbId != null) {

            //先判断是不是参会人员
            if (conference.getAttendPerson().contains(member.getId().toString())) {
                int st = statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime());
                //判断会议是否结束
                if (st == ConferenceStatusEnum.STATUS_END.getCode()) {
                    throwBusinessException("会议已结束");
                }

                //判断这个人有没有重复签到
                ConferenceRecord obj = new ConferenceRecord();
                PartyMember byCondition = partyMemberDao.findByCondition(null, userInfo.getIdcard());
                obj.setConferenceId(conferenceId);
                obj.setUserId(byCondition.getId());
                List<ConferenceRecord> conferenceRecords = conferenceRecordDao.queryByParam(obj);
                if (conferenceRecords != null && conferenceRecords.size() >= Const.RESULT_SIZE_ONLY_ONE) {
                    ConferenceRecord temp = conferenceRecords.get(0);
                    if (temp.getSignSituation().equals(BooleanEnum.YES.getCode())) {
                        throwBusinessException("已经签到");
                    }
                }


                //判断是不是迟到
                Date startTime = conference.getStartTime();
                Date endTime = getAheadTime(startTime, conference.getLastTime(), "add");
                Date signTime = getAheadTime(startTime, conference.getAheadTime(), "minus");
                Date current = new Date();

                ConferenceRecord conferenceRecord = new ConferenceRecord();
                conferenceRecord.setUserId(userId);
                conferenceRecord.setConferenceId(conferenceId);
                //根据用户id和会议id查询记录
                List<ConferenceRecord> result = conferenceRecordDao.queryByParam(conferenceRecord);
                if (result != null && result.size() == 1) {
                    Long id = result.get(0).getId();
                    ConferenceRecord upd = new ConferenceRecord();
                    upd.setId(id);
                    upd.setSignSituation(BooleanEnum.YES.getCode());
                    //如果超过结束时间 就不存记录
                    if (current.after(endTime)) {
                        throwBusinessException("会议结束 不能签到");
                    }
                    //如果签到时间没到 就不许签到
                    if (current.before(signTime)) {
                        throwBusinessException("签到时间未到 不能签到");
                    }
                    //在签到时间内
                    if (current.after(signTime) && current.before(startTime)) {
                        upd.setSignCondition(BooleanEnum.NO.getCode());
                    }
                    //会议进行中 迟到
                    if (current.after(startTime) && current.before(endTime)) {
                        upd.setSignCondition(BooleanEnum.YES.getCode());
                    }
                    //添加进数据库中
                    upd.setSignTime(current);
                    if (StringUtil.isEmpty(conferenceRecord.getLateReason())) {
                        upd.setLateReason("");
                    }
                    //更新记录表
                    conferenceRecordDao.update(upd);
                }

                SignInfo sn = new SignInfo();
                sn.setSignTime(current);
                sn.setTheme(conference.getTheme());
                sn.setPlace(conference.getPlace());
                sn.setStartTime(conference.getStartTime());
                Organization byId = organizationDao.findById(conference.getPbId().toString());
                sn.setPbName(byId.getName());
                baseVo.setData(sn);

            } else {
                throwBusinessException("不是参会人员");
            }
        } else {
            throwBusinessException("党组织id异常");
        }
        return baseVo;
    }

    @Override
    public void exportToExcel(HttpServletResponse response, ConferenceExcelParam conferenceExcelParam) {
        //获得数据
        List<Conference> conferences = getDataForConferenceExcel(conferenceExcelParam);
        if (conferences == null || conferences.size() <= 0) {
            throwBusinessException("无数据可供导出");
        }
        List<ConferenceExcel> conferenceExcelList = new ArrayList<>();
        int i = 1;
        for (Conference conference : conferences) {
            ConferenceExcel conferenceExcel = new ConferenceExcel();
            BeanUtils.copyProperties(conference, conferenceExcel);
            conferenceExcel.setSignRate(conference.getSignRate() + "%");
            conferenceExcel.setId(i);
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                conferenceExcel.setStatus("草稿");
            }
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_NOT_START.getCode())) {
                conferenceExcel.setStatus("未开始");
            }
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_RUNNING.getCode())) {
                conferenceExcel.setStatus("进行中");
            }
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_END.getCode())) {
                conferenceExcel.setStatus("已结束");
            }
            if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode())) {
                conferenceExcel.setStatus("已取消");
            }

            //设置会议类型
            if (conference.getConferenceType().equals(ConferenceTypeEnum.CONFERENCE_TYPE_DANGYUANDAHUI.getCode())) {
                conferenceExcel.setConferenceType(ConferenceTypeEnum.CONFERENCE_TYPE_DANGYUANDAHUI.getInfo());
            }
            if (conference.getConferenceType().equals(ConferenceTypeEnum.CONFERENCE_TYPE_DANGZHIBUWEIYUANHUI.getCode())) {
                conferenceExcel.setConferenceType(ConferenceTypeEnum.CONFERENCE_TYPE_DANGZHIBUWEIYUANHUI.getInfo());
            }
            if (conference.getConferenceType().equals(ConferenceTypeEnum.CONFERENCE_TYPE_DANGXIAOZUHUI.getCode())) {
                conferenceExcel.setConferenceType(ConferenceTypeEnum.CONFERENCE_TYPE_DANGXIAOZUHUI.getInfo());
            }
            if (conference.getConferenceType().equals(ConferenceTypeEnum.CONFERENCE_TYPE_DANGKE.getCode())) {
                conferenceExcel.setConferenceType(ConferenceTypeEnum.CONFERENCE_TYPE_DANGKE.getInfo());
            }
            //设置会议时间
            Date startTime = conference.getStartTime();
            String start = DateUtil.date2String(startTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            Date endTime = conference.getEndTime();
            String ss = DateUtil.date2String(endTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            ss = ss.substring(11, ss.length());
            conferenceExcel.setStartTime(start + "-" + ss);

            conferenceExcelList.add(conferenceExcel);
            i++;
        }
        String fileName = "会议管理" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "UTF-8") + ".xls";
            response.setHeader("content-type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //导出操作
            ExcelUtil.exportExcel(conferenceExcelList, null, "会议列表", ConferenceExcel.class, fileName, response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private List<Conference> getDataForConferenceExcel(ConferenceExcelParam conferenceExcelParam) {
        //结果集
        List<Conference> conferences = new ArrayList<>();

        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        conferenceExcelParam.setNowDate(new Date());

        List<Long> idList = new ArrayList<>();

        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);

        if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()) {
            if (conferenceExcelParam.getPbId() == null) {
                List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
                if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)) {
                    for (Organization organization : orgByBiggerThanOrgId) {
                        idList.add(Long.valueOf(organization.getId()));
                    }
                }
            } else {
                idList.add(conferenceExcelParam.getPbId());
            }
            conferenceExcelParam.setIdList(idList);
            conferences = conferenceDao.listPageForTownExcel(conferenceExcelParam);
        }
        if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()) {
            List<Long> noids = new ArrayList<>();
            if (conferenceExcelParam.getPbId() == null) {
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), false);
                if (CollectionUtils.isNotEmpty(subordinatesById)) {
                    for (Organization organization : subordinatesById) {
                        noids.add(Long.valueOf(organization.getId()));
                    }
                    noids.add(pbOrgId);
                    idList.add(pbOrgId);
                }
            } else {
                //判断选择的党组织是不是自己 如果是就要加上草稿 如果不是不加草稿
                if (conferenceExcelParam.getPbId().equals(pbOrgId)) {
                    conferenceExcelParam.setIsMeFlag(BooleanEnum.YES.getCode());
                } else {
                    conferenceExcelParam.setIsMeFlag(BooleanEnum.NO.getCode());
                }
                idList.add(conferenceExcelParam.getPbId());
            }
            conferenceExcelParam.setIdList(idList);
            conferenceExcelParam.setNoidList(noids);
            conferences = conferenceDao.listPageForTwoExcel(conferenceExcelParam);
        }
        if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()) {
            idList.add(pbOrgId);
            conferenceExcelParam.setIdList(idList);
            conferences = conferenceDao.listPageForExcel(conferenceExcelParam);
        }
        //遍历结果集 设置状态
        for (Conference conference : conferences) {
            if (!conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode()) && !conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode())) {
                conference.setStatus(statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime()));
            }
            Date endTime = conference.getEndTime();
            String ss = DateUtil.date2String(endTime, DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            ss = ss.substring(11, ss.length());
            conference.setEnd(ss);
        }

        return conferences;
    }


    /**
     * service-file删除文件
     */
    @Override
    @Transactional
    public void pbDelFile(Long fileId) {
        StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
        studyRelFileInfo.setFileId(fileId);
        List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(studyRelFileInfo);
        if (byParam != null && byParam.size() == Const.RESULT_SIZE_ONLY_ONE) {
            Long id = byParam.get(0).getId();
            studyRelFileInfoDao.delete(id);
        }

    }

    /**
     * 每日定时更新会议状态
     *
     * @return
     */
    @Override
    @Transactional
    public BaseVo dailyUpdateConferenceStatus() {
        List<Conference> updateList = new ArrayList<>();
        //全部会议
        List<Conference> conferences = conferenceDao.queryAllConferences();
        if (CollectionUtils.isNotEmpty(conferences)) {
            for (Conference conference : conferences) {
                if (!conference.getStatus().equals(ConferenceStatusEnum.STATUS_DRAFT.getCode()) && !conference.getStatus().equals(ConferenceStatusEnum.STATUS_CANCELED.getCode())) {
                    Conference obj = new Conference();
                    Integer status = statusUtil.setConferenceStatus(conference.getStartTime(), conference.getEndTime());
                    if (!(conference.getStatus().equals(ConferenceStatusEnum.STATUS_END.getCode()) && status == 3)) {
                        obj.setStatus(status);
                        obj.setId(conference.getId());
                        obj.setVersion(conference.getVersion());
                        updateList.add(obj);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(updateList)) {
                if (updateList.size() < 1000) {
                    conferenceDao.batchUpdateConferenceStatus(updateList);
                } else {
                    int len = 1000;
                    int size = updateList.size();
                    int count = size % 1000 == 0 ? size / 1000 : size / 1000 + 1;
                    for (int i = 0; i < count; i++) {
                        //截取1000条数据
                        List<Conference> sublist = updateList.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
                        conferenceDao.batchUpdateConferenceStatus(sublist);
                    }
                }
            }
        }
        return new BaseVo();
    }

    /**
     * 每日定时更新会议签到率
     *
     * @return
     */
    @Override
    @Transactional
    public BaseVo dailyUpdateConferenceSignRate() {
        List<Conference> updateList = new ArrayList<>();
        //全部需要签到的会议
        Conference temp = new Conference();
        temp.setIsSign(StatusEnum.YES.getCode());
        List<Conference> conferences = conferenceDao.findByParam(temp);
        if (CollectionUtils.isNotEmpty(conferences)) {
            for (Conference conference : conferences) {
                if (conference.getIsSign().equals(BooleanEnum.YES.getCode())) {
                    if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_END.getCode())) {
                        Long id = conference.getId();
                        Conference obj = new Conference();
                        Integer count = conferenceDao.calculateSignRate(id);
                        String persons = conference.getAttendPerson();
                        Integer all = persons.split(",").length;
                        Integer rate = rateUtil.signRate(count, all);

                        obj.setId(id);
                        obj.setSignRate(rate);
                        obj.setVersion(conference.getVersion());

                        updateList.add(obj);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(updateList)) {
                if (updateList.size() < 1000) {
                    conferenceDao.batchUpdateConferenceSignRate(updateList);
                } else {
                    int len = 1000;
                    int size = updateList.size();
                    int count = size % 1000 == 0 ? size / 1000 : size / 1000 + 1;
                    for (int i = 0; i < count; i++) {
                        //截取1000条数据
                        List<Conference> sublist = updateList.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
                        conferenceDao.batchUpdateConferenceSignRate(sublist);
                    }
                }
            }

        }
        return new BaseVo();
    }

    /**
     * 每日定时更新会议上传率
     *
     * @return
     */
    @Override
    @Transactional
    public BaseVo dailyUpdateConferenceUploadRate() {
        List<Conference> updateList = new ArrayList<>();
        //全部需要签到的会议
        Conference temp = new Conference();
        temp.setIsUploadExperience(BooleanEnum.YES.getCode());
        List<Conference> conferences = conferenceDao.findByParam(temp);
        if (CollectionUtils.isNotEmpty(conferences)) {
            for (Conference conference : conferences) {
                if (conference.getIsUploadExperience().equals(BooleanEnum.YES.getCode())) {
                    if (conference.getStatus().equals(ConferenceStatusEnum.STATUS_END.getCode())) {
                        Long id = conference.getId();
                        Conference obj = new Conference();
                        Integer count = conferenceExperienceDao.calculateUploadRate(id);
                        String persons = conference.getAttendPerson();
                        Integer all = persons.split(",").length;
                        Integer rate = rateUtil.uploadRate(count, all);

                        obj.setUploadRate(rate);
                        obj.setId(id);
                        obj.setVersion(conference.getVersion());

                        updateList.add(obj);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(updateList)) {
                if (updateList.size() < 1000) {
                    conferenceDao.batchUpdateConferenceUploadRate(updateList);
                } else {
                    int len = 1000;
                    int size = updateList.size();
                    int count = size % 1000 == 0 ? size / 1000 : size / 1000 + 1;
                    for (int i = 0; i < count; i++) {
                        //截取1000条数据
                        List<Conference> sublist = updateList.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
                        conferenceDao.batchUpdateConferenceUploadRate(sublist);
                    }
                }
            }
        }
        return new BaseVo();
    }


    private Date getAheadTime(Date date, Integer time, String type) {
        Date obj = null;
        if (type.equals("minus")) {
            obj = new Date(date.getTime() - time);
        }
        if (type.equals("add")) {
            obj = new Date(date.getTime() + time);
        }
        return obj;
    }

}
