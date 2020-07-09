package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.PmcAnnouncement;
import com.bit.module.cbo.vo.PmcAnnouncementPageVO;
import com.bit.module.cbo.vo.PmcAnnouncementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 10:23
 **/
public interface PmcAnnouncementDao {
	/**
	 * 新增物业公告
	 * @param pmcAnnouncement
	 * @return
	 */
	int add(PmcAnnouncement pmcAnnouncement);

	/**
	 * 更新物业公告
	 * @param pmcAnnouncement
	 * @return
	 */
	int modify(PmcAnnouncement pmcAnnouncement);

	/**
	 * 根据id删除记录
	 * @param id
	 * @return
	 */
	int delete(@Param(value = "id")Long id);

	/**
	 * 根据id查询物业公告
	 * @param id
	 * @return
	 */
	PmcAnnouncement getPmcAnnouncementById(@Param(value = "id")Long id);

	/**
	 * app端分页查询
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	List<PmcAnnouncementVO> appListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO);

	/**
	 * web端分页查询
	 * @param pmcAnnouncementPageVO
	 * @return
	 */
	List<PmcAnnouncementVO> webListPage(PmcAnnouncementPageVO pmcAnnouncementPageVO);
}
