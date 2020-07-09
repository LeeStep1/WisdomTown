package com.bit.module.pb.vo;

import com.bit.module.pb.bean.PartyDue;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2018/12/27 21:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalPartyDueVO extends PartyDue {
    /**
     * 党员姓名
     */
    private String memberName;
}
