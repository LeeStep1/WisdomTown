package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Announcement;
import com.bit.module.cbo.vo.AnnouncementPageVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface AnnouncementService {
	/**
	 * 新增社区公告
	 * @param announcement
	 * @return
	 */
	BaseVo add(Announcement announcement);

	/**
	 * 修改社区公告
	 * @param announcement
	 * @return
	 */
	BaseVo modify(Announcement announcement);


	/**
	 * web端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	BaseVo webListPage(AnnouncementPageVO announcementPageVO);

	/**
	 * app端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	BaseVo appListPage(AnnouncementPageVO announcementPageVO, HttpServletRequest request);
	/**
	 * 返显或单查记录
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);
	/**
	 * 删除社区公告
	 * @param id
	 * @return
	 */
	BaseVo delById(Long id);
}
