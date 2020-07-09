package com.bit.module.system.dao;

import com.bit.module.system.bean.Dict;
import com.bit.module.system.bean.Notice;
import com.bit.module.system.bean.NoticePage;
import com.bit.module.system.vo.NoticeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 10:24
 */
@Repository
public interface NoticeDao {

    /**
     * 添加记录
     * @param notice
     */
    void add(Notice notice);

    /**
     * 删除记录
     * @param id
     */
    void delete(@Param(value = "id")Long id);

    /**
     * 更新记录
     * @param notice
     */
    void update(Notice notice);

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    Notice queryById(@Param(value = "id")Long id);

    /**
     * 分页查询
     * @param noticeVO
     * @return
     */
//    List<NoticePage> listPage(NoticeVO noticeVO);

    /**
     * 分页查询
     * @param noticeVO
     * @return
     */
//    List<NoticePage> anlistPage(NoticeVO noticeVO);
    /**
     * 分页查询用户发送的通知公告
     * @param noticeVO
     * @return
     */
    List<NoticePage> userlistPage(NoticeVO noticeVO);

    /**
     * 查询当前用户下的应用
     * @return
     */
    List<Dict> getApp(@Param(value = "userId") Long userId);

    /**
     * 获得最大的公告id
     * @return
     */
//    Long getMaxAnnouncementId();

}
