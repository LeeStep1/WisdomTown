package com.bit.common.enumerate;

/**
 * 爱心商家枚举类
 * @author liuyancheng
 * @create 2019-04-03 8:59
 */
public enum BenevolenceShopEnum {
    //爱心商家状态，0-停用 1-启用
    BENEVOLENCE_SHOP_ENABLE_NO(0, "停用"),
    BENEVOLENCE_SHOP_ENABLE_YES(1,"启用"),
    //爱心商家审核状态 0待审核，1已通过，2：已退回
    BENEVOLENCE_SHOP_AUDIT_TOAUDIT(0, "待审核"),
    BENEVOLENCE_SHOP_AUDIT_PASSED(1, "已通过"),
    BENEVOLENCE_SHOP_AUDIT_BACK(2, "已退回"),
    //商品状态:0:下架，1上架
    BENEVOLENCE_SHOP_PRODUCT_STATE_NO(0, "下架"),
    BENEVOLENCE_SHOP_PRODUCT_STATE_YES(1, "上架"),
    //爱心商家商品兑换审核状态 0待审核，1已通过，2：已退回
    BENEVOLENCE_SHOP_PRODUCT_TOAUDIT(0, "待审核"),
    BENEVOLENCE_SHOP_PRODUCT_PASSED(1, "已通过"),
    BENEVOLENCE_SHOP_PRODUCT_BACK(2, "已退回"),
    //商品兑换 领取状态:1领取，0未领取
    BENEVOLENCE_SHOP_PRODUCT_AUDIT_YES(1, "领取"),
    BENEVOLENCE_SHOP_PRODUCT_AUDIT_NO(0, "未领取");

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
    BenevolenceShopEnum(int code, String info) {
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
