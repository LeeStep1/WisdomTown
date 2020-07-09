package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/3 21:28
 */
@Data
public class RelationshipTransferParamsVO extends BasePageVo {

    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * 党员名称
     */
    private String memberName;

    /**
     * 原组织ID
     */
    private String fromOrgId;

    /**
     * 原组织
     */
    private String fromOrgName;

    /**
     * 目标组织id
     */
    private String toOrgId;

    /**
     * 转入组织
     */
    private String toOrgName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 转移类型
     * 1：镇内互转；
     * 2：镇内转镇外；
     * 3：镇外转镇内；
     * {@link com.bit.module.pb.enums.TransferTypeMenu}
     */
    private Integer transferType;

}
