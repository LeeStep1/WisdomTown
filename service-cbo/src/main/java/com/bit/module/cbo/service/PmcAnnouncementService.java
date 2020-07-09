package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.PmcAnnouncementPageVO;
import com.bit.module.cbo.vo.PmcAnnouncementVO;

import javax.servlet.http.HttpServletRequest;

public interface PmcAnnouncementService {
	/**
	 * 新增物业公告
	 * @param pmcAnnouncementModelVO
	 * @return
	 */
	BaseVo appInsert(PmcAnnouncementVO pmcAnnouncementModelVO,HttpServletRequest request);
	/**
	 * 编辑物业公告
	 * @param pmcAnnouncementModelVO
	 * @return
	 */
	BaseVo appEdit(PmcAnnouncementVO pmcAnnouncementModelVO, HttpServletRequest request);

	/**
	 * app分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	BaseVo appListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO,HttpServletRequest request);
	/**
	 * web端分页查询物业公告
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	BaseVo webListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO);
	/**
	 * 返显记录
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);

	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	BaseVo delById(Long id);
}
