package com.bit.common.enumerate;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-09-18
 **/
public enum ResidentApplyTypeEumn {

    /**
     * 低保服务
     */
    LIVING_ALLOWANCES(1,"低保","living_allowances"),
    /**
     * 居家养老服务
     */
    HOME_CARE(2,"居家养老","home_care"),
    /**
     * 残疾人服务
     */
    DISABLED_INDIVIDUALS(3,"残疾人","disabled_individuals"),

    /**
     * 特别扶助服务
     */
    SPECIAL_SUPPORT (4,"特别扶助","special_support");

    private int  type;

    private String  name;

    private String table;



    /**
     * @param type  枚举的值
     * @param info  信息
     */
    ResidentApplyTypeEumn(int type, String info, String table) {
        this.type = type;
        this.name = name;
        this.table=table;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }
}
