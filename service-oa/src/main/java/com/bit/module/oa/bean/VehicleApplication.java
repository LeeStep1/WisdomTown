package com.bit.module.oa.bean;

import com.bit.base.exception.CheckException;
import com.bit.module.oa.enums.VehicleApplicationUsageEnum;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * VehicleApplication
 * @author generator
 */
@Data
public class VehicleApplication implements Serializable {

	//columns START

    /**
     * id
     */
	private Long id;
    /**
     * 申请单号
     */	
	private String applyNo;
    /**
     * 申请人，用户id
     */
	private Long userId;
    /**
     * 申请科室id
     */
	private Long orgId;
    /**
     * 申请科室名称
     */
	private String orgName;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
	private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
	private Integer usage;
    /**
     * 乘车人数
     */
	private Integer passengerNum;
    /**
     * 始发地
     */
	private String origin;
    /**
     * 目的地
     */
	private String terminal;
    /**
     * 计划开始时间
     */
	private Date planStartTime;
    /**
     * 计划结束时间
     */
	private Date planEndTime;
    /**
     * 用车事由
     */
	private String applyReason;
    /**
     * 申请时间
     */	
	private Date applyTime;
    /**
     * 派车人，用户id
     */	
	private Long assignerId;
    /**
     * 派车时间
     */	
	private Date dispatchTime;
    /**
     * 开始时间
     */	
	private Date startTime;
    /**
     * 结束时间
     */	
	private Date endTime;
    /**
     * 企业id
     */	
	private Long companyId;
    /**
     * 车牌号列表，英文逗号分隔
     */	
	private String plateNos;
    /**
     * 驾驶员列表，英文逗号分隔，每项的格式为：[驾驶员名称]([驾驶员联系电话])
     */	
	private String drivers;
    /**
     * 备注
     */	
	private String remark;
    /**
     * 拒绝原因
     */	
	private String rejectReason;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */	
	private Integer status;

    /**
     * 版本号
     */
    private Integer version;

	//columns END

    public void checkQuery() {
        checkId();
    }

    public void checkRejectParam() {
        checkId().checkRejectReason();
    }

    private VehicleApplication checkId() {
        if (this.getId() == null) {
            throw new CheckException("用车单ID不能为空");
        }
        return this;
    }

    private VehicleApplication checkRejectReason() {
        if (StringUtils.isEmpty(this.getRejectReason())) {
            throw new CheckException("拒绝理由不能为空");
        }
        return this;
    }

    public void check() {
        checkApplyReason().checkPlanTime().checkNature().checkPassengerNum().checkOrigin().checkTerminal().checkUsage();
    }

    private VehicleApplication checkNature() {
        if (this.getNature() == null) {
            throw new CheckException("用车性质不能为空");
        }
        return this;
    }

    private VehicleApplication checkUsage() {
        if (this.getUsage() == null) {
            throw new CheckException("用车用途不能为空");
        }
        VehicleApplicationUsageEnum usageEnum = VehicleApplicationUsageEnum.getByKey(this.getUsage());
        if (usageEnum == null) {
            throw new CheckException("用车用途不合法");
        }
        return this;
    }

    private VehicleApplication checkPassengerNum() {
        if (this.getPassengerNum() == null) {
            throw new CheckException("乘车人数不能为空");
        }
        return this;
    }

    private VehicleApplication checkOrigin() {
        if (StringUtils.isEmpty(this.getOrigin())) {
            throw new CheckException("始发地不能为空");
        }
        return this;
    }

    private VehicleApplication checkTerminal() {
        if (StringUtils.isEmpty(this.getTerminal())) {
            throw new CheckException("目的地不能为空");
        }
        return this;
    }

    private VehicleApplication checkApplyReason() {
        if (StringUtils.isEmpty(this.getApplyReason())) {
            throw new CheckException("用车事由不能为空");
        }
        return this;
    }

    private VehicleApplication checkPlanTime() {
        if (this.getPlanStartTime() == null || this.getPlanEndTime() == null) {
            throw new CheckException("计划时间不能为空");
        }
        if (!DateUtils.isSameDay(this.getPlanStartTime(), this.getPlanEndTime())) {
            throw new CheckException("派车时间必须在同一天");
        }
        if (this.getPlanEndTime().before(this.getPlanStartTime())) {
            throw new CheckException("派车计划结束时间不能早于计划开始时间");
        }
        if (this.getPlanStartTime().before(new Date())) {
            throw new CheckException("派车时间早于系统时间，无法派车");
        }
        return this;
    }
}


