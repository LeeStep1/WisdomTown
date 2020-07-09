package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/7/25 13:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegulationContentVO extends BasePageVo {
    /**
     * id
     */
    private Long id;

    /**
     * 法规id
     */
    private Long regulationId;

    /**
     * 标题
     */
    private String title;

    /**
     * 正文内容
     */
    private String content;
}
