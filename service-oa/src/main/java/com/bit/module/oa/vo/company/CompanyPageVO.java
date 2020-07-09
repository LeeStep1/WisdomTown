package com.bit.module.oa.vo.company;

import com.bit.module.oa.bean.Company;
import lombok.Data;

/**
 * @Description :
 * @Date ： 2019/1/17 10:46
 */
@Data
public class CompanyPageVO extends Company{
    /**
     * 经办人姓名
     */
    private String operatorName;
}
