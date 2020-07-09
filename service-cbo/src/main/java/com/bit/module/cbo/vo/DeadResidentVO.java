package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 死亡居民信息
 * @author: liyang
 * @date: 2019-07-22
 **/
@Data
public class DeadResidentVO extends BasePageVo{

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 死者名称
     */
    private String name;

    /**
     * 证件类型1 身份证，2士官证、3护照、4港澳通行证
     */
    private Integer cardType;

    /**
     * 证件号
     */
    private String cardNum;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 1男2女
     */
    private Integer sex;

    /**
     * 人群类型 1残疾、2居家养老、3失独、4低保、5其他
     */
   /* private Integer type;*/
    private String type;

    /**
     * 住房的身份：1业主、2家属、3租客
     */
    private Integer identity;

    /**
     * 所属社区的id
     */
    private Long oaOrgId;

    /**
     * 所属社区的名字
     */
    private String oaOrgName;

    /**
     * 房屋的id
     */
    private Long locationId;

    /**
     * 房屋的详细地址
     */
    private String locationName;

    /**
     * yyyy-MM-DD 死亡日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date deadTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人员id
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新者的ID
     */
    private Long updateUserId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
