package com.bit.module.oa.vo.meetingRoom;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/3/1 13:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingRoomVO extends BasePageVo {
    /**
     *
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 地点
     */
    private String location;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 状态，0停用 1启用
     */
    private Boolean status;
}
