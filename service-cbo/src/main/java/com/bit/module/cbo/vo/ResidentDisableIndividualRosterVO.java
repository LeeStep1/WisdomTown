package com.bit.module.cbo.vo;

import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyDisabledIndividuals;
import com.bit.module.cbo.bean.ResidentDisableIndividualRoster;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ResidentDisableIndividualRosterVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 申请的主题
	 */
    private String title;
	/**
	 * 居民的id
	 */
    private Long residentId;
	/**
	 * 居民的名称
	 */
    private String residentName;
	/**
	 * 申请时的证件类型
	 */
    private Integer cardType;
	/**
	 * 证件号
	 */
    private String cardNum;
	/**
	 * 手机号
	 */
    private String mobileNum;
	/**
	 * 申请的类别ID
	 */
    private Long categoryId;
	/**
	 * 申请的类事项ID
	 */
    private Long serviceId;
	/**
	 * 社区id
	 */
    private Long orgId;
	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 申请记录申请的日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date applyTime;
	/**
	 * 数据创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
	/**
	 * 创建人id
	 */
    private Long createUserId;
	/**
	 * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
	 */
    private Integer dataType;
	/**
	 * 经办人名称
	 */
    private String operatorName;
	/**
	 * 关联的申请的id
	 */
    private Long applyId;
	/**
	 * 更新时间
	 */
    private Date updateTime;
	/**
	 * 更新人id
	 */
    private Long updateUserId;
	/**
	 * 是否有效，1：正常，0：已失效
	 */
    private Integer status;
	/**
	 * 注销备注
	 */
    private String cancellationRemarks;
	/**
	 * 附件id
	 */
    private String attachedIds;
	/**
	 * 残疾类别
	 */
    private String disabilityCategory;
	/**
	 * 残疾等级：1：一级 2：“二级” 3：“三级” 4：“四级'
	 */
    private String disabilityLevel;
	/**
	 * 是否低保用户：1：是低保  0：不是低保
	 */
    private Integer livingAble;
	/**
	 * 残疾人业务
	 */
	private ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 服务名称
	 */
	private String serviceName;
	/**
	 * 修改记录
	 */
	private List<ResidentApplyLog> logs;

	/**
	 * 附件集合
	 */
	private List<FileInfo> fileInfos;
	/**
	 * 判断服务名单编辑的时候扩展信息有没有更改 0- 未更改 1-已更改
	 */
	private Integer flag;
	/**
	 * 低保证日期
	 */
	private Date livingIssuanceDate;

	/**
	 * 查询开始时间
	 */
	private String queryApplyBeginTime;

	/**
	 * 查询结束时间
	 */
	private String queryApplyEndTime;
}