package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Notice;
import com.bit.module.cbo.service.NoticeService;
import com.bit.module.cbo.vo.NoticePageVO;
import com.bit.module.cbo.vo.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Description 社区通知
 * @Author chenduo
 * @Date 2019/8/5 10:09
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	/**
	 * 新增通知
	 * @param noticeVO
	 * @return
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody @Valid NoticeVO noticeVO){
		return noticeService.add(noticeVO);
	}

	/**
	 * 编辑通知
	 * @param noticeVO
	 * @return
	 */
	@PostMapping("/modify")
	public BaseVo modify(@RequestBody @Valid NoticeVO noticeVO){
		return noticeService.modify(noticeVO);
	}

	/**
	 * web端返显或单查通知
	 * @param id
	 * @return
	 */
	@GetMapping("/webReflectById/{id}")
	public BaseVo webReflectById(@PathVariable(value = "id")Long id){
		return noticeService.webReflectById(id);
	}

	/**
	 * web端分页查询
	 * @param noticePageVO
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody NoticePageVO noticePageVO){
		return noticeService.webListPage(noticePageVO);
	}

	/**
	 * app端返显或单查通知 并且 改变已读状态
	 * @param id
	 * @return
	 */
	@GetMapping("/appReflectById/{id}")
	public BaseVo appReflectById(@PathVariable(value = "id")Long id,HttpServletRequest request){
		return noticeService.appReflectById(id,request);
	}

	/**
	 * app端分页查询
	 * @param noticePageVO
	 * @return
	 */
	@PostMapping("/appListPage")
	public BaseVo appListPage(@RequestBody NoticePageVO noticePageVO, HttpServletRequest request){
		return noticeService.appListPage(noticePageVO,request);
	}

	/**
	 * 删除通知
	 * @param id
	 * @return
	 */
	@GetMapping("/del/{id}")
	public BaseVo delById(@PathVariable(value = "id")Long id){
		return noticeService.delById(id);
	}

	/**
	 * 查看已读状态
	 * @param noticePageVO
	 * @return
	 */
	@PostMapping("/checkReadDetail")
	public BaseVo checkReadDetail(@RequestBody NoticePageVO noticePageVO){
		return noticeService.checkReadDetail(noticePageVO);
	}

	/**
	 * 更新联系状态
	 * @param id
	 * @return
	 */
	@GetMapping("/updateConnectionStatus/{id}")
	public BaseVo updateConnectionStatus(@PathVariable(value = "id")Long id){
		return noticeService.updateConnectionStatus(id);
	}

	/**
	 * 校验扩展类型有多少居民
	 * @param extendType
	 * @return
	 */
	@GetMapping("/checkOptionGroupNum/{extendType}")
	public BaseVo checkOptionGroupNum(@PathVariable(value = "extendType")Integer extendType){
		return noticeService.checkOptionGroupNum(extendType);
	}


}
