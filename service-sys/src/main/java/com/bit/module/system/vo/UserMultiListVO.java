package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * User
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMultiListVO extends BasePageVo{

    /**
     * 用户id
     */
    private List<Long> uids;
    /**
     * 应用id
     */
    private Integer appId;

}


