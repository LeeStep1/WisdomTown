package com.bit.module.vol.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User
 * @author generator
 */
@Data
public class User implements Serializable {


	//columns START

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
     * 注册时间
     */	
	private Date insertTime;
    /**
     * 修改时间
     */	
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
    /**
     * 临时字段--接受身份信息
     */
    private List<Identity> identities;
    /**
     * 临时字段--字典里面的应用集合
     */
    private List<Integer> apps;

    /**
     * dict application id
     */
    private Integer dictId;
	//columns END

    private List<Long> identityIds;
    private List<Long> pbOrgIds;
    private List<Integer> oaOrgIds;
    private List<Integer> appIds;
    /**
     * 用户与志愿者站点集合
     */
    private List<UserRelVolStation> userRelVolStationList;

}


