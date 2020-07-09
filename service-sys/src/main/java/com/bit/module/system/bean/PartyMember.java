package com.bit.module.system.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * PartyMember
 * @author generator
 */
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PartyMember implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 姓名
     */	
	private String name;
    /**
     * 照片
     */	
	private String photo;
    /**
     * 性别
     */	
	private String sex;
    /**
     * 出生日期
     */	
	private Date birthdate;
    /**
     * 身份证号码
     */	
	private String idCard;
    /**
     * 民族
     */	
	private String nation;
    /**
     * 入党时间
     */	
	private Date joinTime;
    /**
     * 学历
     */	
	private String education;
    /**
     * 党员类型，1正式 2预备
     */	
	private Integer memberType;
    /**
     * 户籍所在派出所
     */	
	private String policeStation;
    /**
     * 工作/学习单位
     */	
	private String company;
    /**
     * 联系电话
     */	
	private String mobile;
    /**
     * 现居住地
     */	
	private String address;
    /**
     * 组织id
     */	
	private String orgId;
    /**
     * 组织名称[关联获取]
     */
	private String orgName;
    /**
     * 插入时间
     */	
	private Date insertTime;
    /**
     * 状态，0待完善 1正常 2停用
     */	
	private Integer status;

	//columns END

}


