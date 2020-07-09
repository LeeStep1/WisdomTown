package com.bit.module.syslog.bean;

import lombok.Data;

/**
 * @Description:  模板与接入端配置对应表
 * 对应的数据库 t_sys_messages_template_rel_tid
 * @Author: liyujun
 * @Date: 2019-08-30
 **/
@Data
public class MessageTemplateRelTid {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 模板id
     */
    private Long templateId;
    /**
     * 接入端id
     */
    private String tid;
    /**
     * 应用id
     */
    private String appid;
    /**
     * 是否持久化
     */
    private  Integer store;

}
