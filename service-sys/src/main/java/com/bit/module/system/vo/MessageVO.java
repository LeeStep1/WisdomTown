package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-02-16 19:41
 */
@Data
public class MessageVO extends BasePageVo{
    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private Long appId;

    /**
     * 标题
     */
    private String title;
    /**
     * 类目
     */
    private Integer categoryId;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否已读 0-已读 1-未读
     */
    private Integer readed;




}
