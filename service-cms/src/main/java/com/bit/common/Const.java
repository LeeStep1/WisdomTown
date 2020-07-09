package com.bit.common;

/**
 * 静态常量类
 * @author mifei
 * @date 2019-05-05
 */
public class Const {

    public static final String TOKEN_PREFIX = "token:";

    /**
     * 刷新token
     */
    public static final String REFRESHTOKEN_TOKEN_PREFIX = "refreshToken:";

    public static final Integer OA_DOOR = 6;

    public static final Integer PB_DOOR = 7;

    /**
     * cms日志表名
     */
    public static final String OPERATIONLOGTABLE = "cmsLog";
    /**
     * 内容未发布
     */
    public static final Integer PUBLISH_STATUS_NO = 0;
    /**
     * 内容已发布
     */
    public static final Integer PUBLISH_STATUS_YES = 1;
    /**
     * 删除状态-正常
     */
    public static final Integer DELETE_STATUS_NO = 0;
    /**
     * 站点状态正常
     */
    public static final Integer STATION_STATUS_NORMAL = 0;
    /**
     * 必填字段
     */
    public static final Integer REQUIRED_FIELDS = 0;
    /**
     * 非必填字段
     */
    public static final Integer NOT_REQUIRED_FIELDS = 1;

    /**
     * 一级栏目ID 位数
     */
    public static final Integer ID_LENGTH = 6;

    /**
     * 层级递增ID相差位数
     */
    public static final Integer INCREMENT_COUNT = 3;

    /**
     * 初始化ID
     */
    public static final String INIT_ID = "101";

    /**
     * 职务ID
     */
    public static final String DUTY_MODULE = "duty_code";

    /**
     * 职务ID
     */
    public static final String ORDER_FIELD = "rank";

    /**
     * 默认排行
     */
    public static final Integer DEFAULT_RANK = 0;

    /**
     * 密码盐 （6位）
     */
    public static final int RANDOM_PASSWORD_SALT = 6;

    /**
     * 重置密码
     */
    public static final String RESET_PASSWORD = "123456";

}
