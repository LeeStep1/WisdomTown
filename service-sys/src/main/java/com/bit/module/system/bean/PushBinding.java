package com.bit.module.system.bean;

import lombok.Data;

/**
 * 推送绑定设备表
 * @author liy
 */
@Data
public class PushBinding {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 接入端ID
     */
    private Integer terminalId;

    /**
     * 设备唯一标识
     */
    private String registrationId;
}
