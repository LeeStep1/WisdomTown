package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.*;

import com.bit.common.enumerate.PartnerAuditEnum;
import com.bit.common.enumerate.StationTypeEnum;
import com.bit.module.applylogs.bean.ApplyLogs;
import com.bit.module.applylogs.repository.ApplyRepository;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.PartnerOrgDao;
import com.bit.module.vol.dao.StationDao;
import com.bit.module.vol.feign.FileServiceFeign;

import com.bit.module.vol.service.PartnerOrgService;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.vo.PartnerOrgVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.soft.sms.bean.SmsRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.utils.CodeUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.StringUtil;
import com.bit.utils.VolUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-19 13:31
 */
@Service("partnerOrgService")
@Slf4j
public class PartnerOrgServiceImpl extends BaseService implements PartnerOrgService {

    @Autowired
    private PartnerOrgDao partnerOrgDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private StationService stationService;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private VolUtil volUtil;
    @Autowired
    private CodeUtil codeUtil;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;

    @Autowired
    private SmsFeignClient smsFeignClient;

    /**
     * 添加共建单位审核记录
     *
     * @param partnerOrg
     * @return
     */
    @Override
    @Transactional
    public BaseVo add(PartnerOrg partnerOrg, HttpServletRequest request) {
        BaseVo baseVo = new BaseVo();
        /**判断共建单位名称是否重复，逻辑：判断申请表中判断待审核状态的，基础表中全校验部分启用和未启用**/
        Integer num = stationDao.countSameName(partnerOrg.getPartnerOrgName());

        Integer count = partnerOrgDao.countSamepartnerOrgName(partnerOrg.getPartnerOrgName());

        if (num > 0 || count > 0) {
            baseVo.setData(null);
            baseVo.setCode(ResultCode.WRONG.getCode());
            baseVo.setMsg("共建单位名称重复");
            return baseVo;
        }
        partnerOrg.setCreateTime(new Date());
        partnerOrg.setAuditState(PartnerAuditEnum.PARTENER_AUDIT_STATE_WAIT_VERIFY.getCode());
        partnerOrg.setVersion(0);
        partnerOrgDao.add(partnerOrg);
        // 发送mq消息
        Long topStation = stationDao.findTopStation();
        Long[] targetId = new Long[]{topStation};
        String[] params = new String[]{partnerOrg.getChargeMan()};

        MqSendMessage mqSendMessagTask = AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.PARTNER_ORG_APPLY, targetId, params, partnerOrg.getId(), 0, partnerOrg.getChargeMan(), new Date(), null);
        sendMqPushUtil.sendMqMessage(mqSendMessagTask);
        return baseVo;
    }

    /**
     * 更新共建单位
     *
     * @param partnerOrg
     * @return
     */
    @Override
    public BaseVo update(PartnerOrg partnerOrg, HttpServletRequest request) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId = vi.getId();
        //审批前验证审批状态
        PartnerOrg reflect = partnerOrgDao.reflect(partnerOrg.getId());
        if (reflect != null) {
            //如果传入审核参数 与 数据库中审核参数不同 则校验是否重复审批
            if (!partnerOrg.getAuditState().equals(reflect.getAuditState())) {
                if (reflect.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_PASSED.getCode()) ||
                        reflect.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_REJECTED.getCode())) {
                    return new BaseVo(ResultCode.ADUITS_ALREADY_HANDLE.getCode(), ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
                }
            }
        }
        //throwBusinessException("远程调用异常");
        //获取token
        String terminalId = request.getHeader("tid");
        String[] phonenumbers = new String[]{partnerOrg.getChargeManMobile()};
        //发送消息到手机
        sendMessageToMobile(partnerOrg, vi, userId, terminalId, phonenumbers);
        return new BaseVo();
    }

    /**
     * 发送消息到手机
     *
     * @param partnerOrg
     * @param vi
     * @param userId
     * @param terminalId
     * @param phoneNumbers:
     */
    @Transactional(rollbackFor=Exception.class)
    public void sendMessageToMobile(PartnerOrg partnerOrg, Volunteer vi, Long userId, String terminalId, String[] phoneNumbers) {
        // 如果审核失败 发送短信到手机号
        /*if (partnerOrg.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_REJECTED.getCode())) {
            sendFailMessage(partnerOrg, vi, userId, terminalId, phoneNumbers);
        } else if (partnerOrg.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_PASSED.getCode())) {
            // 如果审核成功 发送短信到手机号
            sendSuccessMessage(partnerOrg, vi, userId, terminalId, phoneNumbers);

        }*/
        //throwBusinessException("远程调用异常");
        com.bit.soft.sms.bean.SmsRequest smsRequestNew = new com.bit.soft.sms.bean.SmsRequest();
        smsRequestNew.setTerminalId(Long.valueOf(terminalId));
        //审批日志
        ApplyLogs applyLogs =null;
        //审核被拒绝
        if (partnerOrg.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_REJECTED.getCode())) {
            //短信消息组装
            smsRequestNew.setParams(new String[]{SmsContentConst.VOL_SMS_CONTENT_PARTNER_ORG, partnerOrg.getRejectReason()});
            smsRequestNew.setSmsTemplateId(SmsAccountTemplateEnum.VOL_COMPANION_APPLYREFUSE_GENERAL_MESSAGE.getSmsTemplateType());
             applyLogs = new ApplyLogs(Integer.parseInt(terminalId),
                    BaseConst.AUDIT_LOG_TYPE_VOL_PARTNER_ORG,
                    partnerOrg.getId(),
                    userId,
                    vi.getRealName(),
                    AuditMessageTemplate.OPERATION_NAME_PARTNER_ORG_AUDIT,
                    new Date(),
                    0,
                    DateUtil.format(new Date()),
                    AuditMessageTemplate.AUDIT_PARTNER_ORG_REJECTED_ZHENTUANWEI + partnerOrg.getRejectReason(), "");
        }else if(partnerOrg.getAuditState().equals(PartnerAuditEnum.PARTENER_AUDIT_STATE_PASSED.getCode())){
            smsRequestNew.setParams(new String[]{SmsContentConst.VOL_SMS_CONTENT_PARTNER_ORG});
            smsRequestNew.setSmsTemplateId(SmsAccountTemplateEnum.VOL_COMPANION_APPLYPASS_GENERAL_MESSAGE.getSmsTemplateType());
            Station stationparam = new Station();
            stationparam.setStationType(StationTypeEnum.STATION_TYPE_GONGJIAN.getCode());
            // 校验code是否重复
            String code = codeUtil.genStationCode(stationparam);
            Integer count = stationDao.countSameCode(code);
            if (count > 0) {
                throwBusinessException("共建单位编码重复");
            }

            Integer num = stationDao.countSameName(partnerOrg.getPartnerOrgName());
            if (num > 0) {
                throwBusinessException("共建单位名称重复");
            }
            Station station = new Station();
            station.setStationName(partnerOrg.getPartnerOrgName());
            station.setStationLocation(partnerOrg.getPartnerOrgAddress());
            station.setStationIntroduction(partnerOrg.getPartnerOrgIntroduction());

            station.setFirstChargeMan(partnerOrg.getChargeMan());
            station.setFirstChargeManMobile(partnerOrg.getChargeManMobile());
            station.setStationLeader(partnerOrg.getStationLeader());
            station.setStationLeaderMobile(partnerOrg.getStationLeaderMobile());
            station.setViceStationLeader(partnerOrg.getViceStationLeader());
            station.setViceStationLeaderMobile(partnerOrg.getViceStationLeaderMobile());

            station.setPartnerOrgType(Const.PARTENER_ORG_TYPE_YES);
            station.setStationCampaignCount(0);
            station.setStationCampaignHour(new BigDecimal(0));
            station.setStationDonateMoney(new BigDecimal(0));
            station.setStationType(StationTypeEnum.STATION_TYPE_GONGJIAN.getCode());
            Long topStation = stationDao.findTopStation();
            station.setUpid(topStation);
            //设置共建单位code
            station.setStationCode(code);
            stationService.add(station);
            applyLogs = new ApplyLogs(Integer.parseInt(terminalId),
                    BaseConst.AUDIT_LOG_TYPE_VOL_PARTNER_ORG,
                    partnerOrg.getId(),
                    userId,
                    vi.getRealName(),
                    AuditMessageTemplate.OPERATION_NAME_PARTNER_ORG_AUDIT,
                    new Date(),
                    0,
                    DateUtil.format(new Date()),
                    AuditMessageTemplate.AUDIT_PARTNER_ORG_PASSED_ZHENTUANWEI, "");
           /* partnerOrg.setUpdateTime(new Date());
            partnerOrg.setUpdateUserId(userId);
            Integer version = partnerOrg.getVersion();
            if (version == null) {
                version = 0;
            }
            partnerOrg.setVersion(version);
            partnerOrgDao.update(partnerOrg);*/
        }
        smsRequestNew.setPhoneNumbers(phoneNumbers);
        try {
            BaseVo feignDto = smsFeignClient.sendRegSms(smsRequestNew);
            if (feignDto.getCode() == ResultCode.WRONG.getCode()) {
                throwBusinessException("发送消息失败");
            }
        }catch (Exception e){
          log.info("远程调用异常"+e.getClass().getName());
          throw  e;
           // throwBusinessException("远程调用异常");
        }
        log.info("調用完成，開始更新DB");
        partnerOrg.setUpdateTime(new Date());
        partnerOrg.setUpdateUserId(userId);
        Integer version = partnerOrg.getVersion();
        if (version == null) {
            version = 0;
        }
        partnerOrg.setVersion(version);
        partnerOrgDao.update(partnerOrg);
        //审批拒绝
        applyRepository.addApplyLogs(applyLogs);
    }

    /**
     * 发送成功短信
     *
     * @param partnerOrg
     * @param vi
     * @param userId
     * @param terminalId
     * @param phoneNumbers
     */
    private void sendSuccessMessage(PartnerOrg partnerOrg, Volunteer vi, Long userId, String terminalId, String[] phoneNumbers) {
        Station stationparam = new Station();
        stationparam.setStationType(StationTypeEnum.STATION_TYPE_GONGJIAN.getCode());
        // 校验code是否重复
        String code = codeUtil.genStationCode(stationparam);
        Integer count = stationDao.countSameCode(code);
        if (count > 0) {
            throwBusinessException("共建单位编码重复");
        }

        Integer num = stationDao.countSameName(partnerOrg.getPartnerOrgName());
        if (num > 0) {
            throwBusinessException("共建单位名称重复");
        }

        partnerOrg.setUpdateTime(new Date());
        partnerOrg.setUpdateUserId(userId);
        Integer version = partnerOrg.getVersion();
        if (version == null) {
            version = 0;
        }
        partnerOrg.setVersion(version);
        partnerOrgDao.update(partnerOrg);

        /*发送短信*/
        com.bit.soft.sms.bean.SmsRequest smsRequestNew = new com.bit.soft.sms.bean.SmsRequest();
        smsRequestNew.setTerminalId(Long.valueOf(terminalId));
        smsRequestNew.setParams(new String[]{SmsContentConst.VOL_SMS_CONTENT_PARTNER_ORG});
        smsRequestNew.setSmsTemplateId(SmsAccountTemplateEnum.VOL_COMPANION_APPLYPASS_GENERAL_MESSAGE.getSmsTemplateType());
        smsRequestNew.setPhoneNumbers(phoneNumbers);
        BaseVo feignDto = smsFeignClient.sendRegSms(smsRequestNew);
        if (feignDto.getCode() == ResultCode.WRONG.getCode()) {
            throwBusinessException("发送消息超时");
        }
        // 如果审核成功 发送短信到手机号 同时添加一条记录到站点表中
        insertStation(partnerOrg, code);
        //添加日志
        addApplyLogs(partnerOrg, vi, userId, terminalId);
        //设置短信模板
       /* String[] param = new String[]{SmsContentConst.VOL_SMS_CONTENT_PARTNER_ORG};
        smsRequest.setParam(param);
        smsRequest.setTemplateCode(SmsTemplateCodeConst.VOL_SMS_TEMPLATE_PARTNER_ORG_PASS);*/
    }

    /**
     * 添加日志
     *
     * @param partnerOrg
     * @param vi
     * @param userId
     * @param terminalId
     */
   private void addApplyLogs(PartnerOrg partnerOrg, Volunteer vi, Long userId, String terminalId) {
        ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId),
                BaseConst.AUDIT_LOG_TYPE_VOL_PARTNER_ORG,
                partnerOrg.getId(),
                userId,
                vi.getRealName(),
                AuditMessageTemplate.OPERATION_NAME_PARTNER_ORG_AUDIT,
                new Date(),
                0,
                DateUtil.format(new Date()),
                AuditMessageTemplate.AUDIT_PARTNER_ORG_PASSED_ZHENTUANWEI, "");
        applyRepository.addApplyLogs(applyLogs);
    }

    /**
     * 插入新站点
     *
     * @param partnerOrg
     * @param code
     */
    private void insertStation(PartnerOrg partnerOrg, String code) {
        Station station = new Station();
        station.setStationName(partnerOrg.getPartnerOrgName());
        station.setStationLocation(partnerOrg.getPartnerOrgAddress());
        station.setStationIntroduction(partnerOrg.getPartnerOrgIntroduction());

        station.setFirstChargeMan(partnerOrg.getChargeMan());
        station.setFirstChargeManMobile(partnerOrg.getChargeManMobile());
        station.setStationLeader(partnerOrg.getStationLeader());
        station.setStationLeaderMobile(partnerOrg.getStationLeaderMobile());
        station.setViceStationLeader(partnerOrg.getViceStationLeader());
        station.setViceStationLeaderMobile(partnerOrg.getViceStationLeaderMobile());

        station.setPartnerOrgType(Const.PARTENER_ORG_TYPE_YES);
        station.setStationCampaignCount(0);
        station.setStationCampaignHour(new BigDecimal(0));
        station.setStationDonateMoney(new BigDecimal(0));
        station.setStationType(StationTypeEnum.STATION_TYPE_GONGJIAN.getCode());
        Long topStation = stationDao.findTopStation();
        station.setUpid(topStation);
        //设置共建单位code
        station.setStationCode(code);

        stationService.add(station);
    }

    /**
     * 发送失败短信
     *
     * @param partnerOrg
     * @param vi
     * @param userId
     * @param terminalId
     * @param
     */
    private void sendFailMessage(PartnerOrg partnerOrg, Volunteer vi, Long userId, String terminalId, String[] phoneNumber) {
        com.bit.soft.sms.bean.SmsRequest smsRequestNew = new com.bit.soft.sms.bean.SmsRequest();
        smsRequestNew.setTerminalId(Long.valueOf(terminalId));
        smsRequestNew.setParams(new String[]{SmsContentConst.VOL_SMS_CONTENT_PARTNER_ORG, partnerOrg.getRejectReason()});
        smsRequestNew.setSmsTemplateId(SmsAccountTemplateEnum.VOL_COMPANION_APPLYREFUSE_GENERAL_MESSAGE.getSmsTemplateType());
        smsRequestNew.setPhoneNumbers(phoneNumber);
        BaseVo feignDto = smsFeignClient.sendRegSms(smsRequestNew);
        if (feignDto.getCode() == ResultCode.WRONG.getCode()) {
            throwBusinessException("发送消息失败");
        }
        ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId),
                BaseConst.AUDIT_LOG_TYPE_VOL_PARTNER_ORG,
                partnerOrg.getId(),
                userId,
                vi.getRealName(),
                AuditMessageTemplate.OPERATION_NAME_PARTNER_ORG_AUDIT,
                new Date(),
                0,
                DateUtil.format(new Date()),
                AuditMessageTemplate.AUDIT_PARTNER_ORG_REJECTED_ZHENTUANWEI + partnerOrg.getRejectReason(), "");
        applyRepository.addApplyLogs(applyLogs);
        partnerOrg.setUpdateTime(new Date());
        partnerOrg.setUpdateUserId(userId);
        Integer version = partnerOrg.getVersion();
        if (version == null) {
            version = 0;
        }
        partnerOrg.setVersion(version);
        partnerOrgDao.update(partnerOrg);
    }


    /**
     * 反显记录
     *
     * @param id
     * @return
     */
    @Override
    public BaseVo reflectById(Long id) {
        PartnerOrg partnerOrg = partnerOrgDao.reflect(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(partnerOrg);
        return baseVo;
    }

    /**
     * 分页查询审核记录
     *
     * @param partnerOrgVO
     * @return
     */
    @Override
    public BaseVo listPage(PartnerOrgVO partnerOrgVO) {
        PageHelper.startPage(partnerOrgVO.getPageNum(), partnerOrgVO.getPageSize());
        List<PartnerOrg> partnerOrgs = partnerOrgDao.listPage(partnerOrgVO);
        PageInfo<PartnerOrg> pageInfo = new PageInfo<>(partnerOrgs);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 获取资料
     *
     * @param id
     * @return
     */
    @Override
    public BaseVo getdataById(Long id) {
        Station station = stationDao.findById(id);
        if (station == null) {
            throwBusinessException("站点不存在");
        }
        List<FileInfo> fileInfoList = new ArrayList<>();
        String informationFile = station.getInformationFile();
        if (StringUtil.isNotEmpty(informationFile)) {
            String[] str = informationFile.split(",");
            for (String s : str) {
                BaseVo b1 = fileServiceFeign.findById(Long.parseLong(s));
                String s1 = JSON.toJSONString(b1.getData());
                FileInfo fileInfo = JSON.parseObject(s1, FileInfo.class);
                if (fileInfo == null) {
                    throwBusinessException("文件不存在");
                }
                fileInfoList.add(fileInfo);
            }
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(fileInfoList);
        return baseVo;
    }
    @Override
    public BaseVo getTest(){
       /* try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        SmsRequest re = new SmsRequest();
        try {
            smsFeignClient.sendRegSms(re);
        }catch (Exception e){
            log.error("sendRegSms 报错",e);
            throw e;
        }


        return new BaseVo();
    }
}
