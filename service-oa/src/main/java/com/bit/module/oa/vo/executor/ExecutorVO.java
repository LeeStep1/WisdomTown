package com.bit.module.oa.vo.executor;

import com.bit.base.vo.BasePageVo;
import com.bit.module.oa.bean.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/18 11:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutorVO extends BasePageVo {
    private Long id;
    /**
     * 巡检id
     */
    private Long inspectId;
    /**
     * 巡检单号
     */
    private String inspectNo;
    /**
     * 巡检名称
     */
    private String inspectName;
    /**
     * 巡检类型 1智能巡检 2轨迹巡检
     */
    private Integer inspectType;
    /**
     * 巡检开始时间
     */
    private Date inspectStartTime;
    /**
     * 巡检结束时间
     */
    private Date inspectEndTime;
    /**
     * 部门json集合，格式[{"id":xx,"name":xxx},{...}]
     */
    private List<Dept> inspectDeps;
    /**
     * 执行者id
     */
    private Long executorId;
    /**
     * 签到状态 0缺卡 1正常 2补卡 3补卡成功 4补卡失败
     */
    private Integer checkInStatus;
    /**
     * 补卡状态 0未审批 1通过 2驳回
     */
    private Integer applyStatus;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 状态 1未执行 2执行中 3已完成 4已终止
     */
    private Integer status;
}
