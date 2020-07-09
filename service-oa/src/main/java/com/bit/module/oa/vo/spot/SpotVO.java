package com.bit.module.oa.vo.spot;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Company
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpotVO extends BasePageVo{

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
     * 区域ID
     */
    private Long zoneId;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;

    /**
     * 状态，0停用 1启用
     */
    private Integer status;

	//columns END

}


