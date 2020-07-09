package com.bit.module.oa.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.exception.BusinessException;
import com.bit.utils.CheckUtil;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description : 会议
 * @Date ： 2019/3/1 16:45
 */
@Data
public class Meeting implements Serializable {
    /**
	* 
	*/
    private Long id;

    /**
	* 会议申请单号（按规则生成）
	*/
    private String no;

    /**
	* 会议主题
	*/
    private String title;

    /**
	* 会议室id
	*/
    private Long roomId;

    /**
	* 开始时间
	*/
    private Date startTime;

    /**
	* 结束时间
	*/
    private Date endTime;

    /**
	* 主持人id
	*/
    private Long hostId;

    /**
	* 主持人姓名
	*/
    private String hostName;

    /**
	* 会议纪要员id
	*/
    private Long reporterId;

    /**
	* 会议纪要员姓名
	*/
    private String reporterName;

    /**
	* 审批人id
	*/
    private Long approverId;

    /**
	* 审批人姓名
	*/
    private String approverName;

    /**
	* 附件id集合，英文逗号分隔
	*/
    private String attactIds;

    /**
	* 提醒设置，0无提醒 1提前30分钟 2提前60分钟
	*/
    private Integer remindSet;

    /**
	* 提醒时间
	*/
    private Date remindAt;

    /**
	* 是否需要签到（0：false；1：true）
	*/
    private Boolean needCheckIn;

    /**
     * 签到拍照的url集合，英文逗号隔开
     */
    private String checkInPicUrls;

    /**
     * 线下签到数量
     */
    private Integer checkInOfflineNum;

    /**
	* 发布人id
	*/
    private Long publisherId;

    /**
	* 发布人姓名
	*/
    private String publisherName;

    /**
	* 部门id
	*/
    private Long depId;

    /**
	* 部门名称
	*/
    private String depName;

    /**
	* 创建时间
	*/
    private Date createAt;

    /**
	* 更新时间
	*/
    private Date updateAt;

    /**
	* 发布时间
	*/
    private Date publishAt;

    /**
	* 状态，0待发布 1待审核 2已驳回 3待开始 4进行中 5已取消 6已结束 7 已失效
	*/
    private Integer status;

    /**
	* 与会者json集合，格式[{"id":xx,"name":xxx},{...}]
	*/
    private List<SimpleUser> participants;

    /**
	* 会议内容
	*/
    private String content;

    /**
     * 版本号
     */
    private Integer version;

    public void checkAdd() {
        CheckUtil.notNull(this.getTitle());
        CheckUtil.notNull(this.getRoomId());
        CheckUtil.notNull(this.getHostId());
    }

    public List<SimpleUser> parseParticipants() {
        if (CollectionUtils.isEmpty(this.participants)) {
            return Collections.emptyList();
        }
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(this.participants));
        this.setParticipants(array.toJavaList(SimpleUser.class));
        return this.getParticipants();
    }


    /**
     * 检查会议时间
     * @param publishAt
     */
    public void checkMeetingTime(Date publishAt) {
        // 会议时间检查
        if (this.getStartTime().before(publishAt)) {
            throw new BusinessException("会议开始时间不能早于当前时间");
        }
        if (this.getEndTime().before(this.getStartTime())) {
            throw new BusinessException("会议结束时间不能早于开始时间");
        }
    }
}