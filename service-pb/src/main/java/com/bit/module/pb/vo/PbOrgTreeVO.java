package com.bit.module.pb.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-06-11
 **/
@Data
public class PbOrgTreeVO {

    //columns START

    /**
     * id
     */
    //private Long id;
    private String id;
    /**
     * 代码
     */
    private String pcode;
    /**
     * 名称
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 是否审批机关，0否 1是
     */
    private Integer isApprovalAuz;
    /**
     * 状态，0否 1是
     */
    private Integer status;
    /**
     * 组织类型
     */
    private Integer orgType;
    /**
     * 组织描述
     */
    private String orgDesc;
    /**
     * 临时字段---父id
     */
    private Long pid;
    /**
     * 临时字段---转string二进制的id
     */
    private String strId;
    /**
     * 临时字段---父id 转string二进制的id
     */
    private String strPid;
    /**
     * 临时字段---父名称
     */
    private String pname;
    /**
     * 临时字段---等级
     */
    private int level;
    /**
     * 临时字段---接受孩子list
     */
    private List<PbOrgTreeVO> childList;
    /**
     * 创建时间
     */
    private Date createTime;

}
