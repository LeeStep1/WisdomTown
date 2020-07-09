package com.bit.soft.sms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.core.utils.CacheUtil;

import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.bean.SmsRequest;
import com.bit.soft.sms.bean.SmsSendLog;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.soft.sms.service.SmsService;
import com.bit.soft.sms.utils.SmsSenderComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: 短信发送服务
 * @Author: liyujun
 * @Date: 2019-03-20
 **/
@Service
@Slf4j
public class SmsServiceImpl extends BaseService implements SmsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SmsSenderComponent smsSenderComponent;

    @Autowired
    private CacheUtil cacheUtil;


    @Autowired
    private SmsFeignClient smsFeignClient;


    /**
     * 每日短信发送上限
     */
    public final long DAY_SMS_LIMIT = 86400L;


    public final long EACH_SMS_INTERVAL = 90L;

    /**
     * 每条短信间隔
     */
    @Value("${sms.interval}")
    private String interval;

    /**
     * 每日最大发送次数
     */
    @Value("${sms.limit}")
    private String smsSendLimit;

    /**
     * @param smsRequest :
     * @return : void
     * @description: 内部调用的发送短信服务
     * @author liyujun
     * @date 2019-03-20
     */
    @Override
    public BaseVo sendSmsMessage(SmsRequest smsRequest) {


        BaseVo vo=new BaseVo();

        try {
            if (smsRequest.getPhoneNumbers().length == 1) {
                //单条发送
                smsSenderComponent.sendSmsSingle(smsRequest.getSmsAccountTemplateEnum(), smsRequest.getPhoneNumbers()[0], smsRequest.getParams());
            } else if (smsRequest.getPhoneNumbers().length > 1) {
                //多条发送
                smsSenderComponent.sendMultiSender(smsRequest.getSmsAccountTemplateEnum(), smsRequest.getPhoneNumbers(), smsRequest.getParams());

            }
            CompletableFuture.runAsync(()->{
                SmsSendLog smsSendLog=new SmsSendLog();
                smsSendLog.setTerminalId(smsRequest.getTerminalId());
                smsSendLog.setPhoneNumbers( smsRequest.getPhoneNumbers());
                smsSendLog.setSendTime(new Date());
               /* smsSendLog.setSendTime(DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss.getValue()));*/
                smsSendLog.setSmsType(smsRequest.getSmsAccountTemplateEnum().getSmsTemplateType());
                mongoTemplate.insert(smsSendLog);
            });
            vo.setCode(ResultCode.SUCCESS.getCode());
            vo.setMsg(ResultCode.SUCCESS.getInfo());
        }catch (Exception e){
            vo.setCode(ResultCode.WRONG.getCode());
            vo.setMsg(ResultCode.WRONG.getInfo());
        }

        return vo;
    }


    /**
     * @param smsRequest :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2020-05-21
     */
    @Override
    public BaseVo sendCheckSms(CheckSmsRequest smsRequest) {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BaseVo baseVo = new BaseVo();
        try {
            String temTotal = "";
            SmsAccountTemplateEnum smsAccountTemplateEnum =SmsAccountTemplateEnum.getSmsAccountTemplateEnum(smsRequest.getSmsTemplateId()) ;
            String totalKey = smsAccountTemplateEnum.getSmslimitKey(smsRequest.getMobileNumber(), String.valueOf(smsRequest.getTerminalId()));
            String smsSendTemplateKey = smsAccountTemplateEnum.getSmsSendTemplate(smsRequest.getMobileNumber(), String.valueOf(smsRequest.getTerminalId()));
            temTotal = (String) cacheUtil.get(totalKey);
            //每日累计发送数量
            int total = 0;
            if (StringUtils.isNotEmpty(temTotal)) {
                total = Integer.valueOf(temTotal);
            } else {
                temTotal = "0";
            }

            //如果大于等于当天的限制次数，直接返回
            if (total < Integer.valueOf(smsSendLimit)) {

                //获取验证码
                String authCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

                //数组具体的元素个数和模板中变量个数必须一致
                String[] params = {authCode, interval};
                //   SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(appid), appkey);

                // 发送短信
                try {
                    smsSenderComponent.sendSmsSingle(SmsAccountTemplateEnum.getSmsAccountTemplateEnum(smsRequest.getSmsTemplateId()), smsRequest.getMobileNumber(), params);
                    //日志 同步  todo 可考虑改变为异步
                    CompletableFuture.runAsync(()->{
                        SmsSendLog smsSendLog=new SmsSendLog();
                        smsSendLog.setTerminalId(smsRequest.getTerminalId());
                        smsSendLog.setPhoneNumbers( new String[]{smsRequest.getMobileNumber()});
                        smsSendLog.setSendTime(new Date());
                       /* smsSendLog.setSendTime(DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss.getValue()));*/
                        smsSendLog.setSmsType(smsRequest.getSmsTemplateId());
                        mongoTemplate.insert(smsSendLog);
                    });

                } catch (BusinessException e) {
                    baseVo.setMsg("短信发送失败");
                    baseVo.setCode(ResultCode.WRONG.getCode());
                    return baseVo;
                }

                //验证码返给前端 todo 应隐藏验证码
                baseVo.setData(authCode);
                baseVo.setCode(ResultCode.SUCCESS.getCode());
                Long remainingTime = getRemainingTime(totalKey);

                //存入redis总数 如果小于当天次数，则当天累计发送次数 + 1
                cacheUtil.set(totalKey, String.valueOf(total + 1), remainingTime);

                //存入redis验证码 设置 N 分钟失效
                cacheUtil.set(smsSendTemplateKey, authCode, 6000);//EACH_SMS_INTERVAL

                baseVo.setMsg("短信发送成功,您今天还能接收" + (Integer.valueOf(smsSendLimit) - Integer.valueOf(temTotal)) + "次验证码");

                return baseVo;
            } else {
                baseVo.setCode(ResultCode.WRONG.getCode());
                baseVo.setMsg("今日验证码已超过" + smsSendLimit + "次 明天再来吧！");
                return baseVo;
            }
        } catch (Exception e) {
            baseVo.setCode(ResultCode.WRONG.getCode());
            baseVo.setMsg("短信发送失败");
            //log.error("手机号为：" + smsRequest.getMobileNumber() + " ,短信模板 " + smsRequest.getTemplateId() + " 发送异常:{}", e.getMessage());
            return baseVo;
        }
    }

    /**
     * @param request :验证码存入redis的key值
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2020-05-21
     */
    @Override
    public BaseVo checkSmsCode(SmsCodeRequest request) {

        BaseVo vo=new BaseVo();

        SmsAccountTemplateEnum aaa= SmsAccountTemplateEnum.getSmsAccountTemplateEnum(request.getSmsTemplateId());
        if(BeanUtil.isEmpty(aaa)){
             throwBusinessException("无此短信模板模板");
        }
        String codeKey=aaa.getSmsSendTemplate(String.valueOf(request.getMobileNumber()),String.valueOf(request.getTerminalId()));
        Object vl=cacheUtil.get(codeKey);
        if(!BeanUtil.isEmpty(vl)){
            codeKey= String.valueOf(vl);
            if(request.getSmsCode().equals(codeKey)){
                vo.setMsg("验正码正确");
            }else {
                vo.setCode(ResultCode.WRONG.getCode());
                vo.setMsg("验正码错误");
            }

        }else{
            vo.setCode(ResultCode.WRONG.getCode());
            vo.setMsg("验正码错误");
        }
        return vo;
    }


    /**
     * 获取失效时间
     *
     * @param registerKey key
     * @return
     */
    private long getRemainingTime(String registerKey) {

        //获取redis 中的value是否存在
        Long expireTime = cacheUtil.getExpire(registerKey);
        if (expireTime > 0L) {
            return DAY_SMS_LIMIT - expireTime;
        } else {
            return DAY_SMS_LIMIT;
        }

    }


    @Override
    public BaseVo sendCheckSmsFeign(CheckSmsRequest smsRequest){

        com.bit.soft.sms.bean.CheckSmsRequest   ss=new com.bit.soft.sms.bean.CheckSmsRequest();
        ss.setMobileNumber("13920886136");
        ss.setTerminalId(2);
        ss.setSmsTemplateId(1);
        BaseVo aa=null;
        try {aa=smsFeignClient.sendSmsCode(ss);

        }catch (Exception e){

            e.printStackTrace();
        }

        return aa;
    }
}
