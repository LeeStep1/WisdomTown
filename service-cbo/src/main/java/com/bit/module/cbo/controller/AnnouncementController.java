package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Announcement;
import com.bit.module.cbo.service.AnnouncementService;
import com.bit.module.cbo.vo.AnnouncementPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Description 社区公告
 * @Author chenduo
 * @Date 2019/8/5 10:08
 **/
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;

	/**
	 * 新增社区公告
	 * @param announcement
	 * @return
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody @Valid Announcement announcement){
		return announcementService.add(announcement);
	}

	/**
	 * 修改社区公告
	 * @param announcement
	 * @return
	 */
	@PostMapping("/modify")
	public BaseVo modify(@RequestBody @Valid Announcement announcement){
		return announcementService.modify(announcement);
	}


	/**
	 * web端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody AnnouncementPageVO announcementPageVO){
		return announcementService.webListPage(announcementPageVO);
	}

	/**
	 * app端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	@PostMapping("/appListPage")
	public BaseVo appListPage(@RequestBody AnnouncementPageVO announcementPageVO, HttpServletRequest request){
		return announcementService.appListPage(announcementPageVO,request);
	}

	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	@GetMapping("/reflect/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return announcementService.reflectById(id);
	}

	/**
	 * 删除社区公告
	 * @param id
	 * @return
	 */
	@GetMapping("/del/{id}")
	public BaseVo delById(@PathVariable(value = "id")Long id){
		return announcementService.delById(id);
	}
}
