package com.bit.module.sv.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 增量查询请求参数
 */
@Data
public class IncrementalRequest implements Serializable {

    /**
     * 前值，一般是排序的那个字段值
     */
    private Long previous;

    /**
     * 企业单位ID
     */
    @NotNull(message = "企业单位ID不能为空", groups = IllegalSearch.class)
    private Long unitId;

    /**
     * 当前用户ID
     */
    private Long userId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态集合
     */
    private List<Integer> status;

    /**
     * 排序方向（1：升序，0：倒序）
     */
    private Integer direction = 0;

    /**
     * 查询的限定数量
     */
    private Integer limit = 10;

    /**
     * 来源(appId)
     */
    private Integer source;

    public interface IllegalSearch {

    }
}
