package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-19 13:25
 */
@Data
public class PartnerOrgVO extends BasePageVo {

    /**
     * id
     */
    private Long id;

    /**
     * 共建单位名称
     */
    private String partnerOrgName;
    /**
     * 共建单位地址
     */
    private String partnerOrgAddress;

    /**
     * 第一负责人
     */
    private String chargeMan;
    /**
     * 第一负责人手机号
     */
    private String chargeManMobile;

    /**
     * 共建单位介绍
     */
    private String partnerOrgIntroduction;
    /**
     * 共建单位人数
     */
    private Integer partnerOrgNumber;

    /**
     * 审核状态:状态：0待审核，1已通过，2：已退回
     */
    private Integer auditState;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人员
     */
    private Long updateUserId;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 旧的版本号
     */
    private Integer oldversion;
}
