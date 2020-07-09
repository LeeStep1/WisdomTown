package com.bit.common.enumerate;

/**
 * 报修操作类型 1-居民提交 2-居民取消报修 3-物业受理 4-物业移交居委会
 * 5-物业拒绝 6-物业完结 7-居委会受理 8-居委会退回物业
 * 9-居委会拒绝 10-居委会完结
 */
public enum OpearationTypeEnum {

	/**
	 * 居民提交
	 */
	OPEARATION_TYPE_RESIDENT_SUBMIT(1,"居民提交"),

	/**
	 * 居民取消报修
	 */
	OPEARATION_TYPE_RESIDENT_CANCEL(2,"居民取消报修"),

	/**
	 * 物业受理
	 */
	OPEARATION_TYPE_PMC_CONFIRM(3,"物业受理"),

	/**
	 * 物业移交居委会
	 */
	OPEARATION_TYPE_PMC_TRANSFER_TO_ORG(4,"物业移交居委会"),
	/**
	 * 物业拒绝
	 */
	OPEARATION_TYPE_PMC_REJECT(5,"物业拒绝"),

	/**
	 * 物业完结
	 */
	OPEARATION_TYPE_PMC_FINISH(6,"物业完结"),
	/**
	 * 居委会受理
	 */
	OPEARATION_TYPE_ORG_CONFIRM(7,"居委会受理"),
	/**
	 * 居委会退回物业
	 */
	OPEARATION_TYPE_ORG_TRANSFER_TO_PMC(8,"居委会退回物业"),
	/**
	 * 居委会拒绝
	 */
	OPEARATION_TYPE_ORG_REJECT(9,"居委会拒绝"),

	/**
	 * 居委会完结
	 */
	OPEARATION_TYPE_ORG_FINISH(10,"居委会完结"),


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
	OpearationTypeEnum(int code, String info) {
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
