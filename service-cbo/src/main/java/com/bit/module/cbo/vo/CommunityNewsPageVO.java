package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
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
public class CommunityNewsPageVO extends BasePageVo {

    /**
     * id
     */
    private Long id;

    /**
     * 风采标题
     */
    private String title;

    /**
     * 风采标题
     */
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
     * 发布查询开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginTime;

    /**
     * 发布查询结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

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
    private String pic;

    /**
     * 小区ID
     */
    private Long communityId;

}
