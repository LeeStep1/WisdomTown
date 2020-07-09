package com.bit.common.enumerate;

/**
 * excel导出类型
 */
public enum ExcelExportTypeEnum {

    WEB(0,"镇团委"),
    BASE(1,"基层");

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
    ExcelExportTypeEnum(int code, String info) {
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
