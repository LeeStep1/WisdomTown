package com.bit.module.system.ApplyLogs.bean;

import lombok.Data;

import java.util.List;

/**
 * @description: mongo的分页集合
 * @author: chenduo
 * @create: 2019-04-09 10:50
 */
@Data
public class PageMongo<T> {
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 记录总数
     */
    private Long totalCount;
    /**
     * 记录集合
     */
    private List<T> rows;
}
