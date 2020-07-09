package com.bit.common.enumerate;

/**
 * 报修 居民申请时的数据状态
 */
public enum RepairApplyStatusEnum {
	/**
	 * 待受理
	 */
	REPAIR_APPLY_STATUS_NOT_HANDLE(1,"待受理"),
	/**
	 * 处理中
	 */
	REPAIR_APPLY_STATUS_HANDLEING(2,"处理中"),
	/**
	 * 已完结
	 */
	REPAIR_APPLY_STATUS_FINISHED(3,"已完结"),
	/**
	 * 已拒绝
	 */
	REPAIR_APPLY_STATUS_REJECTED(4,"已拒绝"),
	/**
	 * 已取消
	 */
	REPAIR_APPLY_STATUS_CANCELED(5,"已取消"),

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
	RepairApplyStatusEnum(int code, String info) {
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
