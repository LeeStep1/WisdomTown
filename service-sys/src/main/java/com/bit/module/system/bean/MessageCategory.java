package com.bit.module.system.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 消息对应的类目，数据库：t_sys_message_category
 * @Author: liyujun
 * @Date: 2020-04-08
 **/
@Data
public class MessageCategory implements Serializable{


    /**
     * id
     */
    private Long id;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 类目名称
     */
    private String categoryName;

}
