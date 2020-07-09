package com.bit.module.cbo.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
@Data
public class RepairApply implements Serializable {
    private static final long serialVersionUID = -2971904960087120878L;
	/**
	 * id
	 */
	private Long id;
	/**
	 * 申请人居民id
	 */
    private Long residentId;
	/**
	 * 居民民称
	 */
    private String residentName;
	/**
	 * 居民联系方式
	 */
    private String residentMobile;
	/**
	 * 社区ID
	 */
    private Long orgId;
	/**
	 * 社区名称
	 */
    private String orgName;
	/**
	 * 小区ID
	 */
    private Long communityId;
	/**
	 * 小区名称
	 */
    private String communityName;
	/**
	 * 故障类型 1-公共电梯故障 2-公共门禁故障 3-公共其他故障 4-住户安防故障 5-住户其他故障
	 */
	@NotNull(message = "故障类型不能为空")
    private Integer troubleType;
	/**
	 * 故障描述
	 */
	@NotEmpty(message = "故障描述不能为空")
	@Length(max = 1000,message = "问题描述不能超过500")
	private String troubleDesc;
	/**
	 * 故障地点
	 */
	@NotEmpty(message = "故障地点不能为空")
	@Length(max = 100,message = "故障地点不能超过50")
	private String troubleLocation;
	/**
	 * 居民申请时的数据状态:1：待受理,2：处理中，3：已完结，4：已拒绝 5：已取消
	 */
    private Integer applyStatus;
	/**
	 * 数据流转的的状态     物业：1：待受理, 2：处理中，3：已完结，4：已拒绝  居委会：5：待受理, 6：处理中，7：已完结，8：已拒绝  9：已取消
	 */
    private Integer dataStatus;
	/**
	 * 附件的id，逗号分隔
	 */
    private String attchedIds;
	/**
	 * 数据创建时间
	 */
    private Date createTime;
	/**
	 * 数据更新时间
	 */
    private Date updateTime;
	/**
	 * 数据更新操作人的id
	 */
    private Long updateUserId;
	/**
	 * 更新人的类型：居民，物业，居委会,登陆时的角色  1社区 2.物业 3居民 0社区办
	 */
    private String updateUserType;
	/**
	 * 处理人姓名
	 */
    private String handleUserName;
	/**
	 * 处理人电话
	 */
    private String handleUserMobile;
	/**
	 * 处理结果
	 */
    private String handleUserResult;
	/**
	 * 版本号
	 */
    private Integer version;


}