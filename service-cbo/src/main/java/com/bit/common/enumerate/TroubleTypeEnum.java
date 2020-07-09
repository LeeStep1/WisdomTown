package com.bit.common.enumerate;

/**
 * 故障类型 1-公共电梯故障 2-公共门禁故障 3-公共其他故障 4-住户安防故障 5-住户其他故障
 */
public enum TroubleTypeEnum {

	/**
	 * 公共电梯故障
	 */
	TROUBLE_TYPE_PUBLIC_ELEVATOR(1,"公共电梯故障"),
	/**
	 * 公共门禁故障
	 */
	TROUBLE_TYPE_PUBLIC_ENTRANCE_GUARD(2,"公共门禁故障"),
	/**
	 * 公共其他故障
	 */
	TROUBLE_TYPE_PUBLIC_OTHER(3,"公共其他故障"),
	/**
	 * 住户安防故障
	 */
	TROUBLE_TYPE_RESIDENT_SECURITY(4,"住户安防故障"),
	/**
	 * 住户其他故障
	 */
	TROUBLE_TYPE_RESIDENT_OTHER(5,"住户其他故障"),


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
	TroubleTypeEnum(int code, String info) {
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
		for(TroubleTypeEnum typeEnum : TroubleTypeEnum.values()){
			if(code==typeEnum.getCode()){
				return typeEnum.getInfo();
			}
		}
		return  null;
	}
}
