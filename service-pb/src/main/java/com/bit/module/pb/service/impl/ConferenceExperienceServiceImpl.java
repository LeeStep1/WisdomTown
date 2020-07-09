package com.bit.module.pb.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.enumerate.BooleanEnum;
import com.bit.common.enumerate.PartyOrgLevelEnum;
import com.bit.common.enumerate.StatusEnum;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.ConferenceDao;
import com.bit.module.pb.dao.ConferenceExperienceDao;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.feign.FileServiceFeign;
import com.bit.module.pb.service.ConferenceExperienceService;
import com.bit.module.pb.vo.ConferenceExperienceVO;
import com.bit.module.pb.vo.ConferenceVO;
import com.bit.utils.DateUtil;
import com.bit.utils.RadixUtil;
import com.bit.utils.RateUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenduo
 * @create 2019-01-17 19:13
 */
@Service("conferenceExperienceService")
public class ConferenceExperienceServiceImpl extends BaseService implements ConferenceExperienceService {


    @Autowired
    private ConferenceExperienceDao conferenceExperienceDao;
    @Autowired
    private PartyMemberDao partyMemberDao;
    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private RateUtil rateUtil;
    @Autowired
    private OrganizationDao organizationDao;
    /**
     * 会议心得新增
     * @param conferenceExperience
     * @return
     */
    @Override
    @Transactional
    public void add(ConferenceExperience conferenceExperience) {
        Long id = conferenceExperience.getId();
        conferenceExperience.setId(id);
        conferenceExperience.setStatus(StatusEnum.YES.getCode());
        conferenceExperience.setUploadTime(new Date());
        conferenceExperienceDao.update(conferenceExperience);

        //上传完计算上传率
        Integer count = conferenceExperienceDao.calculateUploadRate(conferenceExperience.getConferenceId());
        Conference con = conferenceDao.queryById(conferenceExperience.getConferenceId());

        String person = con.getAttendPerson();
        String[] ss = person.split(",");
        //计算全部人员
        int rate = rateUtil.signRate(count,ss.length);

        //更新会议表的上传率
        Conference conference = new Conference();
        conference.setId(con.getId());
        conference.setUploadRate(rate);
        conference.setVersion(con.getVersion());
        conferenceDao.update(conference);

    }
    /**
     * 会议心得更新
     * @param conferenceExperience
     * @return
     */
    @Override
    @Transactional
    public void update(ConferenceExperience conferenceExperience) {
        conferenceExperience.setUploadTime(new Date());
        conferenceExperienceDao.update(conferenceExperience);
    }
    /**
     * 会议心得分页查询
     * @param conferenceVO
     * @return
     */
    @Override
    public BaseVo listpage(ConferenceVO conferenceVO) {
        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        conferenceVO.setNowDate(new Date());

        List<Long> idList = new ArrayList<>();
        List<Conference> conferences = new ArrayList<>();
        BaseVo baseVo = new BaseVo();

        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);

        if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
            if (conferenceVO.getPbId()==null){
                List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
                if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)){
                    for (Organization organization : orgByBiggerThanOrgId) {
                        idList.add(Long.valueOf(organization.getId()));
                    }
                }
            }else {
                idList.add(conferenceVO.getPbId());
            }
            conferenceVO.setIdList(idList);
            PageHelper.startPage(conferenceVO.getPageNum(),conferenceVO.getPageSize());
            conferences = conferenceExperienceDao.listPage(conferenceVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
            if (conferenceVO.getPbId()==null){
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), false);
                if (CollectionUtils.isNotEmpty(subordinatesById)){
                    for (Organization organization : subordinatesById) {
                        idList.add(Long.valueOf(organization.getId()));
                    }
                }
                idList.add(pbOrgId);
            }else {
                idList.add(conferenceVO.getPbId());
            }
            conferenceVO.setIdList(idList);
            PageHelper.startPage(conferenceVO.getPageNum(),conferenceVO.getPageSize());
            conferences = conferenceExperienceDao.listPage(conferenceVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()){
            idList.add(pbOrgId);
            conferenceVO.setIdList(idList);
            PageHelper.startPage(conferenceVO.getPageNum(),conferenceVO.getPageSize());
            conferences = conferenceExperienceDao.listPage(conferenceVO);
        }
        //遍历结果集 设置状态
        for (Conference conference : conferences) {

            Date endTime = conference.getEndTime();
            String ss = DateUtil.date2String(endTime,DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            ss = ss.substring(11,ss.length());
            conference.setEnd(ss);
        }
        PageInfo<Conference> pageInfo = new PageInfo<Conference>(conferences);
        baseVo.setData(pageInfo);
        return baseVo;
    }


    /**
     * 查询会议的所有心得
     * @param conferenceExperienceVO
     * @return
     */
    @Override
    public BaseVo queryExperience(ConferenceExperienceVO conferenceExperienceVO) {
        ConferenceExperienceFileAll conferenceExperienceFileAll = new ConferenceExperienceFileAll();

        //根据会议id查询党组织id
        Conference obj = conferenceDao.queryById(conferenceExperienceVO.getConferenceId());
        if (obj.getIsUploadExperience().equals(BooleanEnum.NO.getCode())){
            throwBusinessException("此会议不需要上传心得");
        }

        //去查询心得记录
        PageHelper.startPage(conferenceExperienceVO.getPageNum(),conferenceExperienceVO.getPageSize());

        ConferenceExperienceVO temp = new ConferenceExperienceVO();
        temp.setConferenceId(conferenceExperienceVO.getConferenceId());
        List<ConferenceExperienceFile> conferenceExperienceFiles = conferenceExperienceDao.queryExperience(temp);

        //上传了文件的党员id集合
        List<Long> fileIds = new ArrayList<>();
        //处理filedetail
        if (CollectionUtils.isNotEmpty(conferenceExperienceFiles)){
            for (ConferenceExperienceFile conferenceExperienceFile : conferenceExperienceFiles) {
                //如果文件id部位空
                if (conferenceExperienceFile.getFileId()!=null){
                    String filedetail = conferenceExperienceFile.getFileDetail();
                    if (StringUtil.isNotEmpty(filedetail)){
                        try {
                            //单个附件
                            FileInfo fileInfo = JSON.parseObject(filedetail,FileInfo.class);
                            if (fileInfo!=null){
                                conferenceExperienceFile.setFileName(fileInfo.getFileName());
                                conferenceExperienceFile.setStatus(StatusEnum.YES.getCode());

                                fileIds.add(conferenceExperienceFile.getFileId());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    conferenceExperienceFile.setStatus(StatusEnum.NO.getCode());
                }
                conferenceExperienceFile.setFileDetail("");
            }
        }


        PageInfo<ConferenceExperienceFile> pageInfo = new PageInfo<>(conferenceExperienceFiles);
        conferenceExperienceFileAll.setPageInfo(pageInfo);
        conferenceExperienceFileAll.setFileIds(fileIds);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(conferenceExperienceFileAll);
        return baseVo;
    }
    /**
     * 批量校验文件名称
     * @param experienceCheckName
     * @return
     */
    @Override
    public BaseVo batchCheckExperienceName(ExperienceCheckName experienceCheckName) {
        BaseVo baseVo = new BaseVo();
        Long conferenceId = experienceCheckName.getConferenceId();
        if (conferenceId == null){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_ID_NULL.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_ID_NULL.getCode());
            return baseVo;
        }
        Conference obj = conferenceDao.queryById(conferenceId);
        if (obj==null){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_NOT_EXIST.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_NOT_EXIST.getCode());
            return baseVo;
        }
        String fileName = experienceCheckName.getFileName();
        if (StringUtil.isEmpty(fileName)){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_FILE_NAME_NULL.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_FILE_NAME_NULL.getCode());
            return baseVo;
        }
        //校验会议参会人员是否为空
        if (StringUtil.isEmpty(obj.getAttendPerson())){
            baseVo.setMsg(ResultCode.ATTEND_PERSON_NULL.getInfo());
            baseVo.setCode(ResultCode.ATTEND_PERSON_NULL.getCode());
            return baseVo;
        }

        List<Long> memberIds = Arrays.asList(obj.getAttendPerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //党组织下的党员集合
        List<PartyMember> membersByOrgId = partyMemberDao.queryUserbatchSelectByIds(memberIds);

        //党组织下的党员的姓名集合
        List<String> memberNames = membersByOrgId.stream().map(p -> p.getName()).collect(Collectors.toList());
        if (memberNames==null || memberNames.size()==0){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_NO_PARTY_MEMBERS.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_NO_PARTY_MEMBERS.getCode());
            return baseVo;
        }

        String reason = "";
        int index = fileName.indexOf("_");
        if (index == -1){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_FILE_FORMAT_ERROR.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_FILE_FORMAT_ERROR.getCode());
            return baseVo;
        }
        String checkName = fileName.substring(0,index);
        //文件后缀名
        int idx = fileName.indexOf(".");
        if (index == -1){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_FILE_SUFFIX_NULL.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_FILE_SUFFIX_NULL.getCode());
            return baseVo;
        }
        String suffix = fileName.substring(idx+1,fileName.length());
        if (!memberNames.contains(checkName)){
            //如果名字不匹配
            reason = fileName + " 文件错误,原因:文件名不匹配";
            baseVo.setCode(ResultCode.EXPERIENCE_NOT_MATCH.getCode());
            baseVo.setMsg(reason);
        }else if (!(suffix.equals("doc")||suffix.equals("docx"))){
            //如果格式不匹配
            reason = fileName + " 文件错误,原因:文件格式不匹配";
            baseVo.setCode(ResultCode.EXPERIENCE_NOT_MATCH.getCode());
            baseVo.setMsg(reason);
        }
        return baseVo;
    }

    /**
     * 批量上传会议心得
     * @param experienceFile
     * @return
     */
    @Override
    @Transactional
    public BaseVo uploadExperience(ExperienceFile experienceFile) throws IOException {
        BaseVo baseVo = new BaseVo();
        Long conferenceId = experienceFile.getConferenceId();
        FileInfo fileInfo = experienceFile.getFileInfo();
        if (fileInfo==null){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_FILE_INFOSUFFIX_NOT_EXIST.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_FILE_INFOSUFFIX_NOT_EXIST.getCode());
            return baseVo;
        }
        if (conferenceId == null){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_ID_NULL.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_ID_NULL.getCode());
            return baseVo;
        }
        Conference obj = conferenceDao.queryById(conferenceId);
        if (obj==null){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_NOT_EXIST.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_NOT_EXIST.getCode());
            return baseVo;
        }

        List<Long> memberIds = Arrays.asList(obj.getAttendPerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        //党组织下的党员集合
        List<PartyMember> membersByOrgId = partyMemberDao.queryUserbatchSelectByIds(memberIds);

        //党组织下的党员的姓名集合
        List<String> memberNames = membersByOrgId.stream().map(p -> p.getName()).collect(Collectors.toList());
        if (memberNames==null || memberNames.size()<=0){
            baseVo.setMsg(ResultCode.EXPERIENCE_CONFERENCE_NO_PARTY_MEMBERS.getInfo());
            baseVo.setCode(ResultCode.EXPERIENCE_CONFERENCE_NO_PARTY_MEMBERS.getCode());
            return baseVo;
        }

        ConferenceExperience cs = new ConferenceExperience();
        cs.setConferenceId(conferenceId);
        try {
            String fileName = fileInfo.getFileName();
            int index = fileName.indexOf("_");
            String checkName = fileName.substring(0,index);
            //结果集转换成map
            Map<String, PartyMember> partyMemberMap = membersByOrgId.stream().collect(Collectors.toMap(PartyMember::getName, Function.identity()));
            cs.setUserId(partyMemberMap.get(checkName).getId());
            ConferenceExperience byConferenceIdAndUserId = conferenceExperienceDao.findByConferenceIdAndUserId(cs);
            if (byConferenceIdAndUserId!=null){
                Long mainId = byConferenceIdAndUserId.getId();
                ConferenceExperience temp = new ConferenceExperience();
                temp.setId(mainId);
                temp.setUploadTime(new Date());
                temp.setFileId(fileInfo.getId());
                temp.setStatus(BooleanEnum.YES.getCode());
                String detail = JSON.toJSONString(fileInfo);
                temp.setFileDetail(detail);
                conferenceExperienceDao.update(temp);
            }
        }catch (IllegalStateException e){
            baseVo.setCode(ResultCode.EXPERIENCE_PB_MEMBER_DUPLICATE.getCode());
            baseVo.setMsg("文件名:"+fileInfo.getFileName()+"上传失败,原因:党员名称重复");
            //删除文件信息
            Long fileId = fileInfo.getId();
            //物理删除附件信息
            fileServiceFeign.fileDel(fileId);
        }catch (Exception e){
            //删除文件信息
            Long fileId = fileInfo.getId();
            //物理删除附件信息
            fileServiceFeign.fileDel(fileId);
            baseVo.setCode(ResultCode.EXPERIENCE_UPLOAD_FAIL.getCode());
            baseVo.setMsg(ResultCode.EXPERIENCE_UPLOAD_FAIL.getInfo());
        }

        return baseVo;
    }

}
