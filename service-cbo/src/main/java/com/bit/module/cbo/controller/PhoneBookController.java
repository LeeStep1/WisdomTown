package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PhoneBook;
import com.bit.module.cbo.service.PhoneBookService;
import com.bit.module.cbo.vo.PhoneBookVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 14:32
 **/
@RestController
@RequestMapping("/phoneBook")
public class PhoneBookController {

	@Autowired
	private PhoneBookService phoneBookService;

	/**
	 * 查询当前用户所在社区的电话
	 * @return
	 */
	@PostMapping("/getOrgTelInfoByToken")
	public BaseVo getOrgTelInfoByToken(){
		return phoneBookService.getOrgTelInfoByToken();
	}

	/**
	 * web端分页查询
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody PhoneBookVO phoneBookVO){
		return phoneBookService.webListPage(phoneBookVO);
	}

	/**
	 * web端小区物业电话修改提前校验
	 * @return
	 */
	@PostMapping("/checkCommunityTelExist")
	public BaseVo checkCommunityTelExist(@RequestBody PhoneBookVO phoneBookVO){
		return phoneBookService.checkCommunityTelExist(phoneBookVO);
	}

	/**
	 * 新增数据
	 * @param phoneBook
	 * @return
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody  PhoneBook phoneBook){
		return phoneBookService.add(phoneBook);
	}

	/**
	 * 修改数据
	 * @param phoneBook
	 * @return
	 */
	@PostMapping("/modify")
	public BaseVo modify(@RequestBody PhoneBook phoneBook){
		return phoneBookService.modify(phoneBook);
	}

	/**
	 * 居民app通讯录
	 * @return
	 */
	@PostMapping("/appResidentBook")
	public BaseVo appResidentBook(){
		return phoneBookService.appResidentBook();
	}

	/**
	 * 居委会app通讯录 物业
	 * @return
	 */
	@PostMapping("/appOrgBookWithPmc")
	public BaseVo appOrgBookWithPmc(){
		return phoneBookService.appOrgBookWithPmc();
	}

	/**
	 * 居委会app通讯录 社区
	 * @return
	 */
	@PostMapping("/appOrgBookWithCommunity")
	public BaseVo appOrgBookWithCommunity(){
		return phoneBookService.appOrgBookWithCommunity();
	}

	/**
	 * 物业app通讯录
	 * @return
	 */
	@PostMapping("/appPmcBook")
	public BaseVo appPmcBook(){
		return phoneBookService.appPmcBook();
	}

	/**
	 * web端 社区办	查询社区电话
	 * @param phoneBook
	 * @return
	 */
	@PostMapping("/getOrgTelInfo")
	public BaseVo getOrgTelInfo(@RequestBody PhoneBook phoneBook){
		return phoneBookService.getOrgTelInfo(phoneBook);
	}
}
