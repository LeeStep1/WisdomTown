package com.bit.common.enumerate;

/**
 * 居民扩展信息类型枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum ResidentExtendTypeEnum {
    /**
     * 自管党员
     */
    RESIDENT_TYPE_SELF_CONTROLE_PARTY_MEMBER(101,"自管党员"),

    /**
     * 报道党员
     */
    RESIDENT_TYPE_REGISTERED_PARTY_MEMBER(102,"报道党员"),

    /**
     * 居家养老
     */
    RESIDENT_TYPE_OLD(103,"居家养老"),

    /**
     * 困难户
     */
    RESIDENT_TYPE_HARD(104,"困难户"),

    /**
     * 特困户
     */
    RESIDENT_TYPE_VERY_HARD(105,"特困户"),
    /**
     * 边缘户
     */
    RESIDENT_TYPE_EDGE(106,"边缘户"),
    /**
     * 残疾
     */
    RESIDENT_TYPE_DISABLE(107,"残疾"),
    /**
     * 优抚对象
     */
    RESIDENT_TYPE_PRIORITY_LOVE(108,"优抚对象"),
    /**
     * 享受低保
     */
    RESIDENT_TYPE_ENJOY_LOW_FUND(109,"享受低保"),
    /**
     * 救助对象
     */
    RESIDENT_TYPE_HELP_OBJECT(110,"救助对象"),
    /**
     * 矫正人员
     */
    RESIDENT_TYPE_CORRECTION_OBJECT(111,"矫正人员"),
    /**
     * 邢释解救人员
     */
    RESIDENT_TYPE_FORMER_PRISONER(112,"邢释解救人员"),
    /**
     * 精神病人
     */
    RESIDENT_TYPE_PSYCHOTIC_PATIENT(113,"精神病人"),
    /**
     * 志愿者
     */
    RESIDENT_TYPE_VOLUNTEER(114,"志愿者"),
    /**
     * 退役军人
     */
    RESIDENT_TYPE_EX_SERVICE_MAN(115,"退役军人"),

    ;


    /**
     * 枚举的值
     */
    private int code;

    /**
     * 信息
     */
    private String info;

    /**
     * @param code  枚举的值
     * @param info  信息
     */
    ResidentExtendTypeEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }


    public String getInfo() {
        return info;
    }

    /**
     * 根据code得到info
     * @param code
     * @return
     */
    public static String getValueByCode(int code){
        for(ResidentExtendTypeEnum typeEnum : ResidentExtendTypeEnum.values()){
            if(code==typeEnum.getCode()){
                return typeEnum.getInfo();
            }
        }
        return  null;
    }
}
