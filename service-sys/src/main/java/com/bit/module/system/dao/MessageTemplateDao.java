package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.vo.MessageTemplatePageVO;
import com.bit.module.system.vo.MessageTemplateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-09 9:58
 */
public interface MessageTemplateDao {

    List<MessageTemplate> queryByAppId(MessageTemplate messageTemplate);

    /**
     * 根据id单查记录
     * @param id
     */
    MessageTemplate getMessageTemplateById(Long id);


    /**
     * 多参数查询
     * @return
     */
    List<MessageTemplate> findByParam(MessageTemplate messageTemplate);

    /**
     * 新增记录
     */
    void addMessageTemplate(MessageTemplate messageTemplate);

    /**
     * 编辑记录
     */
    void updateMessageTemplate(MessageTemplate messageTemplate);

    /**
     * 删除记录
     */
    void delMessageTemplateById(Long id);

    /**
     * 分页查询
     */
    List<MessageTemplateVO> listPage(MessageTemplatePageVO messageTemplatePageVO);
}
