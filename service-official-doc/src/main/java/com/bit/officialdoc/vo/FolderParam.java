package com.bit.officialdoc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/24 10:24
 */
@Data
public class FolderParam implements Serializable{

    private Long id;

    private String name;

    private Integer num;
    /**
     * 未读
     */
    private Integer unread;
}
