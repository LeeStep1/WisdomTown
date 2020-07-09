package com.bit.module.sv.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RectificationNotice implements Serializable {
    /**
     * 整改通知单ID
     */
    @NotNull(message = "整改通知单ID不能为空", groups = Modify.class)
    private Long id;

    /**
     * 所属任务ID
     */
    @NotNull(message = "所属任务ID不能为空", groups = Add.class)
    private Long taskId;

    /**
     * 整改类型（1：现场整改，2：限期整改）
     */
    @NotNull(message = "整改类型不能为空", groups = Add.class)
    private Integer type;

    /**
     * 整改期限
     */
    private Date deadline;

    /**
     * 整改要求
     */
    @NotBlank(message = "整改要求不能为空", groups = Add.class)
    private String demand;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 现场整改结果
     */
    private DescriptionVO result;

    /**
     * 整改依据
     */
    @NotEmpty(message = "整改依据不能为空", groups = Add.class)
    private List<IdName> rectificationBasis;

    private static final long serialVersionUID = 1L;

    @Data
    public static class DescriptionVO {
        private String remark;
        private List<String> images;
    }

    public interface Add {

    }

    public interface Modify {

    }
}