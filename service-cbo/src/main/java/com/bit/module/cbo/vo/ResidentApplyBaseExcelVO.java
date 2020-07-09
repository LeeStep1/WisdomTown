package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.base.vo.BasePageVo;
import com.bit.module.cbo.bean.ResidentApplyBasicLivingAllowances;
import com.bit.module.cbo.bean.ResidentApplyDisabledIndividuals;
import com.bit.module.cbo.bean.ResidentApplyHomeCare;
import com.bit.module.cbo.bean.ResidentApplySpecialSupport;
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
public class ResidentApplyBaseExcelVO extends BasePageVo{

    /**
     * ID
     */
    private Long id;

    /**
     * 序号
     */
    @Excel(name = "序号", width = 8, orderNum = "1")
    private Long orderId;

    /**
     * 申请类型名称
     */
    @Excel(name = "办事类型", width = 16, orderNum = "2")
    private String guideName;

    /**
     * 居民的名称
     */
    @Excel(name = "申请人", width = 12, orderNum = "3")
    private String residentName;

    /**
     * 证件类型名称
     */
    @Excel(name = "证件类型", width = 12, orderNum = "4")
    private String cardTypeName;

    /**
     * 证件号
     */
    @Excel(name = "证件号", width = 24, orderNum = "5")
    private String cardNum;

    /**
     * 手机号
     */
    @Excel(name = "手机号码", width = 12, orderNum = "6")
    private String mobileNum;

    /**
     * 申请日期
     */
    @Excel(name = "申请时间", width = 12, orderNum = "7")
    private String applyTime;

    /**
     * 经办人名称
     */
    @Excel(name = "经办人", width = 12, orderNum = "8")
    private String operatorName;

    /**
     * 申请状态：1进行中 2,待完善，3已办结，4 已终止
     */
    @Excel(name = "状态", width = 12, orderNum = "9")
    private String applyStatus;

    /**
     * 社区名称
     */
    @Excel(name = "社区名称", width = 12, orderNum = "10")
    private String orgName;

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
     * 低保业务信息
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
