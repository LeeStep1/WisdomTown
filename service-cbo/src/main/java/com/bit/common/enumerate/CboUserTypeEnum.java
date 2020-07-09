package com.bit.common.enumerate;

/**
 * 登录用户类型 1社区 2.物业 3居民 0社区办
 */
public enum CboUserTypeEnum {

	/**
	 * 社区
	 */
	CBO_USER_TYPE_ORG(1,"社区"),

	/**
	 * 物业
	 */
	CBO_USER_TYPE_PMC(2,"物业"),

	/**
	 * 居民
	 */
	CBO_USER_TYPE_RESIDENT(3,"居民"),
	/**
	 * 社区办
	 */
	CBO_USER_TYPE_SHEQUBAN(0,"社区办"),
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
	CboUserTypeEnum(int code, String info) {
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
		for(CboUserTypeEnum typeEnum : CboUserTypeEnum.values()){
			if(code==typeEnum.getCode()){
				return typeEnum.getInfo();
			}
		}
		return  null;
	}
}
