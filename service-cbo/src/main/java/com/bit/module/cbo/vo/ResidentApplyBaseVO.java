package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.cbo.bean.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 居民办事申请基础信息表
 * @author: liyang
 * @date: 2019-08-09
 **/
@Data
public class ResidentApplyBaseVO extends BasePageVo{

    /**
     * ID
     */
    private Long id;

    /**
     * 主题
     */
    @NotNull(message = "主题不能为空！")
    private String title;

    /**
     * 居民ID
     */
    @NotNull(message = "居民的id不能为空")
    private Long residentId;

    /**
     * 居民的名称
     */
    @NotNull(message = "居民的名称不能为空")
    private String residentName;

    /**
     * 申请时的证件类型
     */
//    @NotNull(message = "证件类型不能为空")
    private Integer cardType;

    /**
     * 证件号
     */
    @NotNull(message = "证件号不能为空")
    private String cardNum;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String mobileNum;

    /**
     * 申请的类别ID，对应t_cbo_guide 表中type中为1 的数据的id
     */
    @NotNull(message = "申请类别不能为空")
    private Long categoryId;

    /**
     * 申请的类别名称
     */
    private String categoryName;

    /**
     * 申请的类事项ID，对应t_cbo_guide 表中type中为0 的数据的id
     */
    @NotNull(message = "申请事项不能为空")
    private Long serviceId;

    /**
     * 申请的类事项名称
     */
    private String serviceName;

    /**
     * 申请类型名称
     */
    private String guideName;

    /**
     * 申请状态：1进行中 2,待完善，3已办结，4 已终止
     */
    private Integer applyStatus;

    /**
     * 申请日期
     */
    @NotNull(message = "申请日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date applyTime;



    /**
     * 查询申请开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date applyBeginTime;

    /**
     * 查询申请结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date applyEndTime;

    /**
     * 社区id
     */
    private Long orgId;

    /**
     * 是否有业务扩展信息:1有，0无
     */
    private Integer extend;

    /**
     * 扩展信息的相关业务，对应t_cbo_apply_guide中的extend_type
     */
    private Integer extendType;

    /**
     * 是否生成人员名单：1 :已经生成，0：未生成
     */
    private Integer generateRoster;

    /**
     * 经办人名称
     */
    private String operatorName;



    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 所属事项明细集合
     */
    private List<ResidentApplyProgressVO> residentApplyProgressVOList;

    /**
     * 补充业务信息
     */
    //todo 优化的代码，先不提交
    private ExtendTypeBase extendTypeBase;

    /**
     * 补充业务信息---低保业务信息
     */
    private ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances;

    /**
     * 补充业务信息---居家养老
     */
    private ResidentApplyHomeCare residentApplyHomeCare;
    /**
     * 补充业务信息---残疾人
     */
    private ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals;
    /**
     * 补充业务信息---特殊扶助
     */
    private ResidentApplySpecialSupport residentApplySpecialSupport;
    /**
     * 附件ids
     */
    private String attachedIds;
	/**
	 * 附件集合
	 */
	private List<FileInfo> fileInfos;
}
