package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ConferenceRecord;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.dao.ConferenceRecordDao;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.service.ConferenceRecordService;
import com.bit.module.pb.vo.ConferenceRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-25 14:04
 */
@Service("conferenceRecordService")
public class ConferenceRecordImpl extends BaseService implements ConferenceRecordService {
    @Autowired
    private ConferenceRecordDao conferenceRecordDao;
    @Autowired
    private PartyMemberDao partyMemberDao;
    /**
     * 分页查询
     * @param conferenceRecordVO
     * @return
     */
    @Override
    public BaseVo listPage(ConferenceRecordVO conferenceRecordVO) {
        BaseVo baseVo = new BaseVo();
        PageHelper.startPage(conferenceRecordVO.getPageNum(),conferenceRecordVO.getPageSize());

        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo!=null){
            String idcard = userInfo.getIdcard();
            PartyMember byIdCard = partyMemberDao.findByCondition(null, idcard);
            if (byIdCard!=null){
                conferenceRecordVO.setUserId(byIdCard.getId());
            }else {
                throwBusinessException("此人信息不存在");
            }
        }else {
            throwBusinessException("请重新登录");
        }



        List<ConferenceRecord> conferenceRecords = conferenceRecordDao.listPage(conferenceRecordVO);
        PageInfo<ConferenceRecord> pageInfo=new PageInfo<>(conferenceRecords);
        baseVo.setData(pageInfo);
        return baseVo;
    }
}
