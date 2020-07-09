package com.bit.soft.sms.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:  分页基础类
 * @Author: liyujun
 * @Date: 2020-06-01
 **/
@Data
public class Page<T> implements Serializable{

    /**
     * 每页显示条数
     **/
    public int pageSize = 10;

    /**
     * 当前页
     **/
    public int cunrrentPage = 1;

    /**
     * 总页数
     **/
    public long totalPage;

    /**
     * 总数据数
     **/
    public long totalRecordsNums;

    /**
     * 数据体
     **/
    public List<T> records;

    /**
     * @param pageSize         :
     * @param cunrrentPage     :
     * @param totalRecordsNums :
     * @param records          :
     * @description:
     * @author liyujun
     * @date 2020-06-01
     */
    public Page(int pageSize, int cunrrentPage, long totalRecordsNums, List<T> records) {
        this.pageSize = pageSize;
        this.cunrrentPage = cunrrentPage;
        this.totalRecordsNums = totalRecordsNums;
        this.records = records;
        getTotalPageNums();
    }

    /**
     * @param totalRecordsNums :
     * @param records          :
     * @description:
     * @author liyujun
     * @date 2020-06-01
     */
    public Page(long totalRecordsNums, List<T> records) {
        this.totalRecordsNums = totalRecordsNums;
        this.records = records;
        getTotalPageNums();
    }

    private void getTotalPageNums() {
        if ((this.totalRecordsNums % this.pageSize) == 0) {
            this.totalPage = this.totalRecordsNums / this.pageSize;
        } else {
            this.totalPage = this.totalRecordsNums / this.pageSize + 1;
        }
    }


}
