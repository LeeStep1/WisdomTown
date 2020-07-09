package com.bit.base.vo;


/**
 * @Description:基础分页VO
 * @Author: mifei
 * @Date: 2018-09-17
 **/
public class BasePageVo<T> extends BaseVo {

    /**
     * 要检索页码.
     */
    private int pageNum = 1;
    /**
     * 每页条数.
     */
    private int pageSize = 10;

    /**
     * 排序的字段
     */
    private String orderBy;

    /**
     * 排序顺序
     */
    private String order;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
