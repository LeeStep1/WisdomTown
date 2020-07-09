package com.bit.common.enumerate;

public enum StationTypeEnum {

    /**
     * 服务站类型 0-默认类型 1-镇团委 2-村服务站 3-社区服务站 4-企业服务站 5-共建单位服务站
     */
    STATION_TYPE_DEFAULT(0,"默认类型"),
    STATION_TYPE_ZHENTUANWEI(1,"Z"),
    STATION_TYPE_CUN(2,"C"),
    STATION_TYPE_SHEQU(3,"S"),
    STATION_TYPE_QIYE(4,"Q"),
    STATION_TYPE_GONGJIAN(5,"G");


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
    StationTypeEnum(int code, String info) {
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
