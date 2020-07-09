package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.StudySituationEnum;
import com.bit.common.enumerate.StudyUserTypeEnum;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.bean.Study;
import com.bit.module.pb.bean.StudyRecord;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.dao.StudyDao;
import com.bit.module.pb.dao.StudyRecordDao;
import com.bit.module.pb.service.StudyRecordService;
import com.bit.module.pb.vo.StudyRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-25 15:15
 */
@Service("studyRecordService")
public class StudyRecordServiceImpl extends BaseService implements StudyRecordService {

    @Autowired
    private StudyRecordDao studyRecordDao;
    @Autowired
    private PartyMemberDao partyMemberDao;
    @Autowired
    private StudyDao studyDao;
    /**
     * 学习记录查询
     * @param studyRecordVO
     * @return
     */
    @Override
    public BaseVo listPage(StudyRecordVO studyRecordVO) {

        PageHelper.startPage(studyRecordVO.getPageNum(),studyRecordVO.getPageSize());
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo!=null){
            String idcard = userInfo.getIdcard();
            PartyMember byIdCard = partyMemberDao.findByIdCardAndStatus(idcard);
            if (byIdCard!=null){
                studyRecordVO.setUserId(byIdCard.getId());
            }else {
                throwBusinessException("此人信息不存在");
            }
        }else {
            throwBusinessException("请重新登录");
        }


        List<StudyRecord> records = studyRecordDao.listPage(studyRecordVO);
        PageInfo<StudyRecord> pageInfo = new PageInfo<>(records);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 更新学习记录时间
     * @param studyRecord
     * @return
     */
    @Override
    @Transactional
    public BaseVo updateStudyTime(StudyRecord studyRecord) {
        Study study = studyDao.queryById(studyRecord.getStudyId());
        if (study==null){
            throwBusinessException("无此学习计划");
        }
        UserInfo userInfo = getCurrentUserInfo();
        String cardId = userInfo.getIdcard();
        //根据身份证查询党员id
        PartyMember byIdCardAndStatus = partyMemberDao.findByIdCardAndStatus(cardId);
        if (byIdCardAndStatus!=null){
            StudyRecord temp = new StudyRecord();
            temp.setUserId(byIdCardAndStatus.getId());
            temp.setStudyId(studyRecord.getStudyId());
            List<StudyRecord> studyRecords = studyRecordDao.queryByParam(temp);
            if (studyRecords!=null && studyRecords.size()>1){
                throwBusinessException("学习记录异常");
            }
            //20190831 添加自学人员
            if (CollectionUtils.isEmpty(studyRecords)){
                //如果学习记录为空 就新增一条
                StudyRecord obj = new StudyRecord();
                obj.setStudyId(studyRecord.getStudyId());
                obj.setStudyTime(studyRecord.getStudyTime());
                obj.setStudySituation(StudySituationEnum.YES.getCode());
                obj.setUserId(byIdCardAndStatus.getId());
                obj.setUserType(StudyUserTypeEnum.OPTIONAL.getCode());
                studyRecordDao.add(obj);
            }

            if (studyRecords!=null && studyRecords.size() == 1){
                Long id = studyRecords.get(0).getId();
                StudyRecord obj = new StudyRecord();
                obj.setId(id);
                obj.setStudyTime(studyRecord.getStudyTime());
                obj.setStudySituation(StudySituationEnum.YES.getCode());
                studyRecordDao.update(obj);
            }
        }
        return new BaseVo();
    }
}
