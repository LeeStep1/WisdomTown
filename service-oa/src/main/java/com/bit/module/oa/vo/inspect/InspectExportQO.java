package com.bit.module.oa.vo.inspect;

import com.bit.module.oa.bean.Inspect;
import lombok.Data;

/**
 * @Description :
 * @Date ： 2019/2/22 18:51
 */
@Data
public class InspectExportQO extends Inspect {
    /**
     * 查询类型 1 发布  2 执行
     */
    private Integer queryType;
}
