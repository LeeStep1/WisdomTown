package com.bit.module.oa.vo.zone;

import com.bit.module.oa.bean.Zone;
import lombok.Data;

/**
 * @Description :
 * @Date ： 2019/5/29 10:27
 */
@Data
public class ZoneQO extends Zone {
    /**
     * false 不显示无签到点的区域  true 显示有签到点的区域
     */
    private Boolean queryType = false;
}