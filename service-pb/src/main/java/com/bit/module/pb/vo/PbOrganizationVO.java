package com.bit.module.pb.vo;


import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-06-11
 **/
@Data
public class PbOrganizationVO  {

    //columns START

    /**
     * id
     */
    private String id;
   // private Long id;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 临时字段---父id 转string二进制的id
     */
    private String strPid;
    /**
     * 父级id  Long
     */
    private Long pid;

    /**
     * 二进制ID
     */
    private String strId;

    /**
     * 上级单位的名称
     */
    private  String pname;


    //private String idStr;


}
