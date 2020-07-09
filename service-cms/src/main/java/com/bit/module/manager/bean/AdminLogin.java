package com.bit.module.manager.bean;

import lombok.Data;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:39
 */
@Data
public class AdminLogin {

    private String userName;

    private String passWord;
    /**
     * 接入端
     */
    private Integer terminalId;
}
