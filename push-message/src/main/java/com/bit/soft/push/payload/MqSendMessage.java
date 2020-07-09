package com.bit.soft.push.payload;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description: 业务层统一的消息推送模板（消息和待办的模板）
 * @Author: liyujun
 * @Date: 2019-09-04
 **/
@Data
public class MqSendMessage {

  public MqSendMessage(){

}
  /**
   * 类目
   */
  private Integer categoryId;


  /**
   * 对应的业务表id  如果为待办的话需要传送
   */
  private Long businessId;
  /**
   * 模板id
   */
  private Long templateId;
  /**
   * 目标id
   */
  private Long[] targetId;
  /**
   * 类型  0所有用户  1用户  2组织
   */
  private Integer targetType;

  /**
   * 消息参数
   */
  private String[] params;

  /**
   * 锁
   */
  private Integer version;

  /**
   * 定时推送时间 yyyy-MM-dd HH:mm:ss
   */
  private String pushTime;

  /**
   * 定义app端极光推送的方式 默认 0：别名推送(别名推送目前默认一种组装方式)，1 标签推送
   */
  private int appJpushType;
  /**
   * 标签推送的标签，其他方式为空
   */
  private List<String>tagList;

  /**
   * 标签推送的标签，其他方式为空
   */
  private String creater;

  /**
   * 创建时间
   */
  private Date createTime;


}
