package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.base.vo.BasePageVo;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyBasicLivingAllowances;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 居民低保服务名单导出表
 * @author: liyang
 * @date: 2019-12-09
 **/
@Data
public class ResidentBasicLivingRosterExcelVO{

    /**
     * id
     */
    private Long id;

    /**
     * 序号
     */
    @Excel(name = "序号",width = 12,orderNum = "1")
    private Long orderId;

    /**
     * 居民的名称
     */
    @Excel(name = "申请人",width = 12,orderNum = "2")
    private String residentName;

    /**
     * 低保证号
     */
    @Excel(name = "低保证号",width = 16,orderNum = "3")
    private String cardNum;

    /**
     * 手机号
     */
    @Excel(name = "手机号码",width = 12,orderNum = "4")
    private String mobileNum;

    /**
     * 申请记录申请的日期
     */
    @Excel(name = "申请时间",width = 12,orderNum = "5")
    private String applyTime;

    /**
     * 户主
     */
    @Excel(name = "户主姓名",width = 12,orderNum = "6")
    private String houseHolderName;

    /**
     * 户主身份证号
     */
    @Excel(name = "户主身份证号",width = 16,orderNum = "7")
    private String houseHolderCardNum;

    /**
     * 经办人名称
     */
    @Excel(name = "经办人",width = 12,orderNum = "8")
    private String operatorName;

    /**
     * 救助人数
     */
    @Excel(name = "救助人数",width = 12,orderNum = "9")
    private Integer rescueNum;

    /**
     * 发放时间
     */
    @Excel(name = "低保证签发日期",width = 12,orderNum = "10")
    private String releaseTime;

    /**
     * 银行卡号
     */
    @Excel(name = "银行卡号",width = 16,orderNum = "11")
    private String bankCard;

    /**
     * 金额
     */
    @Excel(name = "低保金额",width = 12,orderNum = "12")
    private String amount;

    /**
     * 致贫原因
     */
    @Excel(name = "致贫原因",width = 12,orderNum = "13")
    private String poorReason;

    /**
     * 家庭电话
     */
    @Excel(name = "家庭电话",width = 12,orderNum = "14")
    private String familyMobile;

    /**
     * 家庭住址
     */
    @Excel(name = "家庭住址",width = 12,orderNum = "15")
    private String familyAddress;

    /**
     * 是否有效，1：正常，0：已失效
     */
    @Excel(name = "状态",width = 12,orderNum = "16")
    private String statusStr;

    /**
     * 社区id
     */
    private Long orgId;

    /**
     * 所属社区名称
     */
    private String orgName;

    /**
     * 申请的类别ID
     */
    private Long categoryId;

    /**
     * 申请的类事项ID
     */
    private Long serviceId;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * '申请的主题'
     */
    private String title;

}
