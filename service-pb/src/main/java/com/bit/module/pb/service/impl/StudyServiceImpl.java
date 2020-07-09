package com.bit.module.pb.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.*;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.feign.FileServiceFeign;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.StudyService;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.StudyMemberDetailVO;
import com.bit.module.pb.vo.StudyVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelUtil;
import com.bit.utils.RadixUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StudyService的实现类
 * @author chenduo
 * @date 2019-1-11
 */
@Service("studyService")
public class StudyServiceImpl extends BaseService implements StudyService {

    private static final Logger logger = LoggerFactory.getLogger(StudyServiceImpl.class);

    @Autowired
    private StudyDao studyDao;
    @Autowired
    private StudyRecordDao studyRecordDao;
    @Autowired
    private StudyRelFileInfoDao studyRelFileInfoDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private PartyMemberDao partyMemberDao;
    @Autowired
    private SysServiceFeign sysServiceFeign;
    @Autowired
    private SendMqPushUtil sendMqPushUtil;


    /**
     * 新增学习计划
     * @param study
     */
    @Override
    @Transactional
    public void add(Study study) {
        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        //从登陆信息中获取用户id
        Long userId = userInfo.getId();
        //从登陆信息中获取用户id
        study.setCreateUserId(userId);
        study.setCreateUserName(userInfo.getUsername());
        study.setCreateRealName(userInfo.getRealName());
        //创建时间
        study.setCreateTime(new Date());
		study.setReleaseTime(new Date());
        //默认状态是草稿
        study.setIsRelease(StudyReleaseStatusEnum.NOT_RELEASED.getCode());
        study.setPborgId(Long.valueOf(userInfo.getPbOrgId()));
        study.setVersion(Const.INIT_VERSION);

        List<Long> orgids = new ArrayList<>();
        List<Long> memberIds = new ArrayList<>();
        //如果全选了
        if(study.getAllOrgId()!=null){
            String s = RadixUtil.toFullBinaryString(Long.valueOf(pbOrgId));
            int level = RadixUtil.getlevel(s);
            if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
                List<PartyMember> membersByBiggerThanOrgId = partyMemberDao.findMembersByBiggerThanOrgId(Long.valueOf(pbOrgId));
                if (CollectionUtils.isNotEmpty(membersByBiggerThanOrgId)){
                    for (PartyMember partyMember : membersByBiggerThanOrgId) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(pbOrgId, true);
                if (CollectionUtils.isNotEmpty(subordinatesById)){
                    for (Organization organization : subordinatesById) {
                        orgids.add(Long.valueOf(organization.getId()));
                    }
                    List<PartyMember> partyMembers = partyMemberDao.batchFindMemberByOrgIds(orgids);
                    for (PartyMember partyMember : partyMembers) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()){
                List<Long> memberIdsByOrgId = partyMemberDao.findMemberIdsByOrgId(Long.valueOf(pbOrgId));
                memberIds.addAll(memberIdsByOrgId);
            }
            if (CollectionUtils.isNotEmpty(memberIds)){
                StringBuffer attendPersons = new StringBuffer(memberIds.size());
                for (Long memberId : memberIds) {
                    attendPersons = attendPersons.append(memberId);
                    attendPersons = attendPersons.append(",");
                }
                String persons = attendPersons.toString();
                persons= persons.substring(0,persons.length()-1);
                study.setAbsolutePerson(persons);
            }

        }
        studyDao.add(study);

        //处理附件
        List<FileInfo> fileInfos = study.getFileInfos();
        List<StudyRelFileInfo> studyRelFileInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fileInfos)){
            for (FileInfo fileInfo : fileInfos) {
                StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
                studyRelFileInfo.setStudyId(study.getId());
                studyRelFileInfo.setFileId(fileInfo.getId());
//                studyRelFileInfo.setType(fileInfo.getType());
                studyRelFileInfo.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
                studyRelFileInfoList.add(studyRelFileInfo);
            }
            studyRelFileInfoDao.batchAdd(studyRelFileInfoList);
        }
    }

    /**
     * 修改学习计划
     * @param study
     */
    @Override
    @Transactional
    public void update(Study study) {
        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        Long userId = userInfo.getId();
        study.setUpdateUserId(userId);
        study.setUpdateTime(new Date());

        //判断附件有没有改变
        StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
        studyRelFileInfo.setStudyId(study.getId());
		studyRelFileInfo.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());

        //如果fileInfos是空就跳过
        if (study.getFileInfos()!=null){
            //检验数据库中数据和传进来的fileid是否一致
            List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(studyRelFileInfo);
            List<Long> old = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(byParam)){
                for (StudyRelFileInfo s : byParam){
                    old.add(s.getId());
                }
            }
            List<Long> news = new ArrayList<>();
            for (FileInfo f : study.getFileInfos()){
                news.add(f.getId());
            }
            List<StudyRelFileInfo> arr = new ArrayList<>();
            if (!equalList(old,news)){
                //批量删除
                if (CollectionUtils.isNotEmpty(old)){
                    studyRelFileInfoDao.batchDel(old);
                }

                //批量新增
                for (FileInfo fileInfo : study.getFileInfos()){
                    StudyRelFileInfo s = new StudyRelFileInfo();
                    s.setStudyId(study.getId());
                    s.setFileId(fileInfo.getId());
                    s.setType(fileInfo.getType());
                    arr.add(s);
                }
                if (CollectionUtils.isNotEmpty(arr)){
                    studyRelFileInfoDao.batchAdd(arr);
                }
            }
        }
        List<Long> orgids = new ArrayList<>();
        List<Long> memberIds = new ArrayList<>();
        //如果全选了
        if(study.getAllOrgId()!=null){
            String s = RadixUtil.toFullBinaryString(Long.valueOf(pbOrgId));
            int level = RadixUtil.getlevel(s);
            if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
                List<PartyMember> membersByBiggerThanOrgId = partyMemberDao.findMembersByBiggerThanOrgId(Long.valueOf(pbOrgId));
                if (CollectionUtils.isNotEmpty(membersByBiggerThanOrgId)){
                    for (PartyMember partyMember : membersByBiggerThanOrgId) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(pbOrgId, true);
                if (CollectionUtils.isNotEmpty(subordinatesById)){
                    for (Organization organization : subordinatesById) {
                        orgids.add(Long.valueOf(organization.getId()));
                    }
                    List<PartyMember> partyMembers = partyMemberDao.batchFindMemberByOrgIds(orgids);
                    for (PartyMember partyMember : partyMembers) {
                        memberIds.add(partyMember.getId());
                    }
                }
            }
            if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()){
                List<Long> memberIdsByOrgId = partyMemberDao.findMemberIdsByOrgId(Long.valueOf(pbOrgId));
                memberIds.addAll(memberIdsByOrgId);
            }

            if (CollectionUtils.isNotEmpty(memberIds)){
                StringBuffer attendPersons = new StringBuffer(memberIds.size());
                for (Long memberId : memberIds) {
                    attendPersons = attendPersons.append(memberId);
                    attendPersons = attendPersons.append(",");
                }
                String persons = attendPersons.toString();
                persons= persons.substring(0,persons.length()-1);
                study.setAbsolutePerson(persons);
            }
        }

        studyDao.update(study);
    }

    private  boolean equalList(List list1, List list2) {
        if (list1.size() != list2.size()){
            return false;
        }

        if (list2.containsAll(list1)){
            return true;
        }

        return false;
    }

    /**
     * 删除学习计划
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {

        Study study = studyDao.queryById(id);
        if (study!=null){
            if (study.getIsRelease().equals(StudyReleaseStatusEnum.RELEASED.getCode())){
                throwBusinessException("已经发布不能删除");
            }
            //先查出来关联纪录
            StudyRelFileInfo studyRelFileInfo = new StudyRelFileInfo();
            studyRelFileInfo.setStudyId(id);
            studyRelFileInfo.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
            List<StudyRelFileInfo> byParam = studyRelFileInfoDao.findByParam(studyRelFileInfo);
            if (CollectionUtils.isNotEmpty(byParam)){
                //如果结果集数量==1
                if (byParam.size()==Const.RESULT_SIZE_ONLY_ONE){
                    studyRelFileInfoDao.delete(byParam.get(0).getId());
                }else {
                    //如果结果集为复数 就直接批量删除
                    List<Long> ids = new ArrayList<>();
                    for (StudyRelFileInfo sf : byParam){
                        ids.add(sf.getId());
                    }
                    studyRelFileInfoDao.batchDel(ids);
                }
            }
            studyDao.delete(id);
        }else {
            throwBusinessException("该条记录不存在");
        }
    }

    /**
     * 发布学习计划
     * @param st
     */
    @Override
    @Transactional
    public void release(Study st) {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        Long id = st.getId();
        Study obj = studyDao.queryById(id);
        if (obj==null){
            throwBusinessException("学习计划不存在");
        }
        if (!obj.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
            throwBusinessException("不是草稿不能发布");
        }

        Study study = new Study();
        study.setId(id);
        study.setReleaseTime(new Date());
        study.setIsRelease(StudyReleaseStatusEnum.RELEASED.getCode());
        study.setUpdateUserId(userId);
        study.setUpdateUserName(userInfo.getUsername());
        study.setUpdateRealName(userInfo.getRealName());
        study.setCreateTime(new Date());
        study.setVersion(obj.getVersion());
        List<Long> memberIds =null;
        //处理必学人员

        //得到参会人员
        //处理必学学习人员
        List<StudyRecord> studyRecordList = new ArrayList<>();
        if(StringUtil.isNotEmpty(obj.getAbsolutePerson())){
            memberIds= Arrays.asList(obj.getAbsolutePerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            //studyRecordList添加必学人员
            for (Long memberId : memberIds){
                StudyRecord record = new StudyRecord();
                record.setStudyId(id);
                record.setUserId(memberId);
                record.setUserType(StudyUserTypeEnum.ABSOLUTE.getCode());
                record.setStudyTime(null);
                record.setStudySituation(StudySituationEnum.NO.getCode());
                studyRecordList.add(record);
            }
        }
        studyDao.update(study);
        if (studyRecordList!=null && studyRecordList.size()>0){
            studyRecordDao.batchAdd(studyRecordList);
        }



        // 发送mq消息
      /*  MqMessage mqMessage = new MqMessage();
        mqMessage.setAppId(Const.APPID_PB);
        mqMessage.setBusinessId(id);
        mqMessage.setTemplateId(Long.valueOf(MessageTemplateEnum.STUDY_PLAN.getId()));*/
        //查询出党员 在查询出sys的userid
        StudyRecord sr = new StudyRecord();
        sr.setStudyId(id);
        sr.setUserType(StudyUserTypeEnum.ABSOLUTE.getCode());
        List<Long> pmIds = new ArrayList<>();
        List<StudyRecord> records = studyRecordDao.queryByParam(sr);
        for (StudyRecord record : records) {
            pmIds.add(record.getUserId());
        }
        if(CollectionUtils.isNotEmpty(pmIds)){
            //查询党员表 得到身份证
            List<String> stringList = partyMemberDao.batchSelectByIds(pmIds);
            if (stringList!=null && stringList.size()>0){
                List<Long> longList = sysServiceFeign.batchSelectByCardId(stringList);
                if (longList!=null && longList.size()>0){
                    Long[] targetId = new Long[longList.size()];
                    longList.toArray(targetId);
                    //mqMessage.setTargetId(targetId);
                    Organization byId = organizationDao.findById(obj.getPborgId().toString());
                    String creater=null;
                    if (byId!=null){
                        creater=byId.getName();
                    }
                    String[] params = {};
                    MqSendMessage mqSendMessage= AppPushMessageUtil.pushUserMessageByAlias(MessageTemplateEnum.STUDY_PLAN,targetId,params,creater,new Date(),null);
                    sendMqPushUtil.sendMqMessage(mqSendMessage);
                }
            }
           /* mqMessage.setTargetUserType(TargetTypeEnum.ALL.getCode()+"");
            mqMessage.setTargetType(TargetTypeEnum.ALL.getCode());*/

           /* String[] params = {};
            mqMessage.setParams(params);
            mqMessageUtil.assembleMqMessage(rabbitTemplate,mqMessage);*/

        }

    }
    /**
     * 镇团委发布学习计划
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo townRelease(Long id) {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();

        Study obj = studyDao.queryById(id);
        if (obj==null){
            throwBusinessException("学习计划不存在");
        }
        if (!obj.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
            throwBusinessException("不是草稿不能发布");
        }

        Study study = new Study();
        study.setId(id);
        study.setReleaseTime(new Date());
        study.setIsRelease(StudyReleaseStatusEnum.RELEASED.getCode());
        study.setUpdateUserId(userId);
        study.setUpdateUserName(userInfo.getUsername());
        study.setUpdateRealName(userInfo.getRealName());
        study.setVersion(obj.getVersion());

        studyDao.update(study);
        return new BaseVo();
    }

    private List<Long> getAttendPerson(Long pbOrgId){
        int level = RadixUtil.getlevel(pbOrgId + "");
        List<Long> attendPersons = new ArrayList<>();
        if (level == 1){
            List<Organization> allGrassRootsUnits = organizationDao.findAllGrassRootsUnits();
            List<Long> orgIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(allGrassRootsUnits)){
                for (Organization org : allGrassRootsUnits) {
                    orgIds.add(Long.valueOf(org.getId()));
                }
                attendPersons = partyMemberDao.findMemberIdsByOrgIds(orgIds);
            }
        }
        if (level == 2){
            List<Organization> subordinatesById = organizationDao.findSubordinatesById(pbOrgId + "", false);
            List<Long> orgIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(subordinatesById)){
                for (Organization org : subordinatesById) {
                    orgIds.add(Long.valueOf(org.getId()));
                }
            }
            orgIds.add(pbOrgId);
            attendPersons = partyMemberDao.findMemberIdsByOrgIds(orgIds);
        }
        if (level == 3){
            attendPersons = partyMemberDao.findMemberIdsByOrgId(pbOrgId);
        }

        return attendPersons;
    }

    /**
     * 查看学习计划
     * @param id
     * @return Study
     */
    @Override
    public BaseVo appReflect(Long id) {

        BaseVo baseVo = new BaseVo();

        UserInfo userInfo = getCurrentUserInfo();
        String idcard = userInfo.getIdcard();


        //根据id查询学习计划
        Study study = studyDao.queryById(id);
        if (study==null){
            throwBusinessException("学习计划不存在");
        }

        Organization byId = organizationDao.findById(study.getPborgId().toString());
        study.setPborgName(byId.getName());

        //根据id去查文件id
        List<FileInfo> fileInfos = new ArrayList<>();
        StudyRelFileInfo obj = new StudyRelFileInfo();
        obj.setStudyId(id);
        obj.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
        List<StudyRelFileInfo> studyRelFileInfoList = studyRelFileInfoDao.findByParam(obj);
        for (StudyRelFileInfo studyRelFileInfo : studyRelFileInfoList) {
            BaseVo b1 = fileServiceFeign.findById(studyRelFileInfo.getFileId());
            String s = JSON.toJSONString(b1.getData());
            FileInfo f1 = JSON.parseObject(s,FileInfo.class);
			f1.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
            fileInfos.add(f1);
        }

        if (CollectionUtils.isNotEmpty(fileInfos)){
            study.setFileInfos(fileInfos);
        }else {
            study.setFileInfos(null);
        }
        //根据id查询必学人员和自学人员
        StudyMemberDetail studyMemberDetail = new StudyMemberDetail();
        studyMemberDetail.setId(id);
        studyMemberDetail.setUserType(StudyUserTypeEnum.ABSOLUTE.getCode());
        List<StudyMemberDetail> lis1 = studyDao.querydetail(studyMemberDetail);
        if (CollectionUtils.isNotEmpty(lis1)){
            study.setAbsoluteMembers(lis1);
        }
        studyMemberDetail.setUserType(StudyUserTypeEnum.OPTIONAL.getCode());
        List<StudyMemberDetail> lis2 = studyDao.querydetail(studyMemberDetail);
        if (CollectionUtils.isNotEmpty(lis2)){
            study.setOptionalMembers(lis2);
        }
        PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(idcard);
        if (byIdCardAndStatus!=null){
            for (StudyMemberDetail smd : lis1) {
                if (byIdCardAndStatus.getId().equals(smd.getMemberId())){
                    study.setStudyTime(smd.getStudyTime());
                }
            }
            for (StudyMemberDetail smd : lis2) {
                if (byIdCardAndStatus.getId().equals(smd.getMemberId())){
                    study.setStudyTime(smd.getStudyTime());
                }
            }
        }

        baseVo.setData(study);
        return baseVo;

    }


    /**
     * 一级机构--->二级 + 三级
     * 二级机构--->自己 + 三级
     * 三级机构 ：如果是党支部 orgtype=3 --->自己
     *           如果不是党支部  --->自己+下级
     * 四级机构--->自己+上级
     * @param studyVO
     * @return
     */
    @Override
    public BaseVo listPage(StudyVO studyVO) {

        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        List<Long> idList = new ArrayList<>();
        List<Study> studyList = new ArrayList<>();
        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);
        if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
            if (studyVO.getPbId()==null){
                List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
                if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)){
                    for (Organization organization : orgByBiggerThanOrgId) {
                        idList.add(Long.valueOf(organization.getId()));
                    }
                }
            }else {
                idList.add(studyVO.getPbId());
            }
            studyVO.setIdList(idList);
            PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
            studyList = studyDao.listPageForTown(studyVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
            List<Long> noids = new ArrayList<>();
            if (studyVO.getPbId()==null){
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), false);
                if (CollectionUtils.isNotEmpty(subordinatesById)){
                    for (Organization organization : subordinatesById) {
                        noids.add(Long.valueOf(organization.getId()));
                    }
                }
                idList.add(pbOrgId);
            }else {
                if (studyVO.getPbId().equals(pbOrgId)){
                    studyVO.setIsMeFlag(BooleanEnum.YES.getCode());
                }else {
                    studyVO.setIsMeFlag(BooleanEnum.NO.getCode());
                }
                idList.add(studyVO.getPbId());
            }
            studyVO.setIdList(idList);
            studyVO.setNoidList(noids);
            PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
            studyList = studyDao.listPageForTwo(studyVO);
        }
        if (level >= PartyOrgLevelEnum.LEVEL_THREE.getCode()){
            List<Long> noids = new ArrayList<>();
			Organization org = organizationDao.findById(pbOrgId.toString());
			if (org==null){
				throwBusinessException("该组织不存在");
			}
			if (org.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())){
				idList.add(pbOrgId);
                noids.add(pbOrgId);
			}else {
				idList.add(pbOrgId);
				List<OrganizationInfoVO> directSubordinatesInfoById = organizationDao.findDirectSubordinatesInfoById(pbOrgId.toString());
				if (CollectionUtils.isNotEmpty(directSubordinatesInfoById)){
					for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById) {
						noids.add(Long.valueOf(organizationInfoVO.getId()));
					}
				}
				noids.add(pbOrgId);
			}
			studyVO.setNoidList(noids);
            studyVO.setIdList(idList);
            PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
            studyList = studyDao.listPage(studyVO);
        }
        if (level == PartyOrgLevelEnum.LEVEL_FOUR.getCode()){
			//能看自己
			idList.add(pbOrgId);
			studyVO.setIdList(idList);
			PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
			studyList = studyDao.listPage(studyVO);
		}
		for (Study study : studyList) {
			if (study.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
				study.setReleaseTime(null);
			}
		}

        PageInfo<Study> pageInfo = new PageInfo<Study>(studyList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * web端镇党委学习计划分页查询
     * @param studyVO
     * @return
     */
    @Override
    public BaseVo webTownlistPage(StudyVO studyVO) {

        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        Long topOrg = organizationDao.findTopOrg();
        studyVO.setPborgId(topOrg);
        //判断当前用户是不是镇团委
        if (studyVO.getPborgId().equals(Long.valueOf(pbOrgId))){
            studyVO.setIsMeFlag(BooleanEnum.YES.getCode());
        }else {
            studyVO.setIsMeFlag(BooleanEnum.NO.getCode());
        }

        PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
        List<Study> studyList = studyDao.townStudyListPage(studyVO);
		for (Study study : studyList) {
			if (study.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
				study.setReleaseTime(null);
			}
		}
		PageInfo<Study> pageInfo = new PageInfo<Study>(studyList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * app端我的学习计划分页查询
	 * 一级机构--->二级 + 三级
	 * 二级机构--->自己 + 三级
	 * 三级机构 ：如果是党支部 orgtype=3 --->自己
	 *           如果不是党支部  --->自己+下级
	 * 四级机构--->自己+上级
     * @param studyVO
     * @retutn BaseVo
     */
    @Override
    public BaseVo appStudyPlanListPage(StudyVO studyVO) {

        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);
        List<Long> idList = new ArrayList<>();
        List<Study> studyList = new ArrayList<>();
        //查询当前身份
        BaseVo byId = sysServiceFeign.findById(userInfo.getCurrentIdentity());
        if (byId.getData()!=null){
            String ss = JSON.toJSONString(byId.getData());
            Identity identity = JSON.parseObject(ss,Identity.class);
            //如果是党员 能看自己+二级
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.MEMBER.getCode())){
                idList.add(pbOrgId);
                Organization directSuperiorById = organizationDao.findDirectSuperiorById(pbOrgId.toString(), null);
                //不能是镇一级
                if (directSuperiorById!=null){
                    String tt = RadixUtil.toFullBinaryString(Long.valueOf(directSuperiorById.getId()));
                    int ll = RadixUtil.getlevel(tt);
                    if (ll!=1){
                        idList.add(Long.valueOf(directSuperiorById.getId()));
                    }
                }
                studyVO.setIdList(idList);
                PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(userInfo.getIdcard());
                if (byIdCardAndStatus!=null){
                    studyVO.setUserId(byIdCardAndStatus.getId());
                }
                PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
                studyList = studyDao.appStudyPlanListPage(studyVO);
            }
            //如果是管理员
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.ADMIN.getCode())){
                if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
                    //能看 二级+三级
                    if (studyVO.getPbId()==null){
                        List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
                        for (Organization organization : orgByBiggerThanOrgId) {
                            idList.add(Long.valueOf(organization.getId()));
                        }
                    }else {
                        idList.add(studyVO.getPbId());
                    }
                    studyVO.setIdList(idList);
                    PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
                    studyList = studyDao.appStudyPlanAdminListPage(studyVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
                    //能看自己+三级
                    if (studyVO.getPbId()==null){
                        List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), true);
                        for (Organization organization : subordinatesById) {
                            idList.add(Long.valueOf(organization.getId()));
                        }
                    }else {
                        idList.add(studyVO.getPbId());
                    }
                    studyVO.setIdList(idList);
                    PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
                    studyList = studyDao.appStudyPlanAdminListPage(studyVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()){
					//如果orgType是2 or 4 查询自己+下级
					//如果orgtype是3 查询自己+上级 + (过滤条件)
					//20190814 与需求沟通先不查询上级

					Organization byIdOrg = organizationDao.findById(pbOrgId.toString());
					if (byIdOrg==null){
						throwBusinessException("该组织不存在");
					}
					//如果orgType是2 or 4 查询自己+下级
					if (byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_TWO.getCode()) || byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_FOUR.getCode())){
						idList.add(pbOrgId);
						List<OrganizationInfoVO> directSubordinatesInfoById = organizationDao.findDirectSubordinatesInfoById(pbOrgId.toString());
						if (CollectionUtils.isNotEmpty(directSubordinatesInfoById)){
							for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById) {
								idList.add(Long.valueOf(organizationInfoVO.getId()));
							}
						}
						studyVO.setIdList(idList);
					}else if (byIdOrg.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())) {
						//如果是党支部 orgtype=3  自己+上级 + (过滤条件)
						idList.add(pbOrgId);
//						Organization up = organizationDao.findDirectSuperiorById(pborgId.toString(),null);
//						if (up!=null){
//							idList.add(Long.valueOf(up.getId()));
//						}
						studyVO.setIdList(idList);
					}else {
						throwBusinessException("机构错误");
					}
                    PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
                    studyList = studyDao.appStudyPlanAdminListPage(studyVO);
                }
                if (level == PartyOrgLevelEnum.LEVEL_FOUR.getCode()){
                    //自己+上级
                    //20190814 与需求沟通先不查询上级
                    idList.add(pbOrgId);
//					Organization up = organizationDao.findDirectSuperiorById(pborgId.toString(),null);
//					if (up!=null){
//						idList.add(Long.valueOf(up.getId()));
//					}
                    studyVO.setIdList(idList);
                    PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
                    studyList = studyDao.appStudyPlanAdminListPage(studyVO);
                }
            }
        }
        for (Study study : studyList) {
            if (study.getStudySituation() == null){
                study.setStudySituation(StudySituationEnum.NO.getCode());
            }
        }


        PageInfo<Study> pageInfo = new PageInfo<Study>(studyList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * app端我的学习计划分页查询
     * @param studyVO
     * @retutn BaseVo
     */
    @Override
    public BaseVo appMyStudyPlanListPage(StudyVO studyVO) {
        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        //登录人的组织id
        String idcard = userInfo.getIdcard();
        PartyMember member = partyMemberDao.findByIdCardAndStatus(idcard);
        //显示本组织及上级基层组织发布的学习计划 显示“已学习”状态的学习课程
        List<Long> idList = new ArrayList<>();
        //查询上级组织
        Organization directSuperiorById = organizationDao.findDirectSuperiorById(pbOrgId, null);
        if (directSuperiorById!=null){
            String s1 = RadixUtil.toFullBinaryString(Long.valueOf(pbOrgId));
            int ll = RadixUtil.getlevel(s1);
            if (ll!=1){
                idList.add(Long.valueOf(directSuperiorById.getId()));
            }
        }
        idList.add(Long.valueOf(pbOrgId));
        studyVO.setIdList(idList);
        studyVO.setUserId(member.getId());
        PageHelper.startPage(studyVO.getPageNum(),studyVO.getPageSize());
        List<Study> studyList = studyDao.appMyStudyPlanListPage(studyVO);
        PageInfo<Study> pageInfo = new PageInfo<Study>(studyList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查看学习情况记录
     */
    @Override
    public BaseVo querydetail(StudyMemberDetailVO studyMemberDetailVO) {
        PageHelper.startPage(studyMemberDetailVO.getPageNum(),studyMemberDetailVO.getPageSize());
        List<StudyMemberDetail> querydetail = new ArrayList<>();
        //必学人员
        if (studyMemberDetailVO.getUserType() == StudyUserTypeEnum.ABSOLUTE.getCode()){
            querydetail = studyDao.querydetailListPage(studyMemberDetailVO);
            for (StudyMemberDetail studyMemberDetail : querydetail) {
                if (studyMemberDetail.getStudyTime()!=null){
                    Integer time = studyMemberDetail.getStudyTime();
                    String strTime = secondToTime(time);
                    studyMemberDetail.setStrTime(strTime);
                }else {
                    String strTime = "------";
                    studyMemberDetail.setStrTime(strTime);
                }
            }
        }
        //自学人员
        if (studyMemberDetailVO.getUserType() == StudyUserTypeEnum.OPTIONAL.getCode()){
            Long studyId = studyMemberDetailVO.getId();
            Study study = studyDao.queryById(studyId);
            Long pbOrgId = study.getPborgId();
            List<Long> attendPerson = getAttendPerson(pbOrgId);
            if (CollectionUtils.isNotEmpty(attendPerson)){
                studyMemberDetailVO.setUserIds(attendPerson);
                querydetail = studyDao.querydetailListPage(studyMemberDetailVO);
            }
        }

        PageInfo<StudyMemberDetail> pageInfo =new PageInfo<StudyMemberDetail>(querydetail);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }
    /**
     * 将秒数转换为日时分秒，
     * @param second
     * @return
     */
    public static String secondToTime(long second){
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        return hours + ":" + minutes + ":" + second ;
    }

    @Override
    public void exportToExcel(HttpServletResponse response,StudyExcelParam studyExcelParam) {
        List<Study> studies = new ArrayList<>();
        String fileName = "";
        //基层数据导出
        if (studyExcelParam.getExcelType().equals(ExcelExportTypeEnum.BASE.getCode())){
            studies = getBaseDataForExcelListPage(studyExcelParam);
            if (studies==null || studies.size()<=0){
                throwBusinessException("无数据可供导出");
            }
            fileName = "基层学习计划"+ DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        }
        //镇团委导出
        if (studyExcelParam.getExcelType().equals(ExcelExportTypeEnum.WEB.getCode())){
            studies = getWebDataForExcelListPage(studyExcelParam);
            if (studies==null || studies.size()<=0){
                throwBusinessException("无数据可供导出");
            }
            fileName = "镇团委学习计划"+ DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        }


        List<StudyExcel> studyExcelList = new ArrayList<>();
        int i = 1;
        for (Study study : studies) {
            StudyExcel studyExcel = new StudyExcel();
            BeanUtils.copyProperties(study,studyExcel);
            if (study.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
                studyExcel.setIsRelease("草稿");
            }
            if (study.getIsRelease().equals(StudyReleaseStatusEnum.RELEASED.getCode())){
                studyExcel.setIsRelease("已发布");
            }
            studyExcel.setId(i);
            studyExcelList.add(studyExcel);
            i++;
        }
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
            response.setHeader("content-type","application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //导出操作
            ExcelUtil.exportExcel(studyExcelList,null,"学习计划列表",StudyExcel.class,fileName,response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private List<Study> getWebDataForExcelListPage(StudyExcelParam studyExcelParam){
        UserInfo userInfo = getCurrentUserInfo();
        String pbOrgId = userInfo.getPbOrgId();
        Long topOrg = organizationDao.findTopOrg();
        studyExcelParam.setPborgId(topOrg);
        //判断当前用户是不是镇团委
        if (studyExcelParam.getPborgId().equals(Long.valueOf(pbOrgId))){
            studyExcelParam.setIsMeFlag(BooleanEnum.YES.getCode());
        }else {
            studyExcelParam.setIsMeFlag(BooleanEnum.NO.getCode());
        }

        List<Study> studyList = studyDao.townStudyListPageForExcel(studyExcelParam);
        return studyList;
    }

    private List<Study> getBaseDataForExcelListPage(StudyExcelParam studyExcelParam){
        UserInfo userInfo = getCurrentUserInfo();
        //登录人的组织id
        Long pbOrgId = Long.valueOf(userInfo.getPbOrgId());
        List<Long> idList = new ArrayList<>();
        List<Study> studyList = new ArrayList<>();
        String s = RadixUtil.toFullBinaryString(pbOrgId);
        int level = RadixUtil.getlevel(s);
        if (level == PartyOrgLevelEnum.LEVEL_ONE.getCode()){
			if (studyExcelParam.getPbId()==null){
				List<Organization> orgByBiggerThanOrgId = organizationDao.findOrgByBiggerThanOrgId(pbOrgId);
				if (CollectionUtils.isNotEmpty(orgByBiggerThanOrgId)){
					for (Organization organization : orgByBiggerThanOrgId) {
						idList.add(Long.valueOf(organization.getId()));
					}
				}
			}else {
				idList.add(studyExcelParam.getPbId());
			}
            studyExcelParam.setIdList(idList);
            studyList = studyDao.listPageForTownExcel(studyExcelParam);
        }
        if (level == PartyOrgLevelEnum.LEVEL_TWO.getCode()){
            List<Long> noids = new ArrayList<>();
            if (studyExcelParam.getPbId()==null){
                List<Organization> subordinatesById = organizationDao.findSubordinatesById(userInfo.getPbOrgId(), false);
                if (CollectionUtils.isNotEmpty(subordinatesById)){
                    for (Organization organization : subordinatesById) {
                        noids.add(Long.valueOf(organization.getId()));
                    }
                    idList.add(pbOrgId);
                }
            }else {
                idList.add(studyExcelParam.getPbId());
            }
            studyExcelParam.setIdList(idList);
            studyExcelParam.setNoidList(noids);
            studyList = studyDao.listPageForTwoExcel(studyExcelParam);
        }
        if (level == PartyOrgLevelEnum.LEVEL_THREE.getCode()){
            idList.add(pbOrgId);
            studyExcelParam.setIdList(idList);
            studyList = studyDao.listPageForExcel(studyExcelParam);
        }
        return studyList;
    }

    /**
     * app端删除学习计划与图片的关系
     * @param id
     * @return
     */
    @Override
    public BaseVo appdelStudyImage(Long id) {

        studyRelFileInfoDao.delete(id);
        return new BaseVo();
    }

    /**
     * 添加学习计划与图片的关系
     * @param studyRelFileInfo
     * @return
     */
    @Override
    public BaseVo appUploadImage(StudyRelFileInfo studyRelFileInfo) {
        studyRelFileInfo.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
        studyRelFileInfoDao.add(studyRelFileInfo);
        return new BaseVo();
    }
    /**
     * 单查记录
     * @param id
     * @return
     */
    @Override
    public BaseVo findById(Long id) {
        BaseVo baseVo = new BaseVo();
        //根据id查询学习计划
        Study study = studyDao.queryById(id);

        Organization byId = organizationDao.findById(study.getPborgId().toString());
        study.setPborgName(byId.getName());

        //根据id去查文件id
        List<FileInfo> fileInfos = new ArrayList<>();
        StudyRelFileInfo obj = new StudyRelFileInfo();
        obj.setStudyId(id);
        obj.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
        List<StudyRelFileInfo> studyRelFileInfoList = studyRelFileInfoDao.findByParam(obj);
        for (StudyRelFileInfo studyRelFileInfo : studyRelFileInfoList) {
            BaseVo b1 = fileServiceFeign.findById(studyRelFileInfo.getFileId());
            String s = JSON.toJSONString(b1.getData());
            FileInfo f1 = JSON.parseObject(s,FileInfo.class);
			f1.setType(FileTypeEnum.STUDY_PLAN_FILE.getCode());
            fileInfos.add(f1);
        }

        study.setFileInfos(fileInfos);

        //返显必学人员
        if (StringUtil.isNotEmpty(study.getAbsolutePerson())){
            List<Long> listIds = Arrays.asList(study.getAbsolutePerson().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<PartyMemberOrgName> detailByMemberIds = partyMemberDao.findDetailByMemberIds(listIds);
            study.setPartyMemberOrgNameList(detailByMemberIds);
        }
        //处理草稿状态 发布时间问题
		if (study.getIsRelease().equals(StudyReleaseStatusEnum.NOT_RELEASED.getCode())){
			study.setReleaseTime(null);
		}

        baseVo.setData(study);
        return baseVo;
    }

}
