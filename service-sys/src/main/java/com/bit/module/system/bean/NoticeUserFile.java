package com.bit.module.system.bean;

import lombok.Data;


import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 13:20
 */
@Data
public class NoticeUserFile {

    /**
     * 通知公告
     */
    private Notice notice;
    /**
     * 通知公告 接收人群
     */
//    private List<User> userList;
    /**
     * 通知公告 附件列表
     */
    private List<FileInfo> fileInfoList;
}
