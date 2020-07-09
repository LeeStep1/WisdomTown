package com.bit.module.oa.vo.inspect;

import com.bit.module.oa.bean.Inspect;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/2/20 13:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InspectExecutorVO extends Inspect {
    private Long executorId;
}
