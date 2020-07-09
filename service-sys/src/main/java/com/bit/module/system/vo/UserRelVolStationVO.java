package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @author chenduo
 * @create 2019-03-22 13:17
 */
@Data
public class UserRelVolStationVO extends BasePageVo{
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 志愿者站点id
     */
    private Long stationId;
}
