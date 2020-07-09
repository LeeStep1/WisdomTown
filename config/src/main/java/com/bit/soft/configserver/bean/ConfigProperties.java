package com.bit.soft.configserver.bean;

import lombok.Data;

/**
 * config设置属性
 * @author Liy
 */
@Data
public class ConfigProperties {

    /**
     * 自增ID
     */
    private int id;

    /**
     *  属性键
     */
    private String key;

    /**
     * 属性值
     */
    private String value;

    /**
     * 项目名称
     */
    private String application;

    /**
     * 属性环境
     */
    private String profile;

    /**
     * 属性分支
     */
    private String label;
}
