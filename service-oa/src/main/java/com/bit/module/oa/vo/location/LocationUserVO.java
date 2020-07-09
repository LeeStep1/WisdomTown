package com.bit.module.oa.vo.location;

import com.bit.module.oa.bean.Location;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 10:27
 */
@Data
public class LocationUserVO implements Serializable {
    private Long userId;

    private List<Location> locations;
}
