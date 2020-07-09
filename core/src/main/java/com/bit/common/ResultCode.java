package com.bit.common;

/**
 * @Description:  返回的类型的枚举类
 * @Author: liyujun
 * @Date: 2018-09-17
 **/
public enum ResultCode {

    /**
     *操作成功
     */
    SUCCESS(0, "成功"),

    /**
     *操作失败
     */
    WRONG(1, "操作失败"),

    /**
     *未授权
     */
    UNAUTH(401, "未授权"),

    /**
     *参数错误
     */
    PARAMETER_ERROR(400, "参数错误"),

    /**
     *内部调用RPC返回
     */
    HYSTRIX_TIME_OUT(-1, "未授权"),

    /**
     * 恶意报名三次警告
     */
    WARNING_THREE_TIMES(10,"您已多次取消活动，下次报名将不可取消"),
    /**
     * 超过三次报名警告
     */
    WARNING_OVER_THREE_TIMES(11,"您已多次取消活动，此次活动禁止报名"),
    /**
     * 活动不存在
     */
    WARNING_CAMPAIGN_NOT_EXIST(12,"该活动不存在"),
    /**
     * 活动已开始不能取消报名
     */
    WARNING_CAMPAIGN_IN_PROGRESS_NOT_UNENROLL(13,"活动已开始不能取消报名"),
    /**
     * 活动已开始不能报名
     */
    WARNING_CAMPAIGN_IN_PROGRESS_NOT_ENROLL(14,"活动已开始不能报名"),
    /**
     * 活动已取消
     */
    WARNING_CAMPAIGN_CANCELED(15,"活动已取消"),
    /**
     * 短信发送超过上限
     */
    SMS_OUT_LIMIT(50002,"短信发送超过上限"),

    /**
     * 手机号不存在
     */
    MOBILE_NOT_EXIST(50001,"手机号不存在"),
    /**
     * 手机验证码验证成功
     */
    SMS_CHECKED_SUC(50003,"短信验证码验证成功"),
    /**
     * 手机验证码验证失败
     */
    SMS_CHECKED_FAIL(50004,"短信验证码验证失败"),

    /**
     * 手机验证码验证失败
     */
    SMS_CHECKED_NO_EFFECT(50005,"短信验证码验证失效"),

    /**
     * 审批提示handle
     */
    ADUITS_ALREADY_HANDLE(60001,"该审批已经被处理"),
    /**
     * app党建登录身份没选择
     */
    IDENTITY_NOT_CHOOSE(70001,"请选择登录身份"),
    /**
     * app党建登录手机号格式错误
     */
    MOBILE_FORMAT_WRONG(70002,"请检查手机号格式是否正确"),
    /**
     * app党建登录用户停用
     */
    USER_STATUS_STOP(70003,"用户已停用"),
    /**
     * app党建登录密码错误
     */
    PASSWORD_WRONG(70004,"密码错误"),
    /**
     * app党建登录接入端id为空
     */
    TERMINALID_WRONG(70005,"接入端id不能为空"),
    /**
     * app党建登录不是党员
     */
    NOT_PARTY_MEMBER(70006,"不是党员"),
    /**
     * app党建登录没有授权
     */
    NOT_AUTHORIZE(70007,"没有授权"),
    /**
     * app党建登录用户不存在
     */
    USER_NOT_EXIST(70008,"用户不存在"),
    /**
     * app党建党员注册错误
     */
    IDCARD_FORMAT_WRONG(70009,"身份证格式错误"),
    /**
     * app党建平台身份证验证已存在请直接登录
     */
    SYS_ID_CARD_EXIST(70010,"已注册，请直接登录"),
    /**
     * app党建平台手机号已存在
     */
    MOBILE_ALREADY_EXIST(70011,"手机号已存在"),
    /**
     * app党建平台手机验证码错误
     */
    PB_APP_REGISTER_SMS_CODE_INCORRECT(70012,"短信验证码错误"),

    /**
     * 会议心得文件不匹配
     */
    EXPERIENCE_NOT_MATCH(80001,"会议心得文件不匹配"),
    /**
     * 会议心得党员名称重复
     */
    EXPERIENCE_PB_MEMBER_DUPLICATE(80002,"会议心得党员名称重复"),

    /**
     * 会议心得上传失败
     */
    EXPERIENCE_UPLOAD_FAIL(80003,"会议心得上传失败"),
    /**
     * 会议id不能为空
     */
    EXPERIENCE_CONFERENCE_ID_NULL(80004,"会议id不能为空"),
    /**
     * 会议不存在
     */
    EXPERIENCE_CONFERENCE_NOT_EXIST(80005,"会议不存在"),
    /**
     * 文件名为空
     */
    EXPERIENCE_CONFERENCE_FILE_NAME_NULL(80006,"文件名为空"),
    /**
     * 无党员信息
     */
    EXPERIENCE_CONFERENCE_NO_PARTY_MEMBERS(80007,"无党员信息"),
    /**
     * 文件格式不对
     */
    EXPERIENCE_CONFERENCE_FILE_FORMAT_ERROR(80008,"文件格式不对"),
    /**
     * 文件无后缀名
     */
    EXPERIENCE_CONFERENCE_FILE_SUFFIX_NULL(80009,"文件无后缀名"),
    /**
     * 心得资料不存在
     */
    EXPERIENCE_CONFERENCE_FILE_INFOSUFFIX_NOT_EXIST(80010,"心得资料不存在"),
    /**
     * 党员已停用
     */
    PARTY_MEMBER_STATUS_STOP(80011,"该党员已被停用"),
    /**
     * 会议参会人员为空
     */
    ATTEND_PERSON_NULL(80012,"会议参会人员为空"),

	PASSWORD_IS_NULL(90001,"密码为空,请设置密码"),
    /**
     * 居民在本社区存在
     */
    RESIDENT_EXIST_IN_ORG(100001,"居民在本社区存在"),
    /**
     * 居民在其他社区存在
     */
    RESIDENT_EXIST_IN_OTHER_ORG(100002,"居民在其他社区存在"),
	/**
	 * 居民在任何社区不存在
	 */
	RESIDENT_NOT_EXIST_IN_ANY_ORG(100003,"居民在任何社区不存在"),
	/**
	 * 居民意见该条数据已被删除
	 */
	RECORD_ALREADY_DELETED(100004,"该条数据已被删除"),
    /**
     * 居民类型人数为0
     */
    RESIDNET_EXTEND_TYPE_NUM_ZERO(20001,"此人群类型居民数为0"),


    /**
     * 参数已存在
     */
    PARAMS_KEY_EXIST(4000,"参数已存在"),

    /**
     * 参数不存在
     */
    PARAMS_KEY_NOT_EXIST(4001,"参数不存在"),
    ;






    /**
     * 操作码
     */
    private int code;

    /**
     * 操作信息
     */
    private String info;

    /**
     * @param code  状态码
     * @param info  状态信息
     */
    ResultCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }


    public String getInfo() {
        return info;
    }
}
