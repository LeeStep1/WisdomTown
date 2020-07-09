package com.bit.soft.configserver.service;

import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.ConfigProperties;
import org.springframework.stereotype.Repository;

/**
 * 设置config属性service
 * @author Liy
 */
@Repository
public interface ConfigPropertiesService {

    /**
     * 增加设置
     * @param configProperties
     * @return
     */
    BaseVo insertProperties(ConfigProperties configProperties);

    /**
     * 删除设置
     * @param id
     * @return
     */
    BaseVo delProperties(int id);

    /**
     * 修改设置
     * @param configProperties
     * @return
     */
    BaseVo updateProperties(ConfigProperties configProperties);

    /**
     * 查看设置
     * @return
     */
    BaseVo selectProperties();

}
