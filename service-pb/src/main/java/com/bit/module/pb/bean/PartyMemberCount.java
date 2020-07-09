package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-02-12 15:42
 */
@Data
public class PartyMemberCount {

    /**
     * 党组织id
     */
    private Long orgId;
    /**
     * 会议时间
     */
    private Date time;
    /**
     * 党员状态
     */
    private Integer status;
}
