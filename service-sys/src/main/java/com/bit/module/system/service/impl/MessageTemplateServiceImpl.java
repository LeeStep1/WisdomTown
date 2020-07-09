package com.bit.module.system.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.module.system.bean.MessageCategory;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.dao.MessageCategoryDao;
import com.bit.module.system.dao.MessageTemplateDao;
import com.bit.module.system.dao.MessageTemplateRelTidDao;
import com.bit.module.system.service.MessageTemplateService;
import com.bit.module.system.vo.MessageTemplatePageVO;
import com.bit.module.system.vo.MessageTemplateVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-09 9:57
 */
@Service("messageTemplateService")
public class MessageTemplateServiceImpl extends BaseService implements MessageTemplateService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Autowired
	private MessageTemplateRelTidDao messageTemplateRelTidDao;

    @Autowired
	private MessageCategoryDao messageCategoryDao;

    /**
     * 查询消息模板
     * @param messageTemplate
     * @return
     */
    @Override
    public BaseVo queryByMessage(MessageTemplate messageTemplate) {
        if (messageTemplate.getMsgType().equals(5)){
            messageTemplate.setMsgType(2);
        }
        List<MessageTemplate> messageTemplates = messageTemplateDao.queryByAppId(messageTemplate);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(messageTemplates);
        return baseVo;
    }

    /**
     * 新增数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    @Override
    @Transactional
    public BaseVo add(MessageTemplateVO messageTemplateVO){


		MessageTemplate messageTemplate = new MessageTemplate();
		BeanUtils.copyProperties(messageTemplateVO,messageTemplate);

        messageTemplateDao.addMessageTemplate(messageTemplate);


		return successVo();
    }

	/**
	 * 配置模板
	 * @param messageTemplateRelTids
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo settingAdd(List<MessageTemplateRelTid> messageTemplateRelTids) {
		if (CollectionUtils.isEmpty(messageTemplateRelTids)){
			throwBusinessException("参数为空");
		}
		for (MessageTemplateRelTid messageTemplateRelTid : messageTemplateRelTids) {
			messageTemplateRelTidDao.addMessageTemplateRelTid(messageTemplateRelTid);
		}
		return successVo();
	}

	/**
	 * 配置模板 update
	 * @param messageTemplateRelTids
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo settingUpdate(List<MessageTemplateRelTid> messageTemplateRelTids) {
		if (CollectionUtils.isEmpty(messageTemplateRelTids)){
			throwBusinessException("参数为空");
		}

		messageTemplateRelTidDao.delMessageTemplateRelTidByTemplateId(messageTemplateRelTids.get(0).getTemplateId());

		messageTemplateRelTidDao.batchAdd(messageTemplateRelTids);
		return successVo();
	}

	@Override
	public BaseVo settingReflect(Long templateId) {
		if (templateId==null){
			throwBusinessException("参数为空");
		}
		MessageTemplateRelTid rel = new MessageTemplateRelTid();
		rel.setTemplateId(templateId);
		List<MessageTemplateRelTid> byParam = messageTemplateRelTidDao.findByParam(rel);

		return new BaseVo(byParam);
	}

	/**
     * 编辑数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    @Override
    @Transactional
    public BaseVo update(MessageTemplateVO messageTemplateVO) {


		MessageTemplate messageTemplate = new MessageTemplate();
		BeanUtils.copyProperties(messageTemplateVO,messageTemplate);

        messageTemplateDao.updateMessageTemplate(messageTemplate);


        return successVo();
    }

    /**
     * 删除数据
     * @param id
     * @author chenduo
     * @since ${date}
     */
    @Override
    @Transactional
    public BaseVo delete(Long id) {
        messageTemplateDao.delMessageTemplateById(id);
		messageTemplateRelTidDao.delMessageTemplateRelTidByTemplateId(id);
        return successVo();
    }

    /**
     * 多参数查询数据
     * @param messageTemplate
     * @author chenduo
     * @since ${date}
     * @return List<MessageTemplate>
     */
    @Override
    public BaseVo findByParam(MessageTemplate messageTemplate) {
        List<MessageTemplate> messageTemplateList = messageTemplateDao.findByParam(messageTemplate);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(messageTemplateList);
        return baseVo;
    }

    /**
     * 单查数据
     * @param id
     * @author chenduo
     * @since ${date}
     * @return MessageTemplate
     */
    @Override
    public BaseVo reflectById(Long id) {
        MessageTemplate messageTemplate =	messageTemplateDao.getMessageTemplateById(id);
		MessageTemplateVO messageTemplateVO = new MessageTemplateVO();
		BeanUtils.copyProperties(messageTemplate,messageTemplateVO);



		MessageCategory messageCategoryById = messageCategoryDao.getMessageCategoryById(messageTemplateVO.getCategoryId());
		if (messageCategoryById!=null){
			messageTemplateVO.setCategoryName(messageCategoryById.getCategoryName());
		}

        BaseVo baseVo = new BaseVo();
        baseVo.setData(messageTemplateVO);
        return baseVo;
    }

    /**
     * 分页查询
     * @param messageTemplatePageVO
     * @return
     */
    @Override
    public BaseVo listPage(MessageTemplatePageVO messageTemplatePageVO) {
		PageHelper.startPage(messageTemplatePageVO.getPageNum(), messageTemplatePageVO.getPageSize());
		List<MessageTemplateVO> messageTemplateVOS = messageTemplateDao.listPage(messageTemplatePageVO);
		PageInfo<MessageTemplateVO> pageInfo = new PageInfo<MessageTemplateVO>(messageTemplateVOS);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
	/**
	 * 校验模板与接入端和应用的关系
	 * @return
	 */
	@Override
	public BaseVo distinctRelParams(MessageTemplateRelTid messageTemplateRelTid) {
		BaseVo baseVo = new BaseVo();
		List<MessageTemplateRelTid> byParam = messageTemplateRelTidDao.findByParam(messageTemplateRelTid);
		if (CollectionUtils.isNotEmpty(byParam)){
			baseVo.setCode(ResultCode.PARAMS_KEY_EXIST.getCode());
			baseVo.setMsg(ResultCode.PARAMS_KEY_EXIST.getInfo());
		}else {
			baseVo.setCode(ResultCode.PARAMS_KEY_NOT_EXIST.getCode());
			baseVo.setMsg(ResultCode.PARAMS_KEY_NOT_EXIST.getInfo());
		}
		return baseVo;
	}
}
