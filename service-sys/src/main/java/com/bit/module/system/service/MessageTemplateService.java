package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.vo.MessageTemplatePageVO;
import com.bit.module.system.vo.MessageTemplateVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-09 9:56
 */
public interface MessageTemplateService {
    /**
     * 查询消息模板
     * @param messageTemplate
     * @return
     */
    BaseVo queryByMessage(MessageTemplate messageTemplate);

    /**
     * 新增数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    BaseVo add(MessageTemplateVO messageTemplateVO);

    /**
     * 配置模板  add
     * @param messageTemplateRelTids
     * @return
     */
    BaseVo settingAdd(List<MessageTemplateRelTid> messageTemplateRelTids);

	/**
	 * 配置模板  update
	 * @param messageTemplateRelTids
	 * @return
	 */
	BaseVo settingUpdate(List<MessageTemplateRelTid> messageTemplateRelTids);


	BaseVo settingReflect(Long templateId);

    /**
     * 编辑数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    BaseVo update(MessageTemplateVO messageTemplateVO);

    /**
     * 删除数据
     * @param id
     * @author chenduo
     * @since ${date}
     */
    BaseVo delete(Long id);


    /**
     * 多参数查询数据
     * @param messageTemplate
     * @author chenduo
     * @since ${date}
     * @return List<MessageTemplate>
     */
    BaseVo findByParam(MessageTemplate messageTemplate);

    /**
     * 单查数据
     * @param id
     * @author chenduo
     * @since ${date}
     * @return MessageTemplate
     */
    BaseVo reflectById(Long id);

    /**
     * 分页查询
     * @param messageTemplatePageVO
     * @return
     */
    BaseVo listPage(MessageTemplatePageVO messageTemplatePageVO);
	/**
	 * 校验模板与接入端和应用的关系
	 * @return
	 */
    BaseVo distinctRelParams(MessageTemplateRelTid messageTemplateRelTid);
}
