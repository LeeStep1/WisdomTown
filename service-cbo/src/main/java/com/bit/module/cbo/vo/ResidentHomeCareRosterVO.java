package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyHomeCare;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 居家养老服务名单表
 * @author: liyang
 * @date: 2019-08-14
 **/
@Data
public class ResidentHomeCareRosterVO extends BasePageVo {

    /**
     * ID
     */
    private Long id;

    /**
     * 申请的主题
     */
    @NotNull(message = "申请的主题不能为空！")
    private String title;

    /**
     * 居民的id
     */
    @NotNull(message = "居民的id不能为空！")
    private Long residentId;

    /**
     * 居民的名称
     */
    @NotNull(message = "居民的名称不能为空！")
    private String residentName;

    /**
     * 申请时的证件类型
     */
    @NotNull(message = "证件类型不能为空！")
    private Integer cardType;

    /**
     * 证件号
     */
    @NotNull(message = "证件号不能为空！")
    private String cardNum;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空！")
    private String mobileNum;

    /**
     * 申请的类别ID
     */
    @NotNull(message = "类别ID不能为空！")
    private Long categoryId;

    /**
     * 申请的类别名称
     */
    private String categoryName;

    /**
     * 申请的事项名称
     */
    private String serviceName;

    /**
     * 申请的事项ID
     */
    @NotNull(message = "事项ID不能为空！")
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
     * '数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单'
     */
    @NotNull(message = "数据来源不能为空")
    private Integer dataType;

    /**
     * 关联的申请的id,如果是手动创建的人员名单，此项为空
     */
    private Long applyId;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * '更新时间'
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 经办人名称
     */
    private String operatorName;

    /**
     * 居民养老信息业务
     */
    private ResidentApplyHomeCare residentApplyHomeCare;

    /**
     * 查询申请开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyBeginTime;

    /**
     * 查询申请截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyEndTime;

    /**
     * 户口类别
     */
    private String residenceType;

    /**
     * 待遇类别
     */
    private String treatmentType;

    /**
     * 评估等级
     */
    private String level;
	/**
	 * 状态 是否有效，1：正常，0：已失效
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
}
