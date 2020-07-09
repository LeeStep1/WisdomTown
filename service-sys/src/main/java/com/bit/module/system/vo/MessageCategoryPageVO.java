package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * 
 * 
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 15:21:25
 */
@Data
public class MessageCategoryPageVO extends BasePageVo {

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 类目名称
	 */
	private String categoryName;


}
