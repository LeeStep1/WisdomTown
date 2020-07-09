package com.bit.common.enumerate;

/**
 * 物业员工状态类型枚举类
 * @author chenduo
 * @create 2019-04-11
 */
public enum PmcStaffStatusEnum {
	/**
	 * 停用
	 */
	PMC_STAFF_STATUS_INACTIVE(0,"停用"),

	/**
	 * 启用
	 */
	PMC_STAFF_STATUS_ACTIVE(1,"启用"),

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
	PmcStaffStatusEnum(int code, String info) {
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
