package com.bit.module.oa.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@Data
public class MeetingSummary implements Serializable {
    /**
	* 
	*/
    private Long id;

    /**
	* 会议id
	*/
    private Long meetingId;

    /**
	* 会议主题
	*/
    private String meetingTitle;

    /**
	* 会议纪要员id
	*/
    private Long reporterId;

    /**
	* 会议纪要员姓名
	*/
    private String reporterName;

    /**
	* 附件id集合，英文逗号分隔
	*/
    private String attactIds;

    /**
	* 创建时间
	*/
    private Date createAt;

    /**
	* 会议纪要内容
	*/
    private String content;
}