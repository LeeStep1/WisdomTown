package com.bit.soft.push.payload;

import com.bit.base.exception.BusinessException;

import com.bit.soft.push.msEnum.AppPushTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * mq工程处理待办消息类的消息
 *
 * @author mifei
 * @create 2019-02-16 19:37
 */
@Data
public class MqMessage {


    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 标题  后面去掉
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
     * 类型  1内部用户  2外部用户  后期去掉计划，与模板定义项重复
     */
    private String targetUserType;
    /**
     * 消息参数
     */
    private String[] params;

    /**
     * 作者
     */
    private String creater;
    /**
     * 锁
     */
    private Integer version;

    /**
     * 定时推送时间 yyyy-MM-dd HH:mm:ss
     */
    private String pushTime;


    /**新增参数 ，之前无此参数，故进行默认值**/
    /**
     * 新增是否存储  0：存储，1: 不存存储
     */
   /* private int store;*/

    /**
     * 定义app端极光推送的方式 默认 0：别名推送(别名推送目前默认一种组装方式)，1 标签推送
     */
    private int appJpushType;

    /**
     * 标签推送的标签，其他方式为空
     */
    private List<String> tagList;
    
    /**
     * @description:  校验参数
     * @author liyujun
     * @date 2020-04-07
     * @return : void
     */
    public void checkParams() {

        if(this.getTargetType() == null){
            throw new BusinessException("参数不全:推送类型不为空");
        }
/*        if(StringUtils.isEmpty(this.getTargetUserType())){
            throw new BusinessException("参数不全:推送类型不为空");
        }*/
//        if(StringUtils.isEmpty(this.getCreater())){
//            throw new BusinessException("创建者不能为空");
//        }
        if(this.getTargetId() == null || this.getTargetId().length <= 0){
            throw new BusinessException("参数不全:targetId 为空");
        }
        if(this.getVersion() == null ){
            this.setVersion(0);
        }
        if(this.templateId == null ){
            throw new BusinessException("模板ID不能为空");
        }

        if(this.appJpushType== AppPushTypeEnum.PUSHBYTAG.getPushCode()){
            if (tagList.isEmpty()){
                throw new BusinessException("参数不全");
            }
        }else if(this.appJpushType== AppPushTypeEnum.PUSHBYALIASE.getPushCode() ){
            if (this.targetId==null||this.targetId.length==0){
                throw new BusinessException("推送方式為别名推送时，推送目标不能为空");
            }
        }else{
            throw new BusinessException("推送方式参数非法");
        }
    }


}
