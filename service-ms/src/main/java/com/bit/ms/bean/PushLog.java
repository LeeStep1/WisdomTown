package com.bit.ms.bean;

import lombok.Data;

/**
 * 推送消息日志
 * @author Liy
 */
@Data
public class PushLog {

    /**
     * 消息标题
     */
    private String pushTitle;

    /**
     * 推送内容
     */
    private String pushContent;

    /**
     * 推送类型 (设备推送，别名推送，标签推送)
     */
    private String pushType;

    /**
     * 推送是否成功
     */
    private boolean pushFlg;
}
