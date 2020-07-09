package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.PmcAnnouncementPageVO;
import com.bit.module.cbo.service.PmcAnnouncementService;
import com.bit.module.cbo.vo.PmcAnnouncementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 物业公告
 * @Author chenduo
 * @Date 2019/8/5 10:09
 **/
@RestController
@RequestMapping("/pmcAnnouncement")
public class PmcAnnouncementController {

	@Autowired
	private PmcAnnouncementService pmcAnnouncementService;

	/**
	 * app新增物业公告
	 * @param pmcAnnouncementVO
	 * @return
	 */
	@PostMapping("/appInsert")
	public BaseVo appInsert(@RequestBody PmcAnnouncementVO pmcAnnouncementVO, HttpServletRequest request){
		return pmcAnnouncementService.appInsert(pmcAnnouncementVO,request);
	}

	/**
	 * app编辑物业公告
	 * @param pmcAnnouncementVO
	 * @return
	 */
	@PostMapping("/appEdit")
	public BaseVo appEdit(@RequestBody PmcAnnouncementVO pmcAnnouncementVO, HttpServletRequest request){
		return pmcAnnouncementService.appEdit(pmcAnnouncementVO,request);
	}

	/**
	 * app分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	@PostMapping("/appListPage")
	public BaseVo appListPage(@RequestBody PmcAnnouncementPageVO pmcAnnouncementPageVO,HttpServletRequest request){
		return pmcAnnouncementService.appListPage(pmcAnnouncementPageVO,request);
	}

	/**
	 * web端分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody PmcAnnouncementPageVO pmcAnnouncementPageVO){
		return pmcAnnouncementService.webListPage(pmcAnnouncementPageVO);
	}

	/**
	 * 返显记录
	 * @param id
	 * @return
	 */
	@GetMapping("/reflectById/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return pmcAnnouncementService.reflectById(id);
	}

	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	@GetMapping("/delById/{id}")
	public BaseVo delById(@PathVariable(value = "id")Long id){
		return pmcAnnouncementService.delById(id);
	}
}
