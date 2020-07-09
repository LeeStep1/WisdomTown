package com.bit.common;
/**
 * @Description: 静态变量
 * @Author: liqi
 **/
public class SysConst {

    /**
     *  根节点的PID 根级父亲id
     */
    public static final long ROOT_RESOURCE_PID = 0L;
    /**
     * 重置密码
     */
    public static final String RESET_PASSWORD = "123456";
    /**
     * 密码盐 （6位）
     */
    public static final int RANDOM_PASSWORD_SALT = 6;
    /**
     * 第一个二进制 党组织id
     */
    public static final String BINARY = "0000001000000000000000000000000000000000000000000000000000000000";
    /**
     * 第一个8位--拼装二进制
     */
    public static final String FIRST_EIGHT_BINARY = "0000001";


    public static final String TOKEN_PREFIX = "token:";
    /**
     * 刷新token
     */
    public static final String REFRESHTOKEN_TOKEN_PREFIX = "refreshToken:";
    /**
	 * 社区 修改手机号
	 */
	public static final String CBO_CHANGE_MOBILE = "changeMobile:";

	/**
	 * 社区 修改密码
	 */
	public static final String CBO_CHANGE_PASSWORD = "changePassword:";

    /**
     * 字典redis key 短信验证码
     */
    public static final String REDIS_KEY_SMSCAPTCHA = "smsCaptcha:";

    /**
     * 保存用户默认账号状态
     */
    public static final Integer USER_STATUS = 1;
    /**
     * 表示用户未设置
     */
    public static final Integer USER_STATUS_NO = 3;

    /**
     * 平台创建
     */
    public static final Integer USER_CREATE_TYPE = 1;

    /**
     * app注册
     */
    public static final Integer USER_REGISTER = 2;

    /**
     * 角色设置菜单 1代表已设置
     */
    public static final int ALREADYSET = 1;

    /**
     * 导入用户默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 通知公告状态 0-草稿
     */
    public static final int NOTICE_STATUS_DRAFT = 0;
    /**
     * 通知公告状态 1-已发布
     */
    public static final int NOTICE_STATUS_RELEASED = 1;
    /**
     * 是否已读 0-已读
     */
    public static final int IS_READ_YES = 0;
    /**
     * 是否已读 1-未读
     */
    public static final int IS_READ_NO = 1;

    /**
     * 模块名 平台模块
     */
    public static String MODULE_APP="application";
    /**
     * 模块名 党建模块
     */
    public static String MODULE_PB="category_pb";
    /**
     * 模块名 政务模块
     */
    public static String MODULE_SYS="category_sys";
    /**
     * 模块名 消息类型
     */
    public static String MODULE_MS_TYPE="msg_type";

    /**
     * 用户注册短信
     */
    public static final String USER_SMS_RRGISTER = "register";

    /**
     * 密码找回retrieve
     */
    public static final String RETRIEVE_SMS_PASSWORD = "retrieve";

    /**
     * 验证码
     */
    public static final String VERIFICATION_CODE = "verification";

    /**
     * 每日累计trojan
     */
    public static final String DAY_TOTAL = "total";

    /**
     * REDIS每条短信间隔
     */
    public static final String EACH_SMS_INTERVAL = "90";

    /**
     * 每日短信发送上限
     */
    public static final String DAY_SMS_LIMIT = "86400";

    /**
     * 注册用户短信标识
     */
    public static final int SMS_FOR_REG = 1;

    /**
     * 密码找回短信标识
     */
    public static final int SMS_FOR_RETRIVE = 2;

    /**
     * 用于登录短信标识
     */
    public static final int SMS_FOR_LOGIN = 3;

    /**
     * 验证码失效
     */
    public static final int SMS_VERCODE_LOSE = 50003;

    /**
     * 验证码错误
     */
    public static final int SMS_VERCODE_ERROR = 50004;


    /**
     * 模块名 消息类型
     */
    public static String MESSAGE_TYPE="msg_type";

    /**
     * 消息类型名称
     */
    public static String MESSAGE_TYPE_NAME="msgTypeName";

    /**
     * 消息类目名称
     */
    public static String MESSAGE_CATEGORY_NAME="categoryName";

    /**
     * 默认身份 0非默认1默认
     */
    public static final Integer IDENTITY_ACQUIESCE_DEFAULT=1;
    public static final Integer IDENTITY_ACQUIESCE_NOT_DEFAULT=0;
    /**
     * 登陆用户的类型 0- 管理员 1-党员
     */
    public static final int LOGIN_TYPE_ADMIN = 0;
    public static final int LOGIN_TYPE_DANG = 1;
    /**
     * 用户状态 1表示正常 2表示停用 3未设置
     */
    public static final int USER_STATUS_FUNCTIONAL = 1;
    public static final int USER_STATUS_STOP = 2;
    public static final int USER_STATUS_NOT_SET = 3;
    //党员停用状态
    public static final int PARTY_MEMBER_STATUS_STOP = 2;
    /**
     * 社区办id
     */
	public static final String SHE_QU_BAN_ID = "100113";
    /**
     * 安检 字典表模块值
     */
	public static final String ANJIAN_MODULE_NAME = "dep_id_sv";
    /**
     * 环保 字典表模块值
     */
	public static final String HUANBAO_MODULE_NAME = "dep_id_ep";
	/**
	 * 发送类型 1、注册  2、找回  3、登录  4、社区app修改手机号
	 */
	public static int CBO_OPERATION_FLAG_REGISTER = 1;
	public static int CBO_OPERATION_FLAG_RESET_PASSWORD = 2;
	public static int CBO_OPERATION_FLAG_LOGIN = 3;
	public static int CBO_OPERATION_FLAG_CHANGE_MOBILE = 4;


    /**
     * 居民的token 类型  人员类型：1社区 2.物业 3居民 0社区办
     */
    public static final Integer USER_TYPE_RESIDENT = 3;
    public static final Integer USER_TYPE_ORG = 1;
    public static final Integer USER_TYPE_PMC = 2;
    public static final Integer USER_TYPE_ORG_SHE_QU_BAN = 0;
}
