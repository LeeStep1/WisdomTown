package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.NoticeRelResident;
import com.bit.module.cbo.vo.NoticePageVO;
import com.bit.module.cbo.vo.NoticeResidentReadDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/8 13:36
 **/
public interface NoticeRelResidentDao {
	/**
	 * 添加关联关系
	 * @param noticeRelResident
	 * @return
	 */
	int add(NoticeRelResident noticeRelResident);

	/**
	 * 批量添加关联关系
	 * @param noticeRelResidents
	 * @return
	 */
	int batchAddNoticeRelResident(@Param(value = "noticeRelResidents")List<NoticeRelResident> noticeRelResidents);

	/**
	 * 更新关联关系
	 * @param noticeRelResident
	 * @return
	 */
	int update(NoticeRelResident noticeRelResident);

	/**
	 * 根据通知id和居民id查询记录
	 * @param noticeId
	 * @param residentId
	 * @return
	 */
	NoticeRelResident getRecordByNoticeIdAndResidentId(@Param(value = "noticeId")Long noticeId,@Param(value = "residentId")Long residentId);

	/**
	 * 查询已读状态
	 * @param noticePageVO
	 * @return
	 */
	List<NoticeResidentReadDetailVO> checkReadDetail(NoticePageVO noticePageVO);

	/**
	 * 多参数查询
	 * @param noticeRelResident
	 * @return
	 */
	List<NoticeRelResident> findByParam(NoticeRelResident noticeRelResident);

	/**
	 * 根据通知id删除记录
	 * @param noticeId
	 * @return
	 */
	int delByNoticeId(@Param(value = "noticeId")Long noticeId);
}
