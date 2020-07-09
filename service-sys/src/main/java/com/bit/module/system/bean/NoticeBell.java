package com.bit.module.system.bean;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-15 15:57
 */
@Data
public class NoticeBell {

    private String mongoId;
    private Long businessId;
    private Integer msgType;
    private String msgTypeName;
    private String categoryCode;
    private String categoryName;
    private String content;
    private String tid;
    private Integer appid;
    private Long userId;
    private Integer status;
    private Date recTime;
    private String creater;
    private Integer version;
}
