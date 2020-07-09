package com.bit.module.oa.vo.zone;

import com.bit.module.oa.bean.Spot;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/13 17:52
 */
@Data
public class ZoneSpotVO implements Serializable {
    private Long id;
    /**
     * 名称
     */
    private String zoneName;

    /**
     * 签到点
     */
    private List<Spot> spots;
}
