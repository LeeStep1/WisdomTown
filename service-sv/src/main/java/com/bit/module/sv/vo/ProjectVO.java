package com.bit.module.sv.vo;

import com.bit.module.sv.bean.IdName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProjectVO implements Serializable {

    /**
    * ID
    */
    @NotNull(message = "项目ID不能为空", groups = Modify.class)
    private Long id;

    /**
    * 项目名称
    */
    @NotBlank(message = "项目名称不能为空", groups = Add.class)
    private String name;

    /**
    * 项目代码
    */
    @NotBlank(message = "项目代码不能为空", groups = Add.class)
    private String code;

    /**
    * 项目状态(进度)(0：待审批，1：进行中，2：已暂停，3：已完成)
    */
    private Integer status;

    /**
    * 建设单位ID
    */
    @NotNull(message = "建设单位不能为空", groups = Add.class)
    private Long buildingUnitId;

    /**
    * 监理单位
    */
    @NotNull(message = "监理单位不能为空", groups = Add.class)
    private Long supervisingUnitId;

    /**
    * 施工单位ID
    */
    @NotNull(message = "施工单位不能为空", groups = Add.class)
    private Long constructionUnitId;

    /**
    * 工程类别（1：城建，2：市政，3：拆迁，4：土整）
    */
    @NotNull(message = "工程类别不能为空", groups = Add.class)
    private Integer category;

    /**
    * 工程总造价（单位：元）
    */
    @NotNull(message = "工程总造价不能为空", groups = Add.class)
    private Double cost;

    /**
    * 工程总面积（单位：平方米）
    */
    @NotNull(message = "工程总面积不能为空", groups = Add.class)
    private Double area;

    /**
    * 所在地址
    */
    @NotBlank(message = "所在地址不能为空", groups = Add.class)
    private String address;

    /**
    * 经度
    */
    @NotNull(message = "所在地址经纬度不能为空", groups = Add.class)
    private Double lng;

    /**
    * 纬度
    */
    @NotNull(message = "所在地址经纬度不能为空", groups = Add.class)
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

    private static final long serialVersionUID = 1L;

    public interface Add {}

    public interface Modify {}
}