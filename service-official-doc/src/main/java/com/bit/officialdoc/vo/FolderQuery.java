package com.bit.officialdoc.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/18 18:11
 */
@Data
public class FolderQuery extends BasePageVo {

    private Long owner;
    /**
     * 文件夹名称
     */
    private String name;
}
