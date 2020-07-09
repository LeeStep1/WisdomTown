package com.bit.officialdoc.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Data
public class DocQuery extends BasePageVo {

    // 所属人id
    private Long owner;
    // 公文标题
    private String title;
    // 字号
    private String textNumber;

    private Date begin;

    private Date end;
    //未发送/已发送
    private Boolean sent;
    //待办/已办
    private Boolean processed;
    //文件夹id
    private Long folderId;

}
