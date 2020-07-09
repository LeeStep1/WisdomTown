package com.bit.module.vol.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-28 21:10
 */
@Data
public class CampaignRecordExcel {
    @Excel(name = "序号")
    private Long index;
    /**
     * 志愿者姓名
     */
    @Excel(name = "姓名")
    private String volunteerName;
    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String cardId;
    /**
     * 捐款金额
     */
    @Excel(name = "金额（元）")
    private BigDecimal donateMoney;

    /**
     * 校验是否一致
     * @param campaignRecordExcel
     * @return
     */
    public boolean equals(CampaignRecordExcel campaignRecordExcel){
        if(volunteerName.equals(campaignRecordExcel.getVolunteerName()) && cardId.equals(campaignRecordExcel.getCardId()))
        {
            return true;
        }
        return false;
    }
}
