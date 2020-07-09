package com.bit.module.sv.bean;

import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.vo.FileInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Project implements Serializable {
    /**
    * ID
    */
    private Long id;

    /**
    * 项目名称
    */
    private String name;

    /**
    * 项目代码
    */
    private String code;

    /**
    * 项目状态(进度)(0：待审批，1：进行中，2：已暂停，3：已完成)
    */
    private Integer status;

    /**
    * 建设单位ID
    */
    private Long buildingUnitId;

    /**
    * 建设单位
    */
    private String buildingUnit;

    /**
    * 监理单位Id
    */
    private Long supervisingUnitId;

    /**
    * 监理单位
    */
    private String supervisingUnit;

    /**
    * 施工单位ID
    */
    private Long constructionUnitId;

    /**
    * 施工单位
    */
    private String constructionUnit;

    /**
    * 工程类别（1：城建，2：市政，3：拆迁，4：土整）
    */
    private Integer category;

    /**
    * 工程总造价（单位：元）
    */
    private Double cost;

    /**
    * 工程总面积（单位：平方米）
    */
    private Double area;

    /**
    * 所在地址
    */
    private String address;

    /**
    * 经度
    */
    private Double lng;

    /**
    * 纬度
    */
    private Double lat;

    /**
    * 合同开工日期
    */
    private Date startAt;

    /**
    * 合同竣工日期
    */
    private Date endAt;

    /**
     * 合同工期(单位：天)
     */
    private Long days;

    public Long getDays() {
        if (this.endAt != null && this.startAt != null) {
            return DateUtils.daysBetween(DateUtils.getDayStartTime(this.startAt), DateUtils.getDayStartTime(this.endAt)) + 1;
        }
        return days;
    }

    /**
    * 危险源
    */
    private List<IdName> hazard;

    /**
    * 附件
    */
    private List<FileInfoVO> attach;

    /**
    * 数据来源（3：安监，4：环保，6：城建）
    */
    private Integer source;

    /**
    * 创建时间
    */
    private Date createAt;

    /**
    * 更新时间
    */
    private Date updateAt;

    private static final long serialVersionUID = 1L;
}