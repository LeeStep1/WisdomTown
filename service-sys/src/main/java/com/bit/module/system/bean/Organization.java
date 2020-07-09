package com.bit.module.system.bean;

import lombok.Data;

import java.util.List;

/**通用的组织树
 * @author chenduo
 * @create 2019-02-14 16:18
 */
@Data
public class Organization {
    /**
     * 组织id
     */
    private Long id;
    /**
     * 组织名称
     */
    private String name;

    /**
     * 子节点
     */
    private List<Organization> childList;
    /**
     * 组织层级
     */
    private Integer level;
    /**
     * 组织id 字符串
     */
    private String strId;
}
