package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Notice;
import com.bit.module.system.bean.OrgAndName;
import com.bit.module.system.vo.NoticeVO;

/**
 * @author chenduo
 * @create 2019-02-14 10:20
 */
public interface NoticeService {
    /**
     * 添加通知公告 或 直接发布
     * @param notice
     * @return
     */
    BaseVo add(Notice notice);
    /**
     * 修改通知公告 或 发布
     * @param notice
     * @return
     */
    BaseVo update(Notice notice);
    /**
     * 删除通知公告
     * @param id
     * @return
     */
    BaseVo delete(Long id);
    /**
     * 通知管理 公告管理 的 反显通知公告
     * @param id
     * @return
     */
    BaseVo reflectById(Long id);
    /**
     * 反显通知公告 不做状态改变
     * @param id
     * @return
     */
    BaseVo queryById(Long id);
    /**
     * 分页查询通知 公告 待办 已办 消息
     * @param noticeVO
     * @return
     */
    BaseVo listPage(NoticeVO noticeVO);
    /**
     * 分页查询通知公告 这个用没用
     * @param noticeVO
     * @return
     */
//    BaseVo anlistPage(NoticeVO noticeVO);
    /**
     * 分页查询用户发送的通知公告
     * @param noticeVO
     * @return
     */
    BaseVo userlistPage(NoticeVO noticeVO);
    /**
     * 查询当前用户下的应用
     * @return
     */
    BaseVo getApp();
    /**
     * 根据应用查询组织结构
     * @param appId
     * @return
     */
    BaseVo queryOrgByAppId(Long appId);
    /**
     * 根据应用和用户查询组织结构
     * @param appId
     * @return
     */
    BaseVo queryOrgByAppIdUser(Long appId);
    /**
     * 根据应用 组织id 查询人员
     * @param appId
     * @param orgId
     * @return
     */
    BaseVo queryUserByAppId(Long appId,Long orgId);

    /**
     * 根据通知公告类型更改已读状态
     * @param noticeVO
     * @return
     */
    BaseVo readAll(NoticeVO noticeVO);

//    BaseVo getMaxId(Long userId);


    /**
     * 根据应用 组织id 姓名 查询人员
     * @param orgAndName
     * @return
     */
    BaseVo queryUserByAppIdOrgIdsName(OrgAndName orgAndName);
    /**
     * 铃铛的查询
     * @return
     */
    BaseVo listPageForBell();

    /**
     * 更新mongo的msgtype
     * @param mongoId
     * @return
     */
    BaseVo updateMsgType(String mongoId);
    /**
     * 更新mongo的status
     * @param mongoId
     * @return
     */
    BaseVo updateMongoMsgStatus(String mongoId);
    /**
     * 更新mongo的status
     * @param mongoId
     * @return
     */
    BaseVo deleteMongoMessage(String mongoId);
}