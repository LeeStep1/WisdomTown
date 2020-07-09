package com.bit.module.manager.bean;

import lombok.Data;

import java.util.List;

/**
 * @description: mongo的分页集合
 * @author: chenduo
 * @create: 2019-04-09 10:50
 */
@Data
public class PageResult<T> {
    /**
     * 页面大小
     */
    private Integer pageSize;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 记录总数
     */
    private Integer total;
    /**
     * 记录集合
     */
    private List<T> list;
}
