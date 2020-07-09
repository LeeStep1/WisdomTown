package com.bit.common.enumerate;

/**
 * 报修 数据流转的的状态
 * 数据流转的的状态     物业：1：待受理, 2：处理中，3：已完结，4：已拒绝  居委会：5：待受理, 6：处理中，7：已完结，8：已拒绝  9：已取消
 */
public enum RepairDataStatusEnum {
	/**
	 * 物业 待受理
	 */
	REPAIR_DATA_STATUS_PMC_NOT_HANDLE(1,"待受理"),
	/**
	 * 物业 处理中
	 */
	REPAIR_DATA_STATUS_PMC_HANDLEING(2,"处理中"),
	/**
	 * 物业 已完结
	 */
	REPAIR_DATA_STATUS_PMC_FINISHED(3,"已完结"),
	/**
	 * 物业 已拒绝
	 */
	REPAIR_DATA_STATUS_PMC_REJECTED(4,"已拒绝"),
	/**
	 * 居委会 待受理
	 */
	REPAIR_DATA_STATUS_ORG_NOT_HANDLE(5,"待受理"),
	/**
	 * 居委会 处理中
	 */
	REPAIR_DATA_STATUS_ORG_HANDLEING(6,"处理中"),
	/**
	 * 居委会 已完结
	 */
	REPAIR_DATA_STATUS_ORG_FINISHED(7,"已完结"),
	/**
	 * 居委会 已拒绝
	 */
	REPAIR_DATA_STATUS_ORG_REJECTED(8,"已拒绝"),
	/**
	 * 居民 已取消
	 */
	REPAIR_DATA_STATUS_RESIDENT_CANCELED(9,"已取消"),


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
	RepairDataStatusEnum(int code, String info) {
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
