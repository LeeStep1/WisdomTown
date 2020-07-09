package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.Announcement;
import com.bit.module.cbo.vo.AnnouncementPageVO;
import com.bit.module.cbo.vo.AnnouncementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/7 13:56
 **/
public interface AnnouncementDao {
	/**
	 * 新增社区公告
	 * @param announcement
	 * @return
	 */
	int add(Announcement announcement);

	/**
	 * 修改社区公告
	 * @param announcement
	 * @return
	 */
	int modify(Announcement announcement);

	/**
	 * 发布公告
	 * @param id
	 * @return
	 */
	Announcement getAnnouncementById(@Param(value = "id") Long id);

	/**
	 * web端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	List<AnnouncementVO> webListPage(AnnouncementPageVO announcementPageVO);

	/**
	 * app端分页查询
	 * @param announcementPageVO
	 * @return
	 */
	List<AnnouncementVO> appListPage(AnnouncementPageVO announcementPageVO);

	/**
	 * 删除社区公告
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id")Long id);
}
