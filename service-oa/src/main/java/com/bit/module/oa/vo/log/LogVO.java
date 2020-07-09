package com.bit.module.oa.vo.log;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/14 17:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogVO extends BasePageVo {
    /**
     * id
     */
    private Long id;
    /**
     * 关联id
     */
    private Long refId;
    /**
     * 操作者id
     */
    private Long operatorId;
    /**
     * 操作者姓名
     */
    private String operatorName;
    /**
     * 日志类型 0其他 1巡检 2隐患
     */
    private Integer type;
    /**
     * 日志操作
     */
    private String operate;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createAt;
}
