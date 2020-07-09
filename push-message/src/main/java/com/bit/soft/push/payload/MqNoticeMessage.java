package com.bit.soft.push.payload;

import com.bit.base.exception.BusinessException;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * mq工程处理通知类的消息
 * @author mifei
 * @create 2019-02-16 19:37
 */
@Data
public class MqNoticeMessage {

    /**
     * 应用id
     */
    private Integer appId;
    /**
     * 对应的业务表id
     */
    private Long businessId;
    /**
     * 标题
     */
    private String title;
    /**
     * 目标id
     */
    private Long[] targetId;
    /**
     * 类型  0所有用户  1用户  2组织  必输项
     */
    private Integer targetType;
    /**
     * 接入端
     */
    private String[] tid;
    /**
     * 消息类型名称
     */
    private String msgTypeName;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 作者
     */
    private String creater;
    /**
     * 发布方
     */
    private String publishOrg;


    /**
     * @description:  校验参数
     * @author liyujun
     * @date 2020-04-07
     * @return : void
     */
    public void checkParams() {
        if(this.getTargetType() == null ){
            throw new BusinessException("消息发送失败：TargetType 为空");
        }
        if(this.getAppId() == null || this.getAppId().equals(0)){
            throw new BusinessException("消息发送失败：AppID 为空");
        }
        if(StringUtils.isEmpty(this.getCreater()) ){
            throw new BusinessException("消息发送失败：creater 为空");
        }
        if(StringUtils.isEmpty(this.getTitle()) ){
            throw new BusinessException("消息发送失败：Title 为空");
        }
        if(this.getTid().length == 0){
            throw new BusinessException("消息发送失败：tid 为空");
        }
        if(StringUtils.isEmpty(this.getMsgTypeName())){
            throw new BusinessException("消息发送失败：MsgTypeName 为空");
        }
        if(StringUtils.isEmpty(this.getMsgType()) ){
            throw new BusinessException("消息发送失败：MsgType 为空");
        }
    }


}
