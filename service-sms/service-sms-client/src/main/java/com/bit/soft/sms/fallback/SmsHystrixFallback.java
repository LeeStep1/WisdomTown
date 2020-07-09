package com.bit.soft.sms.fallback;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.soft.sms.bean.CheckSmsRequest;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.bean.SmsRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.stereotype.Component;

/**
 * @Description:  熔断处理
 * @Author: liyujun
 * @Date: 2020-06-04
 **/
@Component
public class SmsHystrixFallback  implements SmsFeignClient {


    @Override
    public BaseVo sendSmsCode(CheckSmsRequest checkSmsRequest) {
        return fallbackTimeOut();
    }

    @Override
    public BaseVo sendRegSms(SmsRequest smsRequest) {
        return fallbackTimeOut();
    }

    @Override
    public BaseVo checkCode(SmsCodeRequest smsCodeRequest) {
        return fallbackTimeOut();
    }


    private BaseVo fallbackTimeOut(){
        BaseVo vo=new BaseVo();
        vo.setCode(ResultCode.HYSTRIX_TIME_OUT.getCode());
        vo.setMsg(ResultCode.HYSTRIX_TIME_OUT.getInfo());
        throw new BusinessException(ResultCode.HYSTRIX_TIME_OUT.getInfo());

    }
}
