package com.bit.module.manager.vo;

import com.bit.base.vo.BasePageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: CMS 用户表
 * @author: liyang
 * @date: 2019-06-11
 **/
@Data
public class PortalUserVo extends BasePageVo{

    /**
     * id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户的实际名称
     */
    private String realName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 增加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date insertTime ;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /**
     * 0已删除 1可用
     */
    private Integer status;
    /**
     * 创建账号类型
     */
    private Integer createType;

    /**
     * 身份证号
     */
    private String idcard;
}
