package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.dao.DictDao;
import com.bit.module.system.dao.MessageDao;
import com.bit.module.system.service.MessageService;
import com.bit.module.system.vo.MessageTemplateVO;
import com.bit.soft.push.msEnum.MessagePushCategoryEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bit.common.SysConst.MESSAGE_CATEGORY_NAME;
import static com.bit.common.SysConst.MESSAGE_TYPE_NAME;

//import com.bit.module.mqCore.MqEnum.MessagePushCategoryEnum;

/**
 * @author chenduo
 * @create 2019-02-16 20:10
 */
@Service("messageService")
public class MessageServiceImpl extends BaseService implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private DictDao dictDao;




    /**
     * 根据模板ID查询所属APPID
     * @author liyang
     * @date 2019-04-04
     * @param id : 模板ID
     * @return : BaseVo
    */
    @Override
    public BaseVo getMessageTempByMessageTempId(Long id) {

        //根据模板ID查询
        MessageTemplate messageTemplate = messageDao.getMessageTempByMessageTempIdSql(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(messageTemplate);
        return baseVo;
    }

    /**
     * 根据模板详情获取该模板的消息类型名称和类目名称
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : Map<String,String> ：消息类型名称和类目名称
    */
    @Override
    public Map<String,String> getMessageTitle(MessageTemplate messageTemplate) {
        Dict dtMsg = new Dict();
        Dict dtCategory = new Dict();

        //先获取消息类型名称
        dtMsg.setModule(SysConst.MESSAGE_TYPE);
        dtMsg.setDictCode(String.valueOf(messageTemplate.getMsgType()));
        dtMsg = dictDao.findByModuleAndDictCode(dtMsg);

        int msgType = messageTemplate.getMsgType();
        if (msgType != 1) {//不是消息就按待办处理
            msgType = 2;
        }
        //得到Category信息
        MessagePushCategoryEnum categoryEnum = MessagePushCategoryEnum.getTypeByAppidAndMsgType(messageTemplate.getAppId(), msgType);
        dtCategory.setModule(categoryEnum.getModule());
        dtCategory.setDictCode(messageTemplate.getCategory());
        dtCategory = dictDao.findByModuleAndDictCode(dtCategory);

        //组成标题
        Map<String,String> messageMap = new HashMap<>();

        //消息类型名称
        messageMap.put(MESSAGE_TYPE_NAME,dtMsg.getDictName());

        //消息类目名称
        messageMap.put(MESSAGE_CATEGORY_NAME,dtCategory.getDictName());

        return messageMap;
    }

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
    */
    @Override
    public List<String> getTidByMessageTemplate(MessageTemplate messageTemplate) {
        List<String> tidList = messageDao.getTidByMessageTemplateSql(messageTemplate);
        return tidList;
    }

    /**
     * 根据模板ID获取相应的接入端实例
     * @author liyujun
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : MessageTemplateRelTid : 模板与接入端
     */
    @Override
    public List<MessageTemplateRelTid> getTidByMessageTemplates(MessageTemplate messageTemplate){
        return messageDao.getTidByMessageTemplates(messageTemplate);
    }


    /**
     * 根据模板ID获取相应的接入端ID
     * @author lyj
     * @date 2019-04-08
     * @param messageTemplate : 模板：根据模板ID，筛选具体接入段的配置
     * @return : BaseVo :业务封装的返回类
     */
    @Override
    public List<MessageTemplateRelTid> getTemplateTidConfigByMessageTemplate(MessageTemplate messageTemplate){
        return  messageDao.getTemplateTidConfigByMessageTemplate(messageTemplate);

    }


    /**
     * @param id :模板id
     * @return : java.util.Map<java.lang.String,java.lang.String>
     * @description:
     * @author liyujun
     * @date 2020-04-08
     */
    @Override
    public MessageTemplateVO getMessageTemplateCategory(Long id){
        MessageTemplateVO vo=messageDao.getMessageTemplateCategory(id);
        if(vo!=null){
            Dict dtMsg = new Dict();
            //先获取消息类型名称
            dtMsg.setModule(SysConst.MESSAGE_TYPE);
            dtMsg.setDictCode(String.valueOf(vo.getMsgType()));
            dtMsg = dictDao.findByModuleAndDictCode(dtMsg);
            vo.setCategoryName(dtMsg.getDictName());
        }
        return vo;
    }
}
