package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageCategory;
import com.bit.module.system.vo.MessageCategoryPageVO;

/**
 * 
 *
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 15:21:25
 */
public interface MessageCategoryService {

	/**
	 * 新增数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	BaseVo add(MessageCategory messageCategory);

	/**
	 * 编辑数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 */
	BaseVo update(MessageCategory messageCategory);

	/**
	 * 删除数据
	 * @param id
	 * @author chenduo
	 * @since ${date}
	 */
	BaseVo delete(Long id);


	/**
	 * 多参数查询数据
	 * @param messageCategory
	 * @author chenduo
	 * @since ${date}
	 * @return List<MessageCategory>
	 */
	BaseVo findByParam(MessageCategory messageCategory);

	/**
	 * 单查数据
	 * @param id
	 * @author chenduo
	 * @since ${date}
	 * @return MessageCategory
	 */
	BaseVo reflectById(Long id);

	/**
    * 分页查询
	* @param messageCategoryPageVO
	* @return
	*/
	BaseVo listPage(MessageCategoryPageVO messageCategoryPageVO);
}

