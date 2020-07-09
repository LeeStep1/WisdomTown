package com.bit.module.system.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.common.SysEnum.SmsAccountTemplateEnum;
import com.bit.module.system.bean.SendSms;
import com.bit.module.system.dao.UserDao;
import com.bit.module.system.service.SendMessageService;
import com.bit.module.system.vo.UserVO;
import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 发送短信
 * @author Liy
 * @date 2019-03-12
 */
@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    @Value("#{${sms.tempIdsmaps}}")
    private Map<String,String> tempIdsmaps;

    @Autowired
    public UserDao userDao;
    @Autowired
	private SmsUtil smsUtil;

    /**
     *  短信发送调用端
     */
    @Autowired
    private SmsFeignClient smsFeignClient;

    /**
     * 发送注册用户验证码
     * @param sendSms 发送信息
     * @return
     */
    @Override
    public BaseVo SendRegSms(SendSms sendSms) {

	/*	sendSms.setOperationFlg(SmsAccountTemplateEnum.SMS_ACCOUNT_REGISTER.getSmsTemplateType());

		sendSms.setTemplateId(tempIdsmaps.get(SmsAccountTemplateEnum.SMS_ACCOUNT_REGISTER.getSmsTemplateKey()));
		//发送验证码短信
        BaseVo baseVo = smsUtil.sendSMSByTemplate(sendSms);*/

        CheckSmsRequest checkSmsRequest=new CheckSmsRequest();
        checkSmsRequest.setTerminalId(Long.valueOf(sendSms.terminalId));
        checkSmsRequest.setSmsTemplateId(SmsAccountTemplateEnum.SMS_ACCOUNT_REGISTER.getSmsTemplateType());
        checkSmsRequest.setMobileNumber(sendSms.mobileNumber);
        BaseVo vo=smsFeignClient.sendSmsCode(checkSmsRequest);
        if(vo.getCode()==ResultCode.WRONG.getCode()){
            log.info("远程调用失败");
            new BusinessException("请求失败");

        }
        return new BaseVo();
    }

    /**
     * 发送密码找回验证码
     * @param sendSms 发送信息
     * @return
     */
    @Override
    public BaseVo SendRetrievePwdSms(SendSms sendSms) {
		/*sendSms.setOperationFlg(SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType());

		sendSms.setTemplateId(tempIdsmaps.get(SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateKey()));
        //设置发送短信类型
		//sendSms.setOperationFlg(SysConst.SMS_FOR_RETRIVE);

		//设置短信模板
		//sendSms.setTemplateId(tempForPassBack);
		//先验证平台是否存在手机号，不存在直接返回
        UserVO userVO = new UserVO();
        userVO.setMobile(sendSms.getMobileNumber());
        int count = userDao.checkMobile(userVO);
        if(count > 0){
            //发送验证码短信
            BaseVo baseVo = smsUtil.sendSMSByTemplate(sendSms);
            return baseVo;
        }else {
            throw new BusinessException("手机号不存在",ResultCode.MOBILE_NOT_EXIST.getCode());
        }*/
        /*修改后的调用方式*/
        CheckSmsRequest checkSmsRequest=new CheckSmsRequest();
        checkSmsRequest.setSmsTemplateId(SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType());
        checkSmsRequest.setMobileNumber(sendSms.getMobileNumber());
        UserVO userVO = new UserVO();
        userVO.setMobile(sendSms.getMobileNumber());
        int count = userDao.checkMobile(userVO);
        if(count > 0){
            //发送验证码短信
            BaseVo  baseVo=smsFeignClient.sendSmsCode(checkSmsRequest);
            if(baseVo.getCode()==ResultCode.WRONG.getCode()){
                log.info("远程调用失败");
                new BusinessException("请求失败");
            }
            return baseVo;
        }else {
            throw new BusinessException("手机号不存在",ResultCode.MOBILE_NOT_EXIST.getCode());
        }


    }
    /**
     * 社区app 登录发送验证码
     * @param sendSms
     * @return
     */
    @Override
    public BaseVo sendCboAppSMS(SendSms sendSms) {
		/*SmsAccountTemplateEnum smsAccountTemplateEnum=SmsAccountTemplateEnum.getSmsAccountTemplateEnum(sendSms.getOperationFlg());
        String smsTemKey=smsAccountTemplateEnum.getSmsTemplateKey();
		if(tempIdsmaps.containsKey(smsTemKey)){
			sendSms.setTemplateId(tempIdsmaps.get(smsTemKey));
		}else{
			log.error("无匹配的模板号{}",smsAccountTemplateEnum.getSmsTemplateKey());
			throw new BusinessException("无匹配的模板号");
		}

		//发送验证码短信
		return smsUtil.sendSMSByTemplate(sendSms);*/
		/*修改后*/
        CheckSmsRequest checkSmsRequest=new CheckSmsRequest();
        checkSmsRequest.setSmsTemplateId(sendSms.getOperationFlg());
        checkSmsRequest.setMobileNumber(sendSms.getMobileNumber());
        checkSmsRequest.setTerminalId(Long.valueOf(sendSms.terminalId));
        BaseVo  baseVo=smsFeignClient.sendSmsCode(checkSmsRequest);
        if(baseVo.getCode()==ResultCode.WRONG.getCode()){
            log.info("远程调用失败");
            new BusinessException("请求失败");
        }
        return baseVo;
    }
}
