package com.bit.ms.service;

import com.bit.ms.bean.Jpush;

/**
 * @Description:
 * @Author: mifei
 * @Date: 2019-08-29
 **/
public interface JpushSettingService {

    Jpush findByTid(Integer tid);
}
