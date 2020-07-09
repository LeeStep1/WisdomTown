package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-29 13:42
 */
@Data
public class VolunteerDict {

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
     * 民族中文
     */
    private String dictPeople;
    /**
     * 教育程度
     */
    private Integer education;
    /**
     * 教育中文
     */
    private String dicteducation;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 健康状况
     */
    private Integer health;
    /**
     * 健康中文
     */
    private String dicthealth;
    /**
     * 婚姻状况
     */
    private Integer marriage;
    /**
     * 婚姻中文
     */
    private String dictmarriage;
    /**
     * 志愿者经验
     */
    private Integer experience;
    /**
     * 经验中文
     */
    private String dictexperience;
    /**
     * 政治面貌
     */
    private Integer political;
    /**
     * 政治中文
     */
    private String dictpolitical;
    /**
     * 志愿服务类别
     */
    private String serviceType;
    /**
     * 服务类别中文
     */
    private String dictservicetype;
    /**
     * 服务时间
     */
    private String serviceTime;
    /**
     * 服务时间中文
     */
    private String dictservicetime;
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
     * 头像id
     */
    private Long volunteerImage;
    /**
     * 志愿等级
     */
    private Integer serviceLevel;
    /**
     * 积分点数
     */
    private BigDecimal point;

    /**
     * 活动时长
     */
    private BigDecimal campaignHour;
    /**
     * 捐款金额
     */
    private BigDecimal donateMoney;
    /**
     * 活动次数
     */
    private Integer campaignCount;
}
