package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.Notice;
import com.bit.module.cbo.vo.NoticePageVO;
import com.bit.module.cbo.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/8 9:04
 **/
public interface NoticeDao {
	/**
	 * 新增通知
	 * @param notice
	 * @return
	 */
	int add(Notice notice);

	/**
	 * 修改通知
	 * @param notice
	 * @return
	 */
	int modify(Notice notice);

	/**
	 * 单查通知
	 * @param id
	 * @return
	 */
	Notice getNoticeById(@Param(value = "id")Long id);

	/**
	 * web端分页查询
	 * @param noticePageVO
	 * @return
	 */
	List<NoticeVO> webListPage(NoticePageVO noticePageVO);

	/**
	 * app居民端分页查询
	 * @param noticePageVO
	 * @return
	 */
	List<NoticeVO> appResidentListPage(NoticePageVO noticePageVO);

	/**
	 * app居委会端分页查询
	 * @param noticePageVO
	 * @return
	 */
	List<NoticeVO> appOrgListPage(NoticePageVO noticePageVO);

	/**
	 * 根据id删除记录
 	 * @return
	 */
	int delById(@Param(value = "id")Long id);
}
