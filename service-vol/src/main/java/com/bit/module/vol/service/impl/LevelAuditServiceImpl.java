package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ApplyLogCode;
import com.bit.common.AuditMessageTemplate;
import com.bit.common.BaseConst;
import com.bit.common.ResultCode;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.LevelAuditEnum;
import com.bit.module.applylogs.bean.ApplyLogs;
import com.bit.module.applylogs.repository.ApplyRepository;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.LevelAuditDao;
import com.bit.module.vol.dao.LevelRegulationDao;
import com.bit.module.vol.dao.StationDao;
import com.bit.module.vol.dao.VolunteerDao;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.LevelAuditService;
import com.bit.module.vol.vo.LevelAuditVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.TypeUtil;
import com.bit.utils.VolUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-20 13:58
 */
@Service("levelAuditService")
public class LevelAuditServiceImpl extends BaseService implements LevelAuditService {
    @Autowired
    private LevelAuditDao levelAuditDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private SysServiceFeign sysServiceFeign;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private VolunteerDao volunteerDao;
    @Autowired
    private LevelRegulationDao levelRegulationDao;
    @Autowired
    private VolUtil volUtil;
    @Autowired
    private TypeUtil typeUtil;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;

    /**
     * 新增审核记录
     * @param levelAudit
     * @return
     */
    @Override
    @Transactional
    public BaseVo add(LevelAudit levelAudit,HttpServletRequest request) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();
        Long stationId = vi.getStationId();

		Volunteer vv = volunteerDao.findById(userId);
		//插入等级审核记录
		insertLevelAudit(levelAudit, userId, stationId, vv);

        //获取appId
        String terminalId = request.getHeader("tid");

        Station byId = stationDao.findById(vi.getStationId());
        String content = byId.getStationName() + vv.getRealName()+ ApplyLogCode.AUDIT_LEVEL_SUBMIT.getInfo();
        ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId), BaseConst.AUDIT_LOG_TYPE_VOL_LEVEL, levelAudit.getId(), userId, vv.getRealName(),
                AuditMessageTemplate.OPERATION_NAME_LEVEL_ADD, new Date(), 0, DateUtil.format(new Date()), content, "");
        applyRepository.addApplyLogs(applyLogs);


        Volunteer temp = volunteerDao.findById(levelAudit.getVolunteerId());
        Long std = temp.getStationId();
        Long[] targetId = new Long[]{std};
        String[] params = {temp.getRealName(),vv.getServiceLevel().toString(),levelAudit.getApplyLevel().toString()};

        //发送到web端
        MqSendMessage mqSendMessageTask= AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.VOL_LEVEL_AUDIT_APPLY,targetId,params,levelAudit.getId(),0,  vv.getRealName(),new Date(),null);
        sendMqPushUtil.sendMqMessage(mqSendMessageTask);
        return new BaseVo();
    }

	/**
	 * 插入等级审核记录
	 * @param levelAudit
	 * @param userId
	 * @param stationId
	 * @param vv
	 */
	private void insertLevelAudit(LevelAudit levelAudit, Long userId, Long stationId, Volunteer vv) {
		levelAudit.setVersion(0);
		levelAudit.setCreateTime(new Date());
		levelAudit.setCreateUserId(userId);
		levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_VERIFYING_OWN.getCode());
		levelAudit.setBelongStationId(stationId);
		levelAudit.setAuditStationId(stationId);
		levelAudit.setVolunteerId(userId);
		Station byId1 = stationDao.findById(stationId);
		if (byId1!=null && byId1.getStationLevel()==1){
			levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_VERIFYING_UP.getCode());
		}

		levelAudit.setOldLevel(vv.getServiceLevel());

		levelAuditDao.add(levelAudit);
	}

	/**
     * 更新审核记录 镇团委
     * @param levelAudit
     * @return
     */
    @Override
    @Transactional
    public BaseVo zupdate(LevelAudit levelAudit, HttpServletRequest request) {
        Integer audit = levelAudit.getAuditStatus();
        UserInfo userInfo = getCurrentUserInfo();
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();

        LevelAudit byId = levelAuditDao.findById(levelAudit.getId());
        if (byId==null){
            throwBusinessException("该记录不存在");
        }
        //审批前验证审批状态
        if (!byId.getVersion().equals(levelAudit.getVersion())){
            return new BaseVo(ResultCode.ADUITS_ALREADY_HANDLE.getCode(),ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
        }
        levelAudit.setUpdateTime(new Date());
        levelAudit.setUpdateUserId(userId);

        String content = "";
        String[] params = {};
        Volunteer vv = volunteerDao.findById(byId.getVolunteerId());

        //调用sys查询userid
        Long sysuserid = getUserByCardId(vv);
        //可能存在多个镇团委
        Long stationId = vi.getStationId();
        Station topStationInfo = stationDao.findById(stationId);
        //如果申请人是镇团委 就直接通过或拒绝
        Long applyId = byId.getVolunteerId();
        Volunteer applyVol = volunteerDao.findById(applyId);

		Long[] targetId = null;
        //发送模板
        MessageTemplateEnum msEnum=null;
        if (applyVol==null){
			throwBusinessException("申请人身份异常");
		}

		//如果申请人是镇团委
		if (applyVol.getStationId().equals(stationId)){
			//镇团委通过或拒绝
			//设置记录状态
			//自己通过  消息
			if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode())){
				levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode());
				content = AuditMessageTemplate.AUDIT_LEVEL_PASSED_ZHENTUANWEI;
				msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_PASS_APPLY;
				targetId = new Long[]{sysuserid};
			}else if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_REJECTED.getCode())){
				//自己拒绝  消息
				levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_REJECTED.getCode());
				content = AuditMessageTemplate.AUDIT_LEVEL_REJECTED_ZHENTUANWEI+levelAudit.getRejectReason();
				msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_REJECTED_APPLY;
				targetId = new Long[]{sysuserid};
				params = new String[]{levelAudit.getRejectReason()};
			}
		}else {
			//服务站通过或拒绝
			//设置记录状态
			//自己通过
			if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode())){
				levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode());
				content = AuditMessageTemplate.AUDIT_LEVEL_PASSED_ZHENTUANWEI;
				msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_PASS_APPLY;
				targetId = new Long[]{sysuserid};
			}
			//自己拒绝
			if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_REJECTED.getCode())){
				levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_VERIFYING_OWN.getCode());
				levelAudit.setAuditStationId(byId.getBelongStationId());
				content = AuditMessageTemplate.AUDIT_LEVEL_REJECTED_ZHENTUANWEI+levelAudit.getRejectReason();
				msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_REJECTED;
				targetId = new Long[]{byId.getBelongStationId()};
				params = new String[]{vv.getRealName(),levelAudit.getRejectReason()};
			}
		}

        Integer version = byId.getVersion();
        if (version==null){
            version = 0;
        }
        levelAudit.setVersion(version);
        levelAuditDao.update(levelAudit);

        if (audit.equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode())){
            Volunteer vo = volunteerDao.findById(byId.getVolunteerId());
            Integer v = vo.getVersion();
            if (v==null){
                v = 0;
            }
            //更新志愿者等级
            Volunteer volunteer = new Volunteer();
            volunteer.setId(vo.getId());
            volunteer.setServiceLevel(byId.getApplyLevel());
            volunteer.setVersion(v);
            volunteerDao.update(volunteer);
        }

		//消息推送
		sendMqMessage(levelAudit, targetId, topStationInfo.getStationName(), msEnum, params, version);

		//生成日志
		//获取appId
		String terminalId = request.getHeader("tid");
		ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId), BaseConst.AUDIT_LOG_TYPE_VOL_LEVEL, levelAudit.getId(), userId, userInfo.getRealName(),
				AuditMessageTemplate.OPERATION_NAME_ZHENTUANWEI, new Date(), 0, DateUtil.format(new Date()), content, "");
		applyRepository.addApplyLogs(applyLogs);
        return new BaseVo();
    }


	/**
	 * 调用sys查询userid
	 * @param vv
	 * @return
	 */
	private Long getUserByCardId(Volunteer vv) {
		BaseVo b1 = sysServiceFeign.queryUserByIdcard(vv.getCardId());
		if (b1.getData()==null){
			throwBusinessException("此人不存在");
		}
		String s1 = JSON.toJSONString(b1.getData());
		User user = JSON.parseObject(s1,User.class);
		return user.getId();
	}

	/**
     * 服务站更新审核记录
     * @param levelAudit
     * @return
     */
    @Override
    @Transactional
    public BaseVo fupdate(LevelAudit levelAudit,HttpServletRequest request) {
        UserInfo userInfo = getCurrentUserInfo();
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();
        //审批前验证审批状态
        LevelAudit check = levelAuditDao.findById(levelAudit.getId());
        if (check!=null){
            if (!check.getVersion().equals(levelAudit.getVersion())){
                return new BaseVo(ResultCode.ADUITS_ALREADY_HANDLE.getCode(),ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
            }
        }else {
            throwBusinessException("该记录不存在");
        }
        //根据用户id查询志愿者站点关联关系
        Long stationId = getStationIdByUserId(userId);

        //上级机构id
        Long upid = Long.parseLong(stationId.toString().substring(0,3));
        Station station = stationDao.findById(stationId);
        levelAudit.setUpdateTime(new Date());
        levelAudit.setUpdateUserId(userId);

		String content = "";
		Long[] targetId = null;
		String creater = "";
        MessageTemplateEnum msEnum=null;

		if (vi.getStationId()!=null){
			Station byId = stationDao.findById(vi.getStationId());
			if (byId!=null){
				creater = byId.getStationName();
			}else {
				throwBusinessException("当前操作人非法");
			}
		}else {
			throwBusinessException("当前操作人非法");
		}

        Volunteer vv = volunteerDao.findById(check.getVolunteerId());
        String[] params = {};

        //调用sys查询userid
		Long sysuserid = getUserIdByCardId(vv);

        //设置记录状态
        //自己通过
        if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_PASSED.getCode())){
            levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_VERIFYING_UP.getCode());
            levelAudit.setAuditStationId(upid);
            content = station.getStationName() + AuditMessageTemplate.AUDIT_LEVEL_PASSED_STATION;
            //待办
            msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_APPLY;
            targetId = new Long[]{upid};
            params = new String[]{vv.getRealName(),vv.getServiceLevel().toString(),check.getApplyLevel().toString()};
        }else if (levelAudit.getAuditStatus().equals(LevelAuditEnum.LEVEL_AUDIT_STATUS_REJECTED.getCode())){
			//自己拒绝
            levelAudit.setAuditStatus(LevelAuditEnum.LEVEL_AUDIT_STATUS_REJECTED.getCode());
            content = station.getStationName() + AuditMessageTemplate.AUDIT_LEVEL_REJECTED_STATION+levelAudit.getRejectReason();
            //消息
            msEnum=MessageTemplateEnum.VOL_LEVEL_AUDIT_REJECTED_APPLY;
            targetId = new Long[]{sysuserid};
            params= new String[]{levelAudit.getRejectReason()};
        }
        Integer version = check.getVersion();
        if (version==null){
            version = 0;
        }
        levelAudit.setVersion(version);
        levelAuditDao.update(levelAudit);

		//消息推送
		sendMqMessage(levelAudit, targetId, creater, msEnum, params, version);

		//获得日志
		//获取appId
		String terminalId = request.getHeader("tid");
		ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId), BaseConst.AUDIT_LOG_TYPE_VOL_LEVEL, levelAudit.getId(), userId, userInfo.getRealName(),
				AuditMessageTemplate.OPERATION_NAME_STATION, new Date(), 0, DateUtil.format(new Date()), content, "");
		applyRepository.addApplyLogs(applyLogs);

        return new BaseVo();
    }

	/**
	 * 消息推送
	 * @param levelAudit
	 * @param targetId
	 * @param creater
	 * @param msEnum
	 * @param params
	 * @param version
	 */
	private void sendMqMessage(LevelAudit levelAudit, Long[] targetId, String creater, MessageTemplateEnum msEnum, String[] params, Integer version) {
		MqSendMessage mqSendMessage=null;
		if(msEnum.getId()==MessageTemplateEnum.VOL_LEVEL_AUDIT_APPLY.getId()){
			//发送待办
			mqSendMessage= AppPushMessageUtil.pushOrgTaskByAlias(msEnum,targetId,params,levelAudit.getId(),version+1,creater,new Date(),null);
		}else {
			//发送消息
			mqSendMessage= AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(levelAudit.getId(),msEnum,targetId,params,creater,new Date(),null);
		}
		if (mqSendMessage!=null){
			sendMqPushUtil.sendMqMessage(mqSendMessage);
		}
	}

	/**
	 * 根据身份证查询用户id
	 * @param vv
	 * @return
	 */
	private Long getUserIdByCardId(Volunteer vv) {
		BaseVo b2 = sysServiceFeign.queryUserByIdcard(vv.getCardId());
		if (b2==null){
			throwBusinessException("此人不存在");
		}
		String s2 = JSON.toJSONString(b2.getData());
		User user = JSON.parseObject(s2,User.class);
		return user.getId();
	}


	/**
	 * 根据用户id查询志愿者站点关联关系
	 * @param userId
	 * @return
	 */
	private Long getStationIdByUserId(Long userId) {
		BaseVo b1 = sysServiceFeign.queryByUserId(userId);
		if (b1==null){
			throwBusinessException("此人没有分配服务站");
		}
		String s1 = JSON.toJSONString(b1.getData());
		UserRelVolStation userRelVolStation = JSON.parseObject(s1,UserRelVolStation.class);
		return userRelVolStation.getStationId();
	}

	/**
     * 反显记录
     * @param id
     * @return
     */
    @Override
    public BaseVo reflect(Long id,HttpServletRequest request) {

        VolunteerLevel volunteerLevel = levelAuditDao.reflect(id);
        Station byId = stationDao.findById(volunteerLevel.getStationId());
        if (byId!=null){
            volunteerLevel.setAuditStationName(byId.getStationName());
        }
        volunteerLevel.setTypelist(typeUtil.getType(volunteerLevel.getServiceType()));

        //获取appId
        Integer terminalId = Integer.parseInt(request.getHeader("tid"));
        List<ApplyLogs> logs = new ArrayList<>();
        //获取新增审核的日志
        List<ApplyLogs> applyLogs = applyRepository.queryApplyLogsList(TerminalTypeEnum.TERMINALTOVOLAPP.getTid(), BaseConst.AUDIT_LOG_TYPE_VOL_LEVEL, id);
        //获取审批审核的日志
        List<ApplyLogs> auditLogs = applyRepository.queryApplyLogsList(terminalId, BaseConst.AUDIT_LOG_TYPE_VOL_LEVEL, id);
        logs.addAll(applyLogs);
        logs.addAll(auditLogs);
        volunteerLevel.setApplyLogs(logs);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(volunteerLevel);
        return baseVo;
    }
    /**
     * 镇团委审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    @Override
    public BaseVo zlistPage(LevelAuditVO levelAuditVO) {

        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();
        BaseVo b1 = sysServiceFeign.queryByUserId(userId);
        if (b1==null){
            throwBusinessException("此人没有分配服务站");
        }

        levelAuditVO.setAuditStationId(vi.getStationId());
        PageHelper.startPage(levelAuditVO.getPageNum(),levelAuditVO.getPageSize());
        List<LevelAuditVolunteer> levelAuditVolunteerList = levelAuditDao.zlistPage(levelAuditVO);
        PageInfo<LevelAuditVolunteer> pageInfo = new PageInfo<LevelAuditVolunteer>(levelAuditVolunteerList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 服务站审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    @Override
    public BaseVo flistPage(LevelAuditVO levelAuditVO) {

        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();
        Long stationId = getStationIdByUserId(userId);

        levelAuditVO.setBelongStationId(stationId);
        PageHelper.startPage(levelAuditVO.getPageNum(),levelAuditVO.getPageSize());
        List<LevelAuditVolunteer> levelAuditVolunteerList = levelAuditDao.flistPage(levelAuditVO);

        PageInfo<LevelAuditVolunteer> pageInfo = new PageInfo<LevelAuditVolunteer>(levelAuditVolunteerList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * app查询自身星级
     * @return
     */
    @Override
    public BaseVo starlevel() {
        StarLevel starLevel = new StarLevel();
        Volunteer vi = volUtil.getVolunteerInfo();
        //判断当前用户能升到的最高级别


        LevelRegulation level1 = levelRegulationDao.queryByLevel(1);
        LevelRegulation level2 = levelRegulationDao.queryByLevel(2);
        LevelRegulation level3 = levelRegulationDao.queryByLevel(3);
        LevelRegulation level4 = levelRegulationDao.queryByLevel(4);
        LevelRegulation level5 = levelRegulationDao.queryByLevel(5);

        Volunteer byId = volunteerDao.findById(vi.getId());
        BigDecimal campaignHour = byId.getCampaignHour();
        BigDecimal donateMoney = byId.getDonateMoney();
        Long hour = campaignHour.longValue();
        Long money = donateMoney.longValue();
        if (byId!=null){
            Integer level = byId.getServiceLevel();
            if (level==null){
                level=0;
            }
            starLevel.setCurrentLevel(level);
            starLevel.setCurrentHour(campaignHour);
            starLevel.setCurrentMoney(donateMoney);
            starLevel.setNextLevel(level1.getServiceLevel());
            starLevel.setNeedHour(level1.getServiceTime());
            starLevel.setNeedMoney(level1.getDonationAmount());
            //等级0
            if (hour < level1.getServiceTime().longValue() || money < level1.getDonationAmount().longValue()) {
                starLevel.setNextLevel(level1.getServiceLevel());
                starLevel.setNeedHour(level1.getServiceTime());
                starLevel.setNeedMoney(level1.getDonationAmount());
            }
            if (hour >= level1.getServiceTime().longValue() || money >= level1.getDonationAmount().longValue()){
				//等级1
                starLevel.setNextLevel(level2.getServiceLevel());
                starLevel.setNeedHour(level2.getServiceTime());
                starLevel.setNeedMoney(level2.getDonationAmount());
                if (level2.getServiceLevel() - level >1){
                    starLevel.setNextLevel(level1.getServiceLevel());
                    starLevel.setNeedHour(level1.getServiceTime());
                    starLevel.setNeedMoney(level1.getDonationAmount());
                }
            }
            if (hour >= level2.getServiceTime().longValue() || money >= level2.getDonationAmount().longValue()){
				//等级2
                starLevel.setNextLevel(level3.getServiceLevel());
                starLevel.setNeedHour(level3.getServiceTime());
                starLevel.setNeedMoney(level3.getDonationAmount());
                if (level3.getServiceLevel() - level >1){
                    starLevel.setNextLevel(level2.getServiceLevel());
                    starLevel.setNeedHour(level2.getServiceTime());
                    starLevel.setNeedMoney(level2.getDonationAmount());
                }
            }
            if (hour >= level3.getServiceTime().longValue() || money >= level3.getDonationAmount().longValue()){
				//等级3
                starLevel.setNextLevel(level4.getServiceLevel());
                starLevel.setNeedHour(level4.getServiceTime());
                starLevel.setNeedMoney(level4.getDonationAmount());
                if (level4.getServiceLevel() - level >1){
                    starLevel.setNextLevel(level3.getServiceLevel());
                    starLevel.setNeedHour(level3.getServiceTime());
                    starLevel.setNeedMoney(level3.getDonationAmount());
                }
            }
            if (hour >= level4.getServiceTime().longValue() || money >= level4.getDonationAmount().longValue()){
				//等级4
                starLevel.setNextLevel(level5.getServiceLevel());
                starLevel.setNeedHour(level5.getServiceTime());
                starLevel.setNeedMoney(level5.getDonationAmount());
                if (level5.getServiceLevel() - level >1){
                    starLevel.setNextLevel(level4.getServiceLevel());
                    starLevel.setNeedHour(level4.getServiceTime());
                    starLevel.setNeedMoney(level4.getDonationAmount());
                }
            }
            if (hour >= level5.getServiceTime().longValue() || money >= level5.getDonationAmount().longValue()){
				//等级5
                starLevel.setNextLevel(level5.getServiceLevel());
                starLevel.setNeedHour(level5.getServiceTime());
                starLevel.setNeedMoney(level5.getDonationAmount());
            }
            //根据token 和 时间 得到最新一条数据的状态
            LevelAudit levelAudit = levelAuditDao.queryTheLastOne(vi.getId());
            if (levelAudit!=null){
                starLevel.setLastStatus(levelAudit.getAuditStatus());
            }

        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(starLevel);
        return baseVo;
    }
    /**
     * 审核记录
     * @param levelAudit
     * @return
     */
    @Override
    public BaseVo log(LevelAudit levelAudit) {

        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId= vi.getId();
        levelAudit.setVolunteerId(userId);
        List<LevelLog> levelLogs = levelAuditDao.queryLogByVolunteerId(levelAudit);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(levelLogs);
        return baseVo;
    }
}
