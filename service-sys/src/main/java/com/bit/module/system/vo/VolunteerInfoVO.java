package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-05 11:57
 */
@Data
public class VolunteerInfoVO extends BasePageVo{
    /**
     * id
     */
    private Long id;
    /**
     * 编号
     */
    private String serialNumber;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 身份证号
     */
    private String cardId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 民族
     */
    private Integer people;
    /**
     * 教育程度
     */
    private Integer education;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 健康状况
     */
    private Integer health;
    /**
     * 婚姻状况
     */
    private Integer marriage;
    /**
     * 志愿者经验
     */
    private Integer experience;
    /**
     * 政治面貌
     */
    private Integer political;
    /**
     * 志愿服务类别
     */
    private String serviceType;
    /**
     * 服务时间
     */
    private String serviceTime;
    /**
     * 特长爱好
     */
    private String hobby;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 居住地
     */
    private String livingPlace;
    /**
     * 工作学习单位
     */
    private String company;
    /**
     * 活动列表
     */
    private String campaignList;
    /**
     * 站点id
     */
    private Long stationId;
    /**
     * 微信号
     */
    private String weChatId;
}
