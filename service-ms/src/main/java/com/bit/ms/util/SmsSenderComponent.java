package com.bit.ms.util;

import com.bit.base.exception.BusinessException;
import com.bit.ms.bean.SmsTemplate;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
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
public class SmsSenderComponent {


    /**
     * 短信应用SDK AppID
     */
    @Value("${sms.appid}")
    private   int appid;

    /**
     * 短信应用SDK AppKey
     */
    @Value("${sms.appkey}")
    private   String appkey;

    /**
     * 每条短信间隔
     */
    @Value("${sms.interval}")
    private  String interval;

    /**
     * 每日最大发送次数
     */
    @Value("${sms.limit}")
    private  String smsSendLimit;

    /**
     * 每日最大发送次数
     */
    @Value("${sms.sign}")
    private  String sign;

    /**
     * 模板编码id
     */
    @Value("${sms.tempCodes}")
    private  String [] tempIds;

    /**缓存的的key(业务模板编码) ,value(短信模板id)**/
    private  ConcurrentHashMap<String,SmsTemplate> idMap=new  ConcurrentHashMap ();



     /**
      * @description:
      * @author liyujun
      * @date 2019-03-20
      * @param templateCode :模板编码
      * @param phoneNumbers :手机号
      * @param params :模板替换的参数
      * @return : void
      */
    public void  sendSmsSingle( String  templateCode , String phoneNumbers,String []params ){

        try {
            SmsTemplate a=getTemplateCode(templateCode);
            if(a!=null){
               if (a.getNum()==params.length){
                   SmsSingleSender ssender =new SmsSingleSender(appid, appkey);
                   SmsSingleSenderResult result =ssender.sendWithParam("86",phoneNumbers,a.getId(),params,"","","");
                   if(result.result>0){
                       throw new BusinessException(result.errMsg,1);
                   }
               }else{
                   throw new BusinessException("参数不足",1);
               }
            }else{
                throw new BusinessException("无此模板",1);
            }
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
     * @description:
     * @author liyujun
     * @date 2019-03-20
     * @param templateCode :  模板编码
     * @param phoneNumbers : 手机号
     * @param params : 模板替换的参数
     * @return : void
     */
    public void sendMultiSender( String templateCode , String[] phoneNumbers, String ...params ){

        try {

            SmsTemplate a=getTemplateCode(templateCode);
            if(a!=null){
                if (a.getNum()==params.length){
                    SmsMultiSender msender= new SmsMultiSender(appid, appkey);
                    SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
                            a.getId(), params, sign, "", "");
                    if(result.result>0){
                        throw new BusinessException(result.errMsg,1);
                    }
                }else{
                    throw new BusinessException("参数不足",1);
                }

            }else{
                throw new BusinessException("无此模板",1);
            }
        } catch (HTTPException e) {
            e.printStackTrace();
            throw new BusinessException("短信发送失败",1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("短信发送失败",1);
        }

    }

    private SmsTemplate getTemplateCode(String templateCode){
        if(idMap.entrySet().size()==0){
            for(String id :tempIds){
                String[] ids =id.split("_");
                int b=Integer.parseInt(ids[1]);
                int c=Integer.parseInt(ids[2]);
                idMap.put(ids[0],new SmsTemplate(ids[0],b,c));
            }
        }
        return  idMap.get(templateCode);
    }

}
