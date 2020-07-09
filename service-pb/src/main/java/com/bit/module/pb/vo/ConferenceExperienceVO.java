package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-17 18:57
 */
@Data
public class ConferenceExperienceVO extends BasePageVo{

    /**
     * id
     */
    private Long id;
    /**
     * 党员id
     */
    private Long userId;
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 上传状态 0-未上传 1-已上传
     */
    private Integer status;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 上传率
     */
    private String uploadRate;
    /**
     * 党组织id
     */
    private Long orgId;
    /**
     * 会议开始时间
     */
    private Date startTime;
    /**
     * 参会人员ids
     */
    private List<Long> personIds;
}
