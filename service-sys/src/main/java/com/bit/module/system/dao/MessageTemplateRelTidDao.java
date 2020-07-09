package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.vo.MessageTemplateRelTidPageVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.bit.module.system.vo.MessageTemplateRelTidVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 消息模板与接入端关系
 * 
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 16:41:26
 */
@Repository
public interface MessageTemplateRelTidDao {

	/**
    * 根据id单查记录
    * @param id
    */
	MessageTemplateRelTid getMessageTemplateRelTidById(Long id);


	/**
    * 多参数查询
    * @return
    */
	List<MessageTemplateRelTid> findByParam(MessageTemplateRelTid messageTemplateRelTid);

	/**
	* 新增记录
    */
	void addMessageTemplateRelTid(MessageTemplateRelTid messageTemplateRelTid);

	/**
    * 编辑记录
    */
	void updateMessageTemplateRelTid(MessageTemplateRelTid messageTemplateRelTid);

	/**
    * 删除记录
    */
	void delMessageTemplateRelTidById(Long id);

	/**
	 * 根据模板id删除数据
	 * @param templateId
	 */
	void delByTemplateId(Long templateId);

	/**
     * 分页查询
     */
	List<MessageTemplateRelTidVO> listPage(MessageTemplateRelTidPageVO messageTemplateRelTidPageVO);

	/**
	 * 根据模板id删除记录
	 */
	void delMessageTemplateRelTidByTemplateId(Long templateId);

	/**
	 * 批量新增
	 * @param messageTemplateRelTids
	 */
	void batchAdd(@Param(value = "messageTemplateRelTids") List<MessageTemplateRelTid> messageTemplateRelTids);
	
}
