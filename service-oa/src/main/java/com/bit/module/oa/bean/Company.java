package com.bit.module.oa.bean;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Company
 * @author generator
 */
@Data
public class Company implements Serializable {

	//columns START

    /**
     * id
     */
	private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 图片
     */	
	private String photo;
    /**
     * 行业
     */	
	private String industry;
    /**
     * 服务性质
     */	
	private String nature;
    /**
     * 服务开始时间
     */	
	private Date serviceStartTime;
    /**
     * 服务结束时间
     */	
	private Date serviceEndTime;
    /**
     * 类型
     */	
	private String type;
    /**
     * 地址
     */	
	private String address;
    /**
     * 统一社会信用代码
     */	
	private String uniformSocialCreditCode;
    /**
     * 联系人
     */	
	private String contract;
    /**
     * 联系人电话
     */	
	private String contractPhone;
    /**
     * 法定代表人
     */	
	private String legalRepresentative;
    /**
     * 登记机关
     */	
	private String registerOffice;
    /**
     * 经办人，用户id
     */	
	private Long operatorId;
    /**
     * 经办人电话
     */	
	private String operatorMobile;
    /**
     * 经营状态 1开业 2存续 3停业 4清算
     */
    @Pattern(regexp = "^[1-4]*$")
	private Integer condition;
    /**
     * 状态 0停用 1启用
     */	
	private Integer status;

	//columns END

    public void checkAdd() {
        checkName();
    }

    public void checkUpdate() {
        checkId();
    }

    private Company checkId() {
        if (this.getId() == null) {
            throw new CheckException("三方公司ID不能为空");
        }
        return this;
    }

    private Company checkName() {
        if (StringUtils.isEmpty(this.getName())) {
            throw new CheckException("三方公司名称不能为空");
        }
        return this;
    }
}


