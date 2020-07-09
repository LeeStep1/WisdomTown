package com.bit.soft.sms.service;

import com.bit.base.vo.BaseVo;
import com.bit.soft.sms.bean.SmsLogPage;
import com.bit.soft.sms.bean.SmsSendLog;
import org.bson.types.ObjectId;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-05-21
 **/
public interface SmsLogService {

    /**
     * @param id :
     * @return : com.bit.base.vo.BaseVo<com.bit.sms.bean.SmsSendLog>
     * @description:
     * @author liyujun
     * @date 2020-05-25
     */
    BaseVo<SmsSendLog> findId(ObjectId id);

    /**
     * @return : com.bit.base.vo.BaseVo<com.bit.sms.bean.SmsSendLog>
     * @description:  发送短信日志查询
     * @author liyujun
     * @date 2020-05-26
     */
    BaseVo<SmsSendLog> smsSendLogPage(SmsLogPage smsLogPage);
}
