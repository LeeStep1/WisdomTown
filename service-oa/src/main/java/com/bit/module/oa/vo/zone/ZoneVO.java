package com.bit.module.oa.vo.zone;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Company
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ZoneVO extends BasePageVo{

	//columns START

    /**
     * id
     */
	private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 状态，0停用 1启用
     */
    private Integer status;

	//columns END

}


