package com.bit.common.enumerate;

public enum LocationAddressTypeEnum {

	/**
	 * 楼栋
	 */
	LOCATION_ADDRESS_TYPE_BUILDING(1,"楼栋"),
	/**
	 * 单元
	 */
	LOCATION_ADDRESS_TYPE_UNIT(2,"单元"),
	/**
	 * 楼层
	 */
	LOCATION_ADDRESS_TYPE_FLOOR(3,"楼层"),
	/**
	 * 房屋
	 */
	LOCATION_ADDRESS_TYPE_ROOM(4,"房屋"),
	;

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
	LocationAddressTypeEnum(int code, String info) {
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
