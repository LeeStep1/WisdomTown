package com.bit.common;

import java.math.BigDecimal;

/**
 * 静态常量类
 * @author chenduo
 * @date 2018-1-11
 */
public class Const {


    /**
     * 活动范围 0-站外 1-站内
     */
    public static final Integer CAMPAIGN_SCALE_IN = 1;
    public static final Integer CAMPAIGN_SCALE_OUT = 0;
    /**
     * 活动报名 0-未满  1-已满
     */
    public static final Integer CAMPAIGN_FULL_YES = 1;
    public static final Integer CAMPAIGN_FULL_NO = 0;
    /**
     * 志愿者报名类型 0-没报名 1-报名
     */
    public static final Integer VOLUNTEER_STATUS_ENROLL = 1;
    public static final Integer VOLUNTEER_STATUS_NOT_ENROLL = 0;
    /**
     * 初始人数
     */
    public static final Integer INIT_NUMBER=0;
    /**
     * 初始活动次数
     */
    public static final Integer INIT_CAMPAIGN_NUMBER=0;
    /**
     * 初始服务次数
     */
    public static final Integer INIT_COUNT=0;
    /**
     * 初始服务等级
     */
    public static final Integer INIT_LEVEL=0;
    /**
     * 初始捐款金额
     */
    public static final BigDecimal INIT_MONEY = new BigDecimal(0);
    /**
     * 初始服务小时
     */
    public static final BigDecimal INIT_HOUR = new BigDecimal(0);
    /**
     * 初始积分
     */
    public static final BigDecimal INIT_POINT = new BigDecimal(0);
    /**
     * 初始版本号
     */
    public static final Integer INIT_VERSION = 0;
    /**
     * 初始头像号
     */
    public static final Long INIT_VOLUNTEER_IMAGE = 1L;
    /**
     * 站点状态 0-停用 1-启用
     */
    public static final Integer STATION_STATUS_ACTIVE = 1;
    public static final Integer STATION_STATUS_INACTIVE = 0;
    /**
     * 志愿者状态 状态 0-停用 1-启用
     */
    public static final Integer VOLUNTEER_STATUS_ACTIVE = 1;
    public static final Integer VOLUNTEER_STATUS_INACTIVE = 0;
    /**
     * 志愿者同步 状态 0-未同步 1-已同步
     */
    public static final Integer VOLUNTEER_SYNCHO_YES = 1;
    public static final Integer VOLUNTEER_SYNCHO_NO = 0;
    /**
     * 志愿者活动签到 状态 0-未签到 1-已签到
     */
    public static final Integer CAMPAIGN_RECORD_SIGN_STATUS_YES = 1;
    public static final Integer CAMPAIGN_RECORD_SIGN_STATUS_NO = 0;
    /**
     * 志愿者活动记录状态 0-停用 1-启用
     */
    public static final Integer CAMPAIGN_RECORD_STATUS_ACTIVE = 1;
    public static final Integer CAMPAIGN_RECORD_STATUS_INACTIVE = 0;
    /**
     * 每个机构下第一个机构后缀
     */
    public static final String STATION_FIRST_CODE="101";
    /**
     * 活动初始版本
     */
    public static final Integer CAMPAIGN_VERSION = 0;

    /**
     * 共建单位 0-不是 1-是
     */
    public static final Integer PARTENER_ORG_TYPE_NO = 0;
    public static final Integer PARTENER_ORG_TYPE_YES = 1;
    /**
     * 活动是否收藏 0-否 1-是
     */
    public static final Integer CAMPAIGN_FAVOURITE_NO = 0;
    public static final Integer CAMPAIGN_FAVOURITE_YES = 1;
    /**
     *  志愿者审批状态 状态 0-已通过 1-草稿 2-待发布 3-已拒绝 4-审核中
     */
    public static final Integer NEWS_AUDIT_STATUS_PASSED = 0;
    public static final Integer NEWS_AUDIT_STATUS_DRAFT = 1;
    public static final Integer NEWS_AUDIT_STATUS_DEPLOYING = 2;
    public static final Integer NEWS_AUDIT_STATUS_REJECTED = 3;
    public static final Integer NEWS_AUDIT_STATUS_CHECK_PEDING = 4;
    /**
     * 志愿者风采保存
     */
    public static final String NEWS_SAVE = "1";

    /**
     * 志愿者风采提交
     */
    public static final String NEWS_COMMIT = "2";

    /**
     * 志愿者风采状态查询默认值
     */
    public static final int DEFAULT_STATUS = 4;

    /**
     * 文章状态未删除
     */
    public static final int NEWS_STATUS_NOT_DEL = 0;

    /**
     * 文章状态已删除
     */
    public static final int NEWS_STATUS_DEL = 1;



    public static final Integer CAMPAIGN_UNENROLL_TIMES = 3;


    /**
     * 导入金额成功 或 失败
     */
    public static final String IMPORT_MONEY_SUCCESS = "成功";
    public static final String IMPORT_MONEY_FAIL = "失败";
    /**
     * 导入金额失败原因
     */
    public static final String IMPORT_MONEY_FAIL_REASON_IDCARD_NULL = "错误:身份证不能为空";
    public static final String IMPORT_MONEY_FAIL_REASON_IDCARD_FORMAT_WRONG = "错误:身份证格式错误";
    public static final String IMPORT_MONEY_FAIL_REASON_MONEY_NULL = "错误:捐款金额不能为空";
    public static final String IMPORT_MONEY_FAIL_REASON_MONEY_FORMAT_WRONG = "错误:捐款金额格式不对";
}
