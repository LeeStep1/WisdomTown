package com.bit.module.oa.vo.location;

import com.bit.module.oa.bean.Location;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 11:28
 */
@Data
public class LocationQO extends Location {
    private List<Long> executeIds;

    private Long inspectId;
}
