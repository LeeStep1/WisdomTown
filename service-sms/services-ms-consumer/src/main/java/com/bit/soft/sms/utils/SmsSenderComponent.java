package com.bit.soft.sms.utils;

import com.bit.base.exception.BusinessException;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 短信发送组件
 * @Author: liyujun
 * @Date: 2019-03-19
 **/
@Component
@Slf4j
public class SmsSenderComponent {



/*    SMSAPPID: 1400192261
    SMSAPPKEY: 2f82b1b20f5c5767f2fc1ef895fe499e
  #每条短信间隔
    INTERVAL: 1*/
    /**
     * 短信应用SDK AppID
     */
    @Value("${sms.SMSAPPID}")
    private int appid;

    /**
     * 短信应用SDK AppKey
     */
    @Value("${sms.SMSAPPKEY}")
    private String appkey;


    /**
     * 每日最大发送次数
     */
    @Value("${sms.INTERVAL}")
    private String sign;

    /**
     * 模板编码id
     */
    @Value("#{${sms.tempIdsmaps}}")
    private ConcurrentHashMap<String, String> idMap;


    /**
     * @param enume        :模板编码
     * @param phoneNumbers :手机号
     * @param params       :模板替换的参数
     * @return : void
     * @description:
     * @author liyujun
     * @date 2019-03-20
     */
    public SmsSingleSenderResult sendSmsSingle(SmsAccountTemplateEnum enume, String phoneNumbers, String[] params) {
        SmsSingleSenderResult result = null;
        try {
            int templateId = Integer.valueOf(idMap.get(enume.getSmsTemplateKey()));
            if (templateId != 0) {
                SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
                result = ssender.sendWithParam("86", phoneNumbers, templateId, params, "", "", "");
                if (result.result > 0) {
                    throw new BusinessException(result.errMsg, 1);
                } else {
                    log.info("发送成功");
                }

            } else {
                throw new BusinessException("无此模板", 1);
            }
        } catch (HTTPException e) {
            e.printStackTrace();
            throw  new BusinessException("发送失败", 1);
        } catch (IOException e) {
            e.printStackTrace();
            throw  new BusinessException("发送失败", 1);
        }
        return result;
    }

    /**
     * @param enume        :  模板编码
     * @param phoneNumbers : 手机号
     * @param params       : 模板替换的参数
     * @return : void
     * @description:
     * @author liyujun
     * @date 2019-03-20
     */
    public SmsMultiSenderResult sendMultiSender(SmsAccountTemplateEnum enume, String[] phoneNumbers, String... params) {
        SmsMultiSenderResult result = null;
        try {
            int templateId = Integer.valueOf(idMap.get(enume.getSmsTemplateKey()));
            if (templateId != 0) {
                SmsMultiSender msender = new SmsMultiSender(appid, appkey);
                result = msender.sendWithParam("86", phoneNumbers,
                        templateId, params, sign, "", "");
                if (result.result > 0) {
                    throw new BusinessException(result.errMsg, 1);
                }
            } else {
                throw new BusinessException("无此模板", 1);
            }
        } catch (HTTPException e) {
            e.printStackTrace();
            throw new BusinessException("短信发送失败", 1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("短信发送失败", 1);
        }
        return result;
    }

/*    private SmsTemplate getTemplateCode(String templateCode) {
        if (idMap.entrySet().size() == 0) {
            for (String id : tempIds) {
                String[] ids = id.split("_");
                int b = Integer.parseInt(ids[1]);
                int c = Integer.parseInt(ids[2]);
                idMap.put(ids[0], new SmsTemplate(ids[0], b, c));
            }
        }
        return idMap.get(templateCode);
    }*/


}
