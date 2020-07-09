package com.bit.ms.service.impl;

import com.bit.base.service.BaseService;
import com.bit.ms.bean.Jpush;
import com.bit.ms.dao.JpushDao;
import com.bit.ms.service.JpushSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description:  jpush相关 表操作
 * @Author: mifei
 * @Date: 2019-08-29
 **/
@Service
@CacheConfig(cacheNames = "jpush_setting")
public class JpushSettingSettingServiceImpl extends BaseService implements JpushSettingService {


    @Autowired
    private JpushDao jpushDao;

    @Override
    @Cacheable
    public Jpush findByTid(Integer tid) {
        return jpushDao.findByTid(tid);
    }
}
