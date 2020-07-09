package com.bit.common;

/**
 * @author chenduo
 * @create 2019-03-21 17:11
 */
public class AuditMessageTemplate {

    public static final String OPERATION_NAME_STATION = "服务站审核志愿等级记录";

    public static final String OPERATION_NAME_ZHENTUANWEI = "镇团委审核志愿等级记录";

    public static final String OPERATION_NAME_PARTNER_ORG_AUDIT = "审核共建单位";

    public static final String OPERATION_NAME_LEVEL_ADD = "志愿者登记申请提交";

    public static final String AUDIT_LEVEL_PASSED_STATION = "服务站审核通过！";

    public static final String AUDIT_LEVEL_PASSED_ZHENTUANWEI = "镇团委审核通过！";

    public static final String AUDIT_LEVEL_REJECTED_STATION = "服务站审核退回，退回原因：";

    public static final String AUDIT_LEVEL_REJECTED_ZHENTUANWEI = "镇团委审核退回，退回原因：";

    public static final String AUDIT_PARTNER_ORG_PASSED_ZHENTUANWEI = "您提交的共建单位申请审核已通过!";

    public static final String AUDIT_PARTNER_ORG_REJECTED_ZHENTUANWEI = "您提交的共建单位申请被驳回，驳回原因：";

    public static final String AUDIT_VOLUNTEER_NEW_REJECTED_ZHENTUANWEI = "您提交的志愿者风采申请被驳回，驳回原因：";

    public static final String AUDIT_VOLUNTEER_NEW_PASSED_ZHENTUANWEI = "您提交的志愿者风采申请审核已通过!";

    //---- 爱心商家 ----
    public static final String AUDIT_BENEVOLENCE_SHOP = "您提交的爱心商家申请审核已通过!";

    public static final String AUDIT_BENEVOLENCE_SHOP_REJECT = "您提交的爱心商家申请被驳回，驳回原因：";

    public static final String AUDIT_BENEVOLENCE_SHOP_PRODUCT = "您提交的商品兑换申请审核已通过，请联系所属服务站或镇团委进行商品兑换！";

    public static final String AUDIT_BENEVOLENCE_SHOP_PRODUCT_REJECT = "您提交的爱心商家商品兑换申请被驳回，驳回原因：";
}
