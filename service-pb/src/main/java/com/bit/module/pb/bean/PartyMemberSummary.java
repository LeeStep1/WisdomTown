package com.bit.module.pb.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 党员汇总信息
 *
 * @author jianming.fan
 * @date 2018-12-21
 */
@Data
public class PartyMemberSummary implements Serializable{
    /**
     * 这是PartyMemberApproval的id
     * 或RelationshipTransfer的id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片
     */
    private String photo;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthdate;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 民族
     */
    private String nation;
    /**
     * 入党时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date joinTime;
    /**
     * 学历
     */
    private String education;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 户籍所在派出所
     */
    private String policeStation;
    /**
     * 工作/学习单位
     */
    private String company;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 现居住地
     */
    private String address;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 退役军人信息
     */
    private ExServiceman exServiceman;
    /**
     * 原组织id
     */
    private String fromOrgId;
    /**
     * 原组织名称
     */
    private String fromOrgName;

}
