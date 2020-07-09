package com.bit.utils;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.common.ResultCode;
import com.bit.common.SysEnum.SmsAccountTemplateEnum;
import com.bit.core.utils.CacheUtil;
import com.bit.module.system.bean.SendSms;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 短信发送工具类
 * @author Liy
 */
@Component
public class SmsUtil extends BaseService{

    protected static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    /**
     * 短信应用SDK AppID
     */
    @Value("${sms.smsappid}")
    private String appid;

    /**
     * 短信应用SDK AppKey
     */
    @Value("${sms.smsappkey}")
    private String appkey;

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
     * redis 工具类
     */
    @Autowired
    private CacheUtil cacheUtil;


    /**
     * 发送验证码
     * @param sendSms 短信详情
     */
    @Transactional
    public BaseVo sendSMSByTemplate(SendSms sendSms){
        try {
            String temTotal = "";
           //新增
            SmsAccountTemplateEnum  smsAccountTemplateEnum= SmsAccountTemplateEnum.getSmsAccountTemplateEnum(sendSms.getOperationFlg());
            String totalKey=smsAccountTemplateEnum.getSmslimitKey(sendSms.getMobileNumber(),sendSms.getTerminalId());
            String smsSendTemplateKey=smsAccountTemplateEnum.getSmsSendTemplate(sendSms.getMobileNumber(),sendSms.getTerminalId());
            temTotal = (String) cacheUtil.get(smsAccountTemplateEnum.getSmslimitKey(sendSms.getMobileNumber(),sendSms.getTerminalId()));

			//每日累计发送数量
            int total = 0;
            if(StringUtils.isNotEmpty(temTotal)){
                total = Integer.valueOf(temTotal);
            }else{
                temTotal = "0";
            }
            BaseVo baseVo = new BaseVo();
            //如果大于等于当天的限制次数，直接返回
            if(total < Integer.valueOf(smsSendLimit)){
                String authCode;

                //获取验证码
                authCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

                //数组具体的元素个数和模板中变量个数必须一致
                String[] params = {authCode,interval};
                SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(appid), appkey);

                // 发送短信
                SmsSingleSenderResult result = ssender.sendWithParam("86",sendSms.getMobileNumber(),
                        Integer.valueOf(sendSms.getTemplateId()), params, "", "", "");
                int returnCode = result.result;


                //验证码返给前端
                baseVo.setData(authCode);

                //如果发送成功存入redis
                if(returnCode == 0){

                    Long remainingTime = getRemainingTime(totalKey);

                    //存入redis总数 如果小于当天次数，则当天累计发送次数 + 1
                    cacheUtil.set( totalKey, String.valueOf(total + 1),remainingTime);

                    //存入redis验证码 设置 N 分钟失效
                    cacheUtil.set(smsSendTemplateKey,authCode,Long.parseLong(SysConst.EACH_SMS_INTERVAL));

					baseVo.setMsg("短信发送成功,您今天还能接收"+(Integer.valueOf(smsSendLimit)-Integer.valueOf(temTotal))+ "次验证码");
                }else {
                    baseVo.setMsg("短信发送失败");
                    baseVo.setCode(result.result);
                }
                return baseVo;
            }else {
                baseVo.setCode(ResultCode.WRONG.getCode());
                baseVo.setMsg("今日验证码已超过" + smsSendLimit + "次 明天再来吧！");
                return baseVo;
            }
        } catch (Exception e) {
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.WRONG.getCode());
			baseVo.setMsg("短信发送失败");
            //baseVo.setData(e.getMessage());
            logger.error("手机号为：" + sendSms.getMobileNumber() + " ,短信模板 " + sendSms.getTemplateId() + " 发送异常:{}",e.getMessage());
            return baseVo;
        }
    }

    /**
     * 获取失效时间
     * @param registerKey key
     * @return
     */
    public Long getRemainingTime(String registerKey) {

        //获取redis 中的value是否存在
        Long ExpireTime = cacheUtil.getExpire(registerKey);
        if(ExpireTime > 0L){
            Long newTime = Long.parseLong(SysConst.DAY_SMS_LIMIT) - ExpireTime;
            return newTime;
        }else {
            Long newTime = Long.parseLong(SysConst.DAY_SMS_LIMIT);
            return newTime;
        }

    }

}
