package com.bit.module.cbo.bean;

import lombok.Data;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 8:35
 **/
@Data
public class ResidentExtend {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 居民类型id 101-自管党员 102-报道党员 103-居家养老 104-困难户 105-特困户 106-边缘户 107-残疾 108-优抚对象 109-享受低保 110-救助对象 111-矫正人员 112-邢释解救人员 113-精神病人 114-志愿者 115-退役军人
	 */
	private Integer extendType;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 社区id
	 */
	private Long orgId;
}
