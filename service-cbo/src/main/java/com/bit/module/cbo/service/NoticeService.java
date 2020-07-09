package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Notice;
import com.bit.module.cbo.vo.NoticePageVO;
import com.bit.module.cbo.vo.NoticeVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/8 9:02
 **/
public interface NoticeService {

	/**
	 * 新增通知
	 * @param noticeVO
	 * @return
	 */
	BaseVo add(NoticeVO noticeVO);

	/**
	 * 编辑通知
	 * @param noticeVO
	 * @return
	 */
	BaseVo modify(NoticeVO noticeVO);



	/**
	 * web端返显或单查通知
	 * @param id
	 * @return
	 */
	BaseVo webReflectById(Long id);
	/**
	 * app端返显或单查通知 并且 改变已读状态
	 * @param id
	 * @return
	 */
	BaseVo appReflectById(Long id,HttpServletRequest request);

	/**
	 * web端分页查询
	 * @param noticePageVO
	 * @return
	 */
	BaseVo webListPage(NoticePageVO noticePageVO);

	/**
	 * app端分页查询
	 * @param noticePageVO
	 * @return
	 */
	BaseVo appListPage(NoticePageVO noticePageVO, HttpServletRequest request);

	/**
	 * 删除通知
	 * @param id
	 * @return
	 */
	BaseVo delById(Long id);

	/**
	 * 查看已读状态
	 * @param noticePageVO
	 * @return
	 */
	BaseVo checkReadDetail(NoticePageVO noticePageVO);

	/**
	 * 更新联系状态
	 * @param id
	 * @return
	 */
	BaseVo updateConnectionStatus(Long id);

	/**
	 * 校验扩展类型有多少居民
	 * @param extendType
	 * @return
	 */
	BaseVo checkOptionGroupNum(Integer extendType);
}
