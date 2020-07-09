package com.bit.module.oa.vo.inspect;

import com.bit.base.vo.BasePageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 14:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InspectVO extends BasePageVo {
    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 巡检单号
     */
    private String no;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型 1智能巡检 2轨迹巡检
     */
    private Integer type;
    /**
     * 巡检开始时间
     */
    private Date startTime;
    /**
     * 巡检结束时间
     */
    private Date endTime;
    /**
     * 部门json集合，格式[{"id":xx,"name":xxx},{...}]
     */
    private String deps;
    /**
     * 执行者json集合，格式[{"id":xx,"name":xxx},{...}]
     */
    private String executors;
    /**
     * 巡检内容
     */
    private String content;
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;
    /**
     * 发布者id
     */
    private Long publisherId;
    /**
     * 最后上报时间
     */
    private Date lastReportAt;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 状态 0未发布 1未执行 2执行中 3已完成 4已终止 5已结束
     */
    private Integer status;
    private List<Integer> statusList;
    /**
     * 查询类型 1 发布  2 执行
     */
    private Integer queryType;

    private Long queryId;
}
