package com.bit.soft.push.msEnum;

/**
 * @Description:  静态类
 * @Author: mifei
 **/
public enum MessageTemplateEnum {

    /**
     * 党员新增申请 通过审核 （消息）
     */
    PB_PARTYMEMBER_APPROVAL_ADD_YES(1, "党员新增"),
    /**
     * 党员停用 通过审核 （消息）
     */
    PB_PARTYMEMBER_APPROVAL_DISABLE_YES(47, "党员停用"),
    /**
     * 党员启用 通过审核 （消息）
     */
    PB_PARTYMEMBER_APPROVAL_ENABLED_YES(48, "党员启用"),
    /**
     * 党员组织关系转移 镇内申请 通过审核 （消息）
     */
    PB_PARTYMEMBER_TRANSFER_TOWN_YES(2, "镇内转移"),
    /**
     * 党员组织关系转移 镇外申请 通过审核 （消息）
     */
    PB_PARTYMEMBER_TRANSFER_INTO_YES(49, "镇外转入"),
    /**
     * 党员组织关系转移 转出申请 通过审核 （消息）
     */
    PB_PARTYMEMBER_TRANSFER_OUT_YES(3, "转出镇外"),


    /**
     * 党员新增申请 （待办-a）
     */
    PB_PARTYMEMBER_APPROVAL_ADD_COMMIT(6, "党员新增"),
    /**
     * 党员新增驳回申请 （待办-b）
     */
    PB_PARTYMEMBER_APPROVAL_ADD_REJECT(7, "党员新增"),
    /**
     * 党员停用申请 （待办-a）
     */
    PB_PARTYMEMBER_APPROVAL_DISABLE_COMMIT(8, "党员停用"),
    /**
     * 党员停用驳回 （待办-b）
     */
    PB_PARTYMEMBER_APPROVAL_DISABLE_REJECT(9, "党员停用"),
    /**
     * 党员启用 （待办-a）
     */
    PB_PARTYMEMBER_APPROVAL_ENABLED_COMMIT(50, "党员启用"),
    /**
     * 党员启用 驳回 （待办-b）
     */
    PB_PARTYMEMBER_APPROVAL_ENABLED_REJECT(51, "党员启用"),


    /**
     * 党员 镇内转移 （待办-c）
     */
    PB_PARTYMEMBER_TRANSFER_TOWN_COMMIT(52, "镇内转移"),
    /**
     * 党员 镇内转移 驳回 （待办-d）
     */
    PB_PARTYMEMBER_TRANSFER_TOWN_REJECT(53, "镇内转移"),
    /**
     * 党员 镇外转入 （待办-c）
     */
    PB_PARTYMEMBER_TRANSFER_INTO_COMMIT(54, "镇外转入"),
    /**
     * 党员 镇外转入 驳回 （待办-d）
     */
    PB_PARTYMEMBER_TRANSFER_INTO_REJECT(55, "镇外转入"),
    /**
     * 党员 转出镇外 （待办-c）
     */
    PB_PARTYMEMBER_TRANSFER_OUT_COMMIT(56, "转出镇外"),
    /**
     * 党员 转出镇外 驳回 （待办-d）
     */
    PB_PARTYMEMBER_TRANSFER_OUT_REJECT(57, "转出镇外"),

    /**
     * 消息模板 三会一课
     */
    THREE_LESSION(4,"三会一课"),
    /**
     * 消息模板 学习计划
     */
    STUDY_PLAN(5,"学习计划"),
    /**
     * 消息模板 志愿星级  志愿者满足升级条件（服务时长或者捐款）
     */
    VOL_LEVEL_UP(10,"志愿星级"),
    /**
     * 消息模板 志愿等级审核  镇团委点击通过按钮后
     */
    VOL_LEVEL_AUDIT_PASS_APPLY(11,"志愿等级审核"),
    /**
     * 消息模板 志愿等级审核  等级申请退回至志愿者，服务站退回后
     */
    VOL_LEVEL_AUDIT_REJECTED_APPLY(12,"志愿等级审核"),

    /**
     * 消息模板 活动提醒 发布了新的活动时
     */
    CAMPAIGN_NEW_RELEASE(13,"活动提醒"),
    /**
     * 消息模板 活动提醒 活动开始前2小时
     */
    CAMPAIGN_BEFORE_TWO_HOUR(14,"活动提醒"),
    /**
     * 消息模板 活动提醒 取消活动后
     */
    CAMPAIGN_CANCEL(15,"活动提醒"),

    /**
     * 待办提醒 志愿等级审核  志愿者提交申请
     */
    VOL_LEVEL_AUDIT_APPLY(18,"志愿等级审核"),
    /**
     * 待办提醒 志愿等级审核  镇团委审核退回后
     */
    VOL_LEVEL_AUDIT_REJECTED(19,"志愿等级审核"),
    /**
     * 待办提醒 共建单位审核  共建单位在APP提交申请后
     */
    PARTNER_ORG_APPLY(21,"共建单位审核"),
    /**
     * 待办提醒 志愿活动审核  服务站提交活动后
     */
    CAMPAIGN_RELEASE(20,"志愿活动审核"),

    /**
     * 待办提醒 爱心商家提交申请
     */
    VOL_BENEVOLENCE_SHOP_APPLY(22,"爱心商家提交申请"),
    /**
     * 待办提醒 志愿者提交兑换商品申请
     */
    VOL_VOLUNTEER_PRODUCT_APPLY(23,"志愿者提交兑换商品申请"),

    /**
     * 待办提醒 志愿者提交兑换商品申请
     */
    VOL_VOLUNTEER_NEWS_COMMIT(24,"志愿者风采提交"),
    /**
     * 消息提醒 志愿者活动延迟
     */
    VOL_CAMPAIGN_DELAY(59,"志愿者活动延迟"),
    /**
     * 消息提醒 商品兑换审核通过
     */
    VOL_BENEVOLENCE_SHOP_PRODUCT_YES(16,"商品兑换审核通过"),
    /**
     * 消息提醒 商品兑换申请退回
     */
    VOL_BENEVOLENCE_SHOP_PRODUCT_NO(17,"商品兑换申请退回"),

    /**
     * 用车申请
     */
    VEHICLE_CONFIRM(25, "用车申请"),

    /**
     * 用车申请
     */
    VEHICLE_REJECT(26, "用车申请"),

    /**
     * 异常上报
     */
    EXCEPTION_FEEDBACK(27, "异常上报"),

    /**
     * 隐患上报
     */
    RISK_FEEDBACK(28, "隐患上报"),

    /**
     * 补卡申请
     */
    INSPECT_APPLICATION_AUDIT(29, "补卡申请"),

    /**
     * 补卡申请
     */
    INSPECT_APPLICATION_REJECT(30, "补卡申请"),

    /**
     * 会议申请
     */
    MEETING_AUDIT(31, "会议申请"),

    /**
     * 会议申请
     */
    MEETING_REJECT(32, "会议申请"),

    /**
     * 任务分派
     */
    TASK_CREATE_MESSAGE(33, "任务分派"),

    /**
     * 任务分派
     */
    TASK_ASSIGN_MESSAGE(34, "任务分派"),

    /**
     * 任务反馈
     */
    TASK_FEEDBACK(35, "任务反馈"),

    /**
     * 任务终止
     */
    TASK_SUPERIOR_TERMINATION(36, "任务终止"),

    /**
     * 任务终止
     */
    TASK_CHILD_TERMINATION(37, "任务终止"),

    /**
     * 用车申请
     */
    VEHICLE_APPLY(38, "用车申请"),

    /**
     * 待办公文
     */
    OFFICIAL_DOC_SEND(39, "待办公文"),

    /**
     * 巡检任务
     */
    INSPECT_PUBLISH(40, "巡检任务"),

    /**
     * 异常上报
     */
    EXCEPTION_SUBMIT(41, "异常上报"),

    /**
     * 隐患上报
     */
    RISK_SUBMIT(42, "隐患上报"),

    /**
     * 补卡申请
     */
    INSPECT_APPLICATION_APPLY(43, "补卡申请"),

    /**
     * 任务分派
     */
    TASK_CREATE(44, "任务分派"),

    /**
     * 任务分派
     */
    TASK_ASSIGN(45, "任务分派"),

    /**
     * 会议申请
     */
    MEETING_APPLY(46, "会议申请"),

    /**
     * 会议取消
     */
    MEETING_CANCEL(60,"会议取消"),

    /**
     * 分派安监巡检任务
     */
    SV_TASK_ASSIGN(61,"新增任务提醒"),

    /**
     * 分派安监复查任务
     */
    SV_REVIEW_TASK_ASSIGN(62,"新增复查提醒"),

    /**
     * 安监巡检任务到期
     */
    SV_TASK_EXPIRE(63,"任务到期提醒"),

    /**
     * 安监复查任务到期
     */
    SV_REVIEW_TASK_EXPIRE(64,"复查到期提醒"),

    /**
     * 分派环保巡检任务
     */
    EP_TASK_ASSIGN(65,"新增任务提醒"),

    /**
     * 分派环保复查任务
     */
    EP_REVIEW_TASK_ASSIGN(66,"新增复查提醒"),

    /**
     * 环保巡检任务到期
     */
    EP_TASK_EXPIRE(67,"任务到期提醒"),

    /**
     * 环保复查任务到期
     */
    EP_REVIEW_TASK_EXPIRE(68,"复查到期提醒"),

    /**
     * 分派城建巡检任务
     */
    UC_TASK_ASSIGN(69,"新增任务提醒"),

    /**
     * 分派城建复查任务
     */
    UC_REVIEW_TASK_ASSIGN(70,"新增复查提醒"),

    /**
     * 城建巡检任务到期
     */
    UC_TASK_EXPIRE(71,"任务到期提醒"),

    /**
     * 城建复查任务到期
     */
    UC_REVIEW_TASK_EXPIRE(72,"复查到期提醒"),


    /**
     *模块：社区
     *
     * 居民信息管理
     */
    CBO_REMIND_LOCATION_APPLY_PASS(73,"房屋认证审核通过"),

    /**
     * 模块：社区
     * 居民信息管理
     */
    CBO_REMIND_LOCATION_APPLY_REFUSE(74,"房屋认证审核被拒绝"),

    /**
     * 模块：社区
     * 通知公告管理
     */
    CBO_REMIND_NOTICE_ORG_ADMIN(75,"社区管理员发布通知"),

    /**
     * 模块：社区
     * 社区管理员发布公告
     */
    CBO_REMIND_ANNOUNCEMENT_ORG_ADMIN(76,"社区管理员发布公告"),
    /**
     * 模块：社区
     * 物业管理员发布公告
     */
    CBO_REMIND_ANNOUNCEMENT_PMC(77,"物业管理员发布公告"),
    /**
     * 模块：社区
     * 居民办事管理-办事台账管理
     * 办事流程任一节点反馈被终止
     */
    CBO_REMIND_RESIDENT_APPLY_TERMINALTION(78,"办事流程任一节点反馈被终止"),
    /**
     * 模块：社区
     * 居民办事管理-办事台账管理
     * 办事流程所有节点反馈全部通过
     */
    CBO_REMIND_RESIDENT_APPLY_PASS(79,"办事流程所有节点反馈全部通过"),
    /**
     * 模块：社区
     * 故障报修-居民报修管理
     * 报修被物业拒绝
     * 报修被居委会拒绝
     */
    CBO_REMIND_RESIDENT_REPAIR_REFUSE(80,"报修被物业拒绝,报修被居委会拒绝"),
    /**
     * 模块：社区
     * 故障报修-居民报修管理
     * 故障报修物业处理完毕，反馈后
     * 故障报修居委会处理完毕，反馈后
     */
    CBO_REMIND_RESIDENT_REPAIR_PROCESSING_COMPLETION(81,"故障报修物业处理完毕或故障报修居委会处理完毕"),
    /**
     * 模块：社区
     * 居民意见-意见箱管理
     * 意见箱管理
     * 居民提交意见居委会反馈
     */
    CBO_REMIND_RESIDENT_SUGGESTION(82,"居民提交意见居委会反馈"),
    /**
     * 模块：社区
     * 房屋认证-居民信息管理
     * 认证审核-居民APP提交房屋认证申请
     */
    CBO_TASK_RESIDENT_LOCATION_APPLY(83,"居民APP提交房屋认证申请"),
    /**
     * 模块：社区
     * 故障报修-居民报修管理
     * 居民报修管理-居民提交故障报修或居委会将故障退回至物业,
     */
    CBO_TASK_RESIDENT_REPAIR_APPLY_RETURN(84,"居民提交故障报修或居委会将故障退回至物业"),
    /**
     * 模块：社区
     * 房屋认证-居民报修管理
     * 居民报修管理-物业将故障提交给居委会
     */
    CBO_TASK_RESIDENT_REPAIR_APPLY_TRANSFER(85,"物业将故障提交给居委会"),

    /**
     * 模块：社区
     * 居民意见-意见箱管理
     * 意见箱管理-居民通过APP提交意见至居委会
     */
    CBO_TASK_RESIDENT_LOCATION(86,"报修被物业拒绝,报修被居委会拒绝"),

    /**
     * 模块：社区
     * 文明上报-文明行为上报管理
     * 文明行为上报-居委会对提交的文明上报反馈后(居民APP)
     */
    CBO_REMIND_CIVILIZED_BEHAVIOR_TO_RESIDENT(87,"居委会对提交的文明上报反馈(居民APP)"),
    /**
     * 模块：社区
     * 文明上报-文明行为上报管理
     * 文明行为上报-居委会对提交的文明上报反馈后(物业APP)
     */
    CBO_REMIND_CIVILIZED_BEHAVIOR_TO_PMC(88,"居委会对提交的文明上报反馈(物业APP)"),
    /**
     * 模块：社区
     * 文明上报-文明行为上报管理
     * 文明行为上报-居民或者物业通过APP提交文明行为上报
     */
    CBO_TASK_CIVILIZED_BEHAVIOR(89,"居民或者物业通过APP提交文明行为上报");
    /**
     * 模板id
     */
    private final int id;

    /**
     * 模板名称
     */
    private String info;

    /**
     * @param id  状态信息
     */
    MessageTemplateEnum(int id,String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }


}
