package com.bit.common;

/**
 * @Description:  静态类
 * @Author: mifei
 **/
public class BaseConst {

    /*
        限流过期秒数
     */
    public static final int LIMIT_EXPIRE_SECOND = 1;
    /*
        限流key
     */
    public static final String LIMIT_REDIS_KEY_PREFIX = "limit:rate";
    /*
        限流请求个数
     */
    public static final int LIMIT_RATE = 60;


    /**
     * 是否过滤请求,不过滤
     */
    public static final String NO_FILTER  = "1";


    /**
     * 是否过滤请求,过滤
     */
    public static final String FILTER  = "0";

    /**
     * 是否过滤请求
     */
    public static final String USERTOKEN_PREFIX  = "userToken";

    /**
     * 请求头中是否过滤标识的  key
     */
    public static final String FLITER_FLAG  = "requestType";
    /**
     * 志愿者信息管理-志愿者等级审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_LEVEL  = 1;
    /**
     * 志愿者活动管理-活动审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_CAMPAIGN  = 2;
    /**
     * 志愿者服务站管理-共建单位审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_PARTNER_ORG  = 3;
    /**
     * 爱心商家-爱心商家审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP = 4;
    /**
     * 爱心商家-商品兑换审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP_GOOD_EXCHANGE = 5;
    /**
     * 志愿风采-志愿风采审核
     */
    public static final int AUDIT_LOG_TYPE_VOL_NEWS = 6;

    /**
     * 志愿风采-志愿风采发布
     */
    public static final int DEPLOYING_LOG_TYPE_VOL_NEWS = 7;

    /**
     * 志愿风采-志愿风采退回
     */
    public static final int BACK_LOG_TYPE_VOL_NEWS = 8;


}
