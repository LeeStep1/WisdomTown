package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PhoneBook;
import com.bit.module.cbo.vo.PhoneBookVO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 14:34
 **/
public interface PhoneBookService {
	/**
	 * 查询当前用户所在社区的电话
	 * @return
	 */
	BaseVo getOrgTelInfoByToken();

	/**
	 * web端分页查询
	 * @return
	 */
	BaseVo webListPage(PhoneBookVO phoneBookVO);

	/**
	 * web端小区物业电话修改提前校验
	 * @return
	 */
	BaseVo checkCommunityTelExist(PhoneBookVO phoneBookVO);
	/**
	 * 新增数据
	 * @param phoneBook
	 * @return
	 */
	BaseVo add(PhoneBook phoneBook);
	/**
	 * 修改数据
	 * @param phoneBook
	 * @return
	 */
	BaseVo modify(PhoneBook phoneBook);
	/**
	 * 居民app通讯录
	 * @return
	 */
	BaseVo appResidentBook();

	/**
	 * 居委会app通讯录 物业
	 * @return
	 */
	BaseVo appOrgBookWithPmc();
	/**
	 * 居委会app通讯录 社区
	 * @return
	 */
	BaseVo appOrgBookWithCommunity();
	/**
	 * 物业app通讯录
	 * @return
	 */
	BaseVo appPmcBook();
	/**
	 * web端 社区办	查询社区电话
	 * @param phoneBook
	 * @return
	 */
	BaseVo getOrgTelInfo(PhoneBook phoneBook);
}
