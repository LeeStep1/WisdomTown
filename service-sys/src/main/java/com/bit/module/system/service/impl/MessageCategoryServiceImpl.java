package com.bit.module.system.service.impl;

import com.bit.module.system.bean.MessageCategory;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.dao.MessageCategoryDao;
import com.bit.module.system.dao.MessageTemplateDao;
import com.bit.module.system.service.MessageCategoryService;
import com.bit.module.system.vo.MessageCategoryPageVO;
import com.bit.module.system.vo.MessageCategoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.bit.base.vo.BaseVo;
import com.bit.base.service.BaseService;

import java.util.Collections;
import java.util.List;

@Service("messageCategoryService")
public class MessageCategoryServiceImpl extends BaseService implements MessageCategoryService {

	@Autowired
	private MessageCategoryDao messageCategoryDao;

	@Autowired
	private MessageTemplateDao messageTemplateDao;


	/**
	 * 新增数据
	 *
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	@Override
	@Transactional
	public BaseVo add(MessageCategory messageCategory) {
		messageCategoryDao.addMessageCategory(messageCategory);
		return successVo();
	}

	/**
	 * 编辑数据
	 *
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	@Override
	@Transactional
	public BaseVo update(MessageCategory messageCategory) {
		messageCategoryDao.updateMessageCategory(messageCategory);
		return successVo();
	}

	/**
	 * 删除数据
	 *
	 * @param id
	 * @author chenduo
	 * @since ${date}
	 */
	@Override
	@Transactional
	public BaseVo delete(Long id) {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setCategoryId(id);
		List<MessageTemplate> byParam = messageTemplateDao.findByParam(messageTemplate);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("存在模板不能删除");
		}
		messageCategoryDao.delMessageCategoryById(id);
		return successVo();
	}

	/**
	 * 多参数查询数据
	 *
	 * @param messageCategory
	 * @return List<MessageCategory>
	 * @author chenduo
	 * @since ${date}
	 */
	@Override
	public BaseVo findByParam(MessageCategory messageCategory) {
		List<MessageCategory> messageCategoryList = messageCategoryDao.findByParam(messageCategory);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(messageCategoryList);
		return baseVo;
	}

	/**
	 * 单查数据
	 *
	 * @param id
	 * @return MessageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	@Override
	public BaseVo reflectById(Long id) {
		MessageCategory messageCategory = messageCategoryDao.getMessageCategoryById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(messageCategory);
		return baseVo;
	}

	/**
	 * 分页查询
	 *
	 * @param messageCategoryPageVO
	 * @return
	 */
	@Override
	public BaseVo listPage(MessageCategoryPageVO messageCategoryPageVO) {
		PageHelper.startPage(messageCategoryPageVO.getPageNum(), messageCategoryPageVO.getPageSize());
		List<MessageCategoryVO> messageCategoryVOS = messageCategoryDao.listPage(messageCategoryPageVO);
		PageInfo<MessageCategoryVO> pageInfo = new PageInfo<MessageCategoryVO>(messageCategoryVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
}