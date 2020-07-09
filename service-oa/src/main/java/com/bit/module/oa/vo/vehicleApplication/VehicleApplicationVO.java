package com.bit.module.oa.vo.vehicleApplication;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * VehicleApplication
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleApplicationVO extends BasePageVo{

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

	//columns END

}


