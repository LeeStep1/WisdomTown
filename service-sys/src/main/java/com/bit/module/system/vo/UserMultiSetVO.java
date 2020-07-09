package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.bean.Identity;
import com.bit.module.system.bean.PbOrganization;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * User
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMultiSetVO extends BasePageVo{

    /**
     * id
     */
    private List<Long> uids;
    private List<Long> identityIds;
    private List<Long> pbOrgIds;
    private List<Integer> oaOrgIds;
    private List<Integer> appIds;
    private List<Long> cboIds;
    private List<Long> stationIds;

}


