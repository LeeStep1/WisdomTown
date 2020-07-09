package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 社区风采
 * @author: liyang
 * @date: 2019-08-29
 **/
@Data
public class CommunityNews {

    /**
     * id
     */
    private Long id;

    /**
     * 风采标题
     */
    @NotNull(message = "风采标题不能为空！")
    private String title;

    /**
     * 风采标题
     */
    @NotNull(message = "风采内容不能为空！")
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;

    /**
     * 发布人ID
     */
    private Long publishUserId;

    /**
     * 发布人姓名
     */
    private String publishUserName;

    /**
     * 状态：1 已发布，0 草稿
     */
    private Integer status;

    /**
     * 社区ID
     */
    private Long orgId;

    /**
     * 社区名称
     */
    private String orgName;

    /**
     * 封面图片id
     */
    @NotNull(message = "封面照片不能为空！")
    private String pic;

    /**
     * 封面图片地址
     */
    private String picAddress;

}
