package com.bit.module.oa.vo.checkIn;

import com.bit.module.oa.bean.CheckIn;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/21 18:12
 */
@Data
public class UserCheckInVO implements Serializable {
    private Long userId;

    private String userName;

    private List<CheckIn> checkIns;


}
