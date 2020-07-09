package com.bit.ms.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 极光推送配置类
 * @author mifei
 * @create 2019-08-09
 */
@Data
public class Jpush implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private Integer tid;
    /**
     * 标题
     */
    private String appKey;

    /**
     * 类目
     */
    private String masterSecret;



}
