package com.bit.module.oa.bean;

import com.bit.base.dto.UserInfo;
import com.bit.module.oa.enums.LogOperateEnum;
import com.bit.module.oa.enums.LogTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/14 11:08
 */
@Data
public class Log implements Serializable {
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
     * 日志类型 0其他 1巡检 2隐患 3补卡申请 4会议
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

    public static Log getLogByOperateAndType(Long id, UserInfo userInfo, LogOperateEnum logOperateEnum, LogTypeEnum logTypeEnum) {
        return getLogByOperateAndTypeAndContent(id, userInfo, logOperateEnum, logTypeEnum, logOperateEnum.getContent());
    }

    public static Log getLogByOperateAndTypeAndContent(Long id, UserInfo userInfo, LogOperateEnum logOperateEnum,
                                                       LogTypeEnum logTypeEnum, String content) {
        Log log = new Log();
        log.setRefId(id);
        log.setCreateAt(new Date());
        log.setOperatorId(userInfo.getId());
        log.setOperatorName(userInfo.getRealName());
        log.setType(logTypeEnum.getKey());
        log.setOperate(logOperateEnum.getOperate());
        log.setContent(content);
        return log;
    }
}

