package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 15:07
 */
@Data
public class VolunteerVO extends BasePageVo {
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
     * 站点id
     */
    private Long stationId;
    /**
     * 微信号
     */
    private String weChatId;
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
    private BigDecimal campaignCount;
    /**
     * 活动主题
     */
    private String campaignTheme;
    /**
     * 状态 0-停用 1-启用
     */
    private Integer volunteerStatus;
    /**
     * 同步 0-已同步 1-未同步
     */
    private Integer volunteerSyncho;
    /**
     * 头像id
     */
    private Long volunteerImage;
    /**
     * appid
     */
    private Long appId;

    /**
     * 用户身份ID
     */
    private Long identity;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 志愿等级
     */
    private Integer serviceLevel;
    /**
     * 积分点数
     */
    private BigDecimal point;
    /**
     * 子站点
     */
    private List<Long> childStationList;
    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 锁
     */
    private Integer version;
    /**
     * 旧锁
     */
    private Integer oldversion;
    /**
     * 新锁
     */
    private Integer newversion;
    /**
     * 左侧时长
     */
    private BigDecimal lhour;
    /**
     * 右侧时长
     */
    private BigDecimal rhour;
}
