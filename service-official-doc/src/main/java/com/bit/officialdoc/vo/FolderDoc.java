package com.bit.officialdoc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/18 18:08
 */
@Data
public class FolderDoc implements Serializable {

    private Long id;

    private String name;
    /**
     * 未读
     */
    private Integer unRead;
    /**
     * 已读
     */
    private Integer read;

    /**
     * 旧ID
     */
    private Long oId;

}
