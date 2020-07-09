package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageCategory;
import com.bit.module.system.vo.MessageCategoryPageVO;
import com.bit.module.system.vo.MessageCategoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 15:21:25
 */
@Repository
public interface MessageCategoryDao{

	/**
    * 根据id单查记录
    * @param id
    */
	MessageCategory getMessageCategoryById(Long id);


	/**
    * 多参数查询
    * @return
    */
	List<MessageCategory> findByParam(MessageCategory messageCategory);

	/**
	* 新增记录
    */
	void addMessageCategory(MessageCategory messageCategory);

	/**
    * 编辑记录
    */
	void updateMessageCategory(MessageCategory messageCategory);

	/**
    * 删除记录
    */
	void delMessageCategoryById(Long id);

	/**
     * 分页查询
     */
	List<MessageCategoryVO> listPage(MessageCategoryPageVO messageCategoryPageVO);
	
}
