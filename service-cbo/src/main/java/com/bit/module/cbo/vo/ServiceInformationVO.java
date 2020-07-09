package com.bit.module.cbo.vo;

import com.bit.module.cbo.bean.*;
import lombok.Data;

/**
 * @description: 业务信息集合
 * @author: liyang
 * @date: 2019-08-14
 **/
@Data
public class ServiceInformationVO {

    /**
     * 台账ID
     */
    private Long applyId;

    /**
     * 补充业务信息 低保申请
     */
    private ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances;

    /**
     * 补充业务信息 居家养老申请
     */
    private ResidentApplyHomeCare residentApplyHomeCare;
    /**
     * 补充业务信息 残疾人申请
     */
    private ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals;
    /**
     * 补充业务信息 特殊扶助申请
     */
    private ResidentApplySpecialSupport residentApplySpecialSupport;
    /**
     * 附件id
     */
    private String attachedIds;

    /**
     * 补充业务信息父类
     */
    private ExtendTypeBase extendTypeBase;

}
