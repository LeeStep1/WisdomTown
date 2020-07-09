package com.bit.module.sv.vo;

import com.bit.module.sv.bean.Regulation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/8/7 18:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegulationVO extends Regulation {
    private Integer range;
}
