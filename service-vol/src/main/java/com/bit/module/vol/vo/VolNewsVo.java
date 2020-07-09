package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 志愿者风采
 * @author Liy
 */
@Data
public class VolNewsVo extends BasePageVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 主题名称
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人ID
     */
    private Long updateUserId;

    /**
     * 状态 0-已通过 1-草稿 2-待发布 3-已拒绝 4-审核中 5-默认
     */
    private int auditState;

    /**
     * 图片ID
     */
    private Long imgId;

    /**
     * 创建者服务站
     */
    private Long createStationId;

    /**
     * 审核服务站
     */
    private Long applyStationId;

    /**
     * 创建者服务站ID
     */
    private String createStationName;

    /**
     * 版本
     */
    private int version;

    /**
     * 操作类型 1、保存  2 提交
     */
    private String operationType;

    /**
     * 服务站集合
     */
    private List<Long> stationIds;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 图片地址
     */
    private String imgPath;

    /**
     * 文章状态 0 未删除  1 已删除
     */
    private int status;

    /**
     * 浏览量
     */
    private int browseCount;

    /**
     *  退回原因
     */
    private String backReason;

    /**
     * 草稿状态(仅查询过滤时使用)
     */
    private Integer auditStateDraft;
}
