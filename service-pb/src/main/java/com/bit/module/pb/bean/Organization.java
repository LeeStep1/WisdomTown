package com.bit.module.pb.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Organization
 * @author generator
 */
@Data
public class Organization implements Serializable {

	//columns START

    /**
     * id
     */	
	private String id;
    /**
     * 代码
     */
    private String pCode;
    /**
     * 名称
     */	
	private String name;
    /**
     * 组织类型
     */
	private Integer orgType;
    /**
     * 组织描述
     */
	private String orgDesc;
    /**
     * 顺序
     */	
	private Integer sort;
    /**
     * 是否审批机关，0否 1是
     */	
	private Integer isApprovalAuz;
    /**
     * 是否已删除，0否 1是
     */	
	private Integer status;

	//columns END

    /**增加字段**/
    /**
     * 创建时间
     */
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Organization that = (Organization) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pCode, that.pCode) &&
                Objects.equals(name, that.name) &&
                Objects.equals(orgType, that.orgType) &&
                Objects.equals(orgDesc, that.orgDesc) &&
                Objects.equals(sort, that.sort) &&
                Objects.equals(isApprovalAuz, that.isApprovalAuz) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, pCode, name, orgType, orgDesc, sort, isApprovalAuz, status, createTime);
    }
}


