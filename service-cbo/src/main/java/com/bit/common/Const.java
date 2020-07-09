package com.bit.common;

/**
 * 静态常量类
 */
public class Const {
	/**
	 * 居民的token 类型  人员类型：1社区 2.物业 3居民 0社区办
	 */
	public static final Integer USER_TYPE_RESIDENT = 3;
	public static final Integer USER_TYPE_ORG = 1;
	public static final Integer USER_TYPE_PMC = 2;
	public static final Integer USER_TYPE_ORG_SHE_QU_BAN = 0;

	/**
	 * 角色
	 * 物业类型下： 2维修工 1管理员
	 * 居民类型下：1:居民 2：游客(当房屋认证通过后，将此值改变为1)
	 */
	public static final Integer IDENTITY_PMC_ADMIN = 1;
	public static final Integer IDENTITY_PMC_REPAIR = 2;
	public static final Integer IDENTITY_RESIDENT_RESIDENT = 1;
	public static final Integer IDENTITY_RESIDENT_TOURIST = 2;
	/**
	 * 居民扩展类型字典模块名称
	 */
	public static final String RESIDENT_EXTEND_TYPE = "resident_extend_type";

	/**
	 * TOKEN前缀
	 */
	public static final String TOKEN_PREFIX = "token:";
	/**
	 * 刷新token
	 */
	public static final String REFRESHTOKEN_TOKEN_PREFIX = "refreshToken:";
	/**
	 * 密码盐 （6位）
	 */
	public static final int RANDOM_PASSWORD_SALT = 6;

	/**
	 * 个数
	 */
	public static final Integer COUNT = 0;
	/**
	 * 社区房屋是否验证 0-未验证 1-已验证
	 */
	public static final Integer APP_COMMUNITY_VERIFY_STATUS_YES = 1;
	/**
	 * 社区房屋是否验证 0-未验证 1-已验证
	 */
	public static final Integer APP_COMMUNITY_VERIFY_STATUS_NO = 0;
	/**
	 * 初始版本号
	 */
	public static final Integer INIT_VERSION = 0;
	/**
	 * 是否出租 0-否 1-是
	 */
	public static final Integer IS_RENT_YES = 1;
	/**
	 * 是否出租 0-否 1-是
	 */
	public static final Integer IS_RENT_NO = 0;
	/**
	 * 房屋默认面积
	 */
	public static final String DEFAULT_SQUARE = "0";

	/**
	 * 物业公告状态：1 已发布，0 草稿
	 */
	public static final Integer PMC_ANNOUNCEMENT_STATUS_DRAFT = 0;
	public static final Integer PMC_ANNOUNCEMENT_STATUS_RELEASED = 1;
	/**
	 * 是不是社区办的表示 0-不是 1-是
	 */
	public static final Integer FLAG_SHEQUBAN_NO = 0;
	public static final Integer FLAG_SHEQUBAN_YES = 1;
	/**
	 * 通讯录 类型：1社区，2物业
	 */
	public static final Integer TYPE_PHONE_BOOK_ORG = 1;
	public static final Integer TYPE_PHONE_BOOK_PMC = 2;
	/**
	 * 居民意见 是否已回复,1回复，0未回复
	 */
	public static final Integer SUGGESTION_TYPE_RESPONSED = 1;
	public static final Integer SUGGESTION_TYPE_NOT_RESPONSE = 0;

	/**
	 * 社区公告状态：1 已发布，0 草稿
	 */
	public static final Integer ORG_ANNOUNCEMENT_STATUS_DRAFT = 0;
	public static final Integer ORG_ANNOUNCEMENT_STATUS_RELEASED = 1;

	/**
	 * 社区通知状态：1 已发布，0 草稿
	 */
	public static final Integer ORG_NOTICE_STATUS_DRAFT = 0;
	public static final Integer ORG_NOTICE_STATUS_RELEASED = 1;
	/**
	 * 社区通知是否已读 0 未读 1 已读
	 */
	public static final Integer NOTICE_READ_STATUS_NO = 0;
	public static final Integer NOTICE_READ_STATUS_YES = 1;
	/**
	 * 社区通知是否联系 1联系 0未联系
	 */
	public static final Integer NOTICE_CONNECTION_STATUS_NO = 0;
	public static final Integer NOTICE_CONNECTION_STATUS_YES = 1;

	/**
	 * app居委会端 时间范围 	0-全部 1- 3天内 2- 一周内 3- 一个月内 4- 三个月内
	 */
	public static final Integer NOTICE_APP_FILTER_TIME_LIMIT_NULL = 0;
	public static final Integer NOTICE_APP_FILTER_TIME_LIMIT_THREE_DAYS = 1;
	public static final Integer NOTICE_APP_FILTER_TIME_LIMIT_ONE_WEEK = 2;
	public static final Integer NOTICE_APP_FILTER_TIME_LIMIT_ONE_MONTH = 3;
	public static final Integer NOTICE_APP_FILTER_TIME_LIMIT_THREE_MONTH = 4;
	/**
	 * 默认的数量
	 */
	public static final Integer INIT_TOTAL_NUMBER = 0;

	/**
	 * 办事指南补充业务申请类型
	 */
	public static final String EXTENDTYPE = "cbo_resident_apply_extend_type";

	/**
	 * 居家养老户口类别
	 */
	public static final String RESIDENCE_TYPE ="cbo_apply_base_residence_type";

	/**
	 * 待遇类别
	 */
	public static final String TREATMENT_TYPE = "cbo_apply_base_treatment_type";

	/**
	 * 评估等级
	 */
	public static final String BASE_LEVEL = "cbo_apply_base_apply_base_level";
	/**
	 * 残疾类型
	 */
	public static final String DISABLE_CATEGORY = "cbo_disability_category";

	/**
	 * 残疾等级
	 */
	public static final String DISABLE_LEVEL = "cbo_disability_level";

	/**
	 * 特殊扶助
	 */
	public static final String SPECIAL_SUPPORT = "cbo_special_support_type";
	/**
	 * 生活自理能力
	 */
	public static final String LIVING_ABILITY = "cbo_living_ability";

	/**
	 * 性别 1-男 2-女
	 */
	public static final Integer SEX_MALE = 1;
	public static final Integer SEX_FEMALE = 2;
	/**
	 * 证件类型 1-身份证
	 */
	public static final Integer CARD_NUM_TYPE_SHENFENZHENG = 1;
	/**
	 * 服务名单 是否有效，1：正常，0：已失效
	 */
	public static final Integer STATUS_ACTIVE = 1;
	public static final Integer STATUS_NOT_ACTIVE = 0;
	/**
	 * 是否低保用户：1：是低保  0：不是低保
	 */
	public static final Integer LIVIING_ABLE_YES = 1;
	public static final Integer LIVIING_ABLE_NO = 0;

	/**
	 * 申请记录来源类型 0 - web 1-app
	 */
	public static final Integer LOCATION_APPLY_TYPE_WEB = 0;
	public static final Integer LOCATION_APPLY_TYPE_APP = 1;

	/**
	 * 国籍module
	 */
	public static final String NATIONALITYNAME = "nationality";

	/**
	 * 民族module
	 */
	public static final String ETHNICITYNAME = "people";

	/**
	 * 政治面貌module
	 */
	public static final String POLITICALNAME = "political";

	/**
	 * 特扶module
	 */
	public static final String SPECIALSUPPORTNAME = "cbo_special_support_type";

	/**
	 * 特扶module
	 */
	public static final String LIVINGABILITYNAME = "cbo_living_ability";


}
