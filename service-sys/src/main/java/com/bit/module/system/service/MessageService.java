package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.vo.MessageTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-02-16 20:07
 */

public interface MessageService {


    /**
     * 根据模板ID 查询APPID
     *
     * @param id : 模板ID
     * @author liyang
     * @date 2019-04-04
     */
    BaseVo getMessageTempByMessageTempId(Long id);

    /**
     * 根据模板详情获取该模板的消息类型名称和类目名称
     *
     * @param messageTemplate : 模板详情
     * @return Map<String,String> ：消息类型名称和类目名称
     * @author liyang
     * @date 2019-04-08
     */
    Map<String, String> getMessageTitle(MessageTemplate messageTemplate);

    /**
     * 根据模板ID获取相应的接入端ID
     *
     * @param messageTemplate : 模板详情
     * @return : String : 接入端ID
     * @author liyang
     * @date 2019-04-08
     */
    List<String> getTidByMessageTemplate(MessageTemplate messageTemplate);


    /**
     * 根据模板ID获取相应的接入端实例
     *
     * @param messageTemplate : 模板详情
     * @return : MessageTemplateRelTid : 模板与接入端
     * @author liyujun
     * @date 2019-04-08
     */
    List<MessageTemplateRelTid> getTidByMessageTemplates(MessageTemplate messageTemplate);

    /**
     * 根据模板ID获取相应的接入端ID
     *
     * @param messageTemplate : 模板：根据模板ID，筛选具体接入段的配置
     * @return : BaseVo :业务封装的返回类
     * @author lyj
     * @date 2019-04-08
     */
    List<MessageTemplateRelTid> getTemplateTidConfigByMessageTemplate(MessageTemplate messageTemplate);

    /**
     * @param id :模板id
     * @return : java.util.Map<java.lang.String,java.lang.String>
     * @description:
     * @author liyujun
     * @date 2020-04-08
     */
    public MessageTemplateVO getMessageTemplateCategory(Long id);

}
