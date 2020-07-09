package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyBasicLivingAllowances;
import com.bit.module.cbo.bean.ResidentBasicLivingRoster;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 居民低保服务名单表
 * @author: liyang
 * @date: 2019-08-14
 **/
@Data
public class ResidentBasicLivingRosterVO extends BasePageVo{

    /**
     * id
     */
    private Long id;

    /**
     * '申请的主题'
     */
    @NotNull(message = "申请主体不能为空！")
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
    @NotNull(message = "申请时的证件类型不能为空！")
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
    @NotNull(message = "申请的类别ID不能为空！")
    private Long categoryId;

    /**
     * 申请的类别名称
     */
    private String categoryName;

    /**
     * 申请的类事项ID
     */
    @NotNull(message = "申请的类事项ID不能为空！")
    private Long serviceId;

    /**
     * 申请的事项名称
     */
    private String serviceName;

    /**
     * 社区id
     */
    private Long orgId;

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
     * 创建人ID
     */
    private Long createUserId;


    /**
     * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     */
    @NotNull(message = "数据来源类型不能为空！")
    private Integer dataType;

    /**
     * 关联的申请的id,如果是手动创建的人员名单，此项为空
     */
    private Long applyId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 居民低保信息
     */
    private ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances;

    /**
     * 经办人名称
     */
    private String operatorName;

    /**
     * 低保证号
     */
    private String allowancesCardNum;

    /**
     * 所属社区名称
     */
    private String orgName;

    /**
     * 是否有效，1：正常，0：已失效
     */
    private Integer status;

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
     * 有效查询开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryValidBeginTime;

    /**
     * 有效查询截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryValidEndTime;
    /**
     * 户主
     */
    private String houseHolderName;
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
