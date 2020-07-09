package com.bit.ms.bean;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.List;

/**
 * 通知消息
 * @author liy
 */
@Data
public class Notification {

    /**
     *  标题
     */
    private String title;

    /**
     *  内容
     */
    private String content;

    /**
     * 设备的唯一注册号
     */
    private List<String> regIDList;

    /**
     * 扩展的业务字段，以key,value形式存在
     */
    private ImmutableMap<String, String> extras;
}
