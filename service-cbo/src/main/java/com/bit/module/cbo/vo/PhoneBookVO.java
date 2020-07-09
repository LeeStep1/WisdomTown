package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 14:41
 **/
@Data
public class PhoneBookVO extends BasePageVo{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 类型：1社区，2物业
	 */
	private Integer type;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 电话1
	 */
	private String telOne;
	/**
	 * 电话2
	 */
	private String telTwo;
	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
}
