package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.module.cbo.bean.ExtendTypeBase;
import org.springframework.stereotype.Component;


/**
 * @description: 扩展信息处理模板类
 * @author: liyang
 * @date: 2020-06-09
 **/
@Component
public abstract class AbstractExtendType {

    /**
     * 根据台账 ID获取 相应的 扩展信息
     * @param id
     * @return
     */
    public abstract ExtendTypeBase getExtendTypeBase(Long id);

    /**
     * 补充业务信息
     * @param extendTypeBase 具体业务信息的bean
     * @param userInfo 操作人详情
     * @param applyId 申请ID
     * @throws Exception
     */
    public abstract void addExtendInfo(ExtendTypeBase extendTypeBase, UserInfo userInfo, Long applyId) throws Exception;

}
