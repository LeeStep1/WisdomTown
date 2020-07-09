package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.service.MessageService;
import com.bit.module.system.vo.MessageTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-02-16 19:42
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 根据模板ID 查询APPID
     * @author liyang
     * @date 2019-04-04
     * @param id :
     * @return : com.bit.base.vo.BaseVo
     */
    @GetMapping("/getMessageTempByMessageTempId/{id}")
    public BaseVo getMessageTempByMessageTempId(@PathVariable(value = "id") Long id){
        return messageService.getMessageTempByMessageTempId(id);
    }

    /**
     * 根据模板详情获取该模板的消息类型名称和类目名称
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return Map<String,String> ：消息类型名称和类目名称
     */
    @PostMapping("/getMessageTitle")
    public Map<String,String> getMessageTitle(@RequestBody MessageTemplate messageTemplate){
        return messageService.getMessageTitle(messageTemplate);
    }

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    @PostMapping("/getTidByMessageTemplate")
    public List<String> getTidByMessageTemplate(@RequestBody MessageTemplate messageTemplate){
        return messageService.getTidByMessageTemplate(messageTemplate);
    }

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    @PostMapping("/getTidByMessageTemplates")
    public List<MessageTemplateRelTid> getTidByMessageTemplates(@RequestBody MessageTemplate messageTemplate){
        return messageService.getTidByMessageTemplates(messageTemplate);
    }

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyujun
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    @PostMapping("/getTemplateTidConfig")
    public List<MessageTemplateRelTid> getTemplateTidConfigByMessageTemplate(@RequestBody MessageTemplate messageTemplate){
        return messageService.getTemplateTidConfigByMessageTemplate(messageTemplate);
    }


    /**
     * 根据模板ID获取相应模板数据以及相关的类目数据
     * @author liyujun
     * @date 2020-04-08
     * @param id : 模板详情
     * @return : String ：接入端ID
     */
    @GetMapping("/getTemplateCategory/{id}")
    public MessageTemplateVO getTemplateCategory(@PathVariable  (value = "id") Long id){
        return messageService.getMessageTemplateCategory(id);
    }
}
