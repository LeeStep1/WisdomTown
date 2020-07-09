package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description : 文明行为信息表
 * @Author liyang
 * @Date 2019/8/31 11:34
 **/
@Data
public class CivilizationBehavior implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 文明行为的标题
     */
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

    /**
     * 地址
     */
    @NotNull(message = "地址不能为空")
    private String address;

    /**
     * 图片集合
     */
    @NotNull(message = "图片不能为空")
    private String picIds;

    /**
     * 社区id
     */
    private Long orgId;

    /**
     * 社区名称
     */
    private String orgName;

    /**
     * 小区ID
     */
    private Long communityId;

    /**
     * 小区名称
     */
    private String communityName;

    /**
     * 数据来源：2物业，3 居民
     */
    private Integer dataType;

    /**
     * 状态:1：待处理，2：处理中:3：已处理
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人手机号
     */
    private String createUserMobile;

    /**
     * 反馈人ID（居委会）
     */
    private Long feedbackUserId;

    /**
     * 反馈者的姓名
     */
    private String feedbackUserName;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date feedbackTime;

    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 数据更新人，用于追溯,居委会人员id
     */
    private Long updateUserId;

    /**
     * 图片地址
     */
    private List<String> picAddress;

    /**
     * 版本号
     */
    private int version;

}