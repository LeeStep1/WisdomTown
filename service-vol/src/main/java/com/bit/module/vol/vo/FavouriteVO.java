package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @author chenduo
 * @create 2019-03-08 15:19
 */
@Data
public class FavouriteVO extends BasePageVo{

    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long campaignId;
    /**
     * 志愿者id
     */
    private Long volunteerId;
}
