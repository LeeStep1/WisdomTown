package com.bit.module.vol.bean;

import lombok.Data;

/**
 * @author chenduo
 * @create 2019-03-22 13:11
 */
@Data
public class UserRelVolStation {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 志愿者站点id
     */
    private Long stationId;
    /**
     * 站点名称
     */
    private String stationName;
}
