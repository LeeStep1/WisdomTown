package com.bit.module.vol.dao;

import com.bit.common.enumerate.PartnerAuditEnum;
import com.bit.module.vol.bean.PartnerOrg;
import com.bit.module.vol.vo.PartnerOrgVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.bit.common.enumerate.PartnerAuditEnum.PARTENER_AUDIT_STATE_WAIT_VERIFY;

/**
 * @author chenduo
 * @create 2019-03-19 13:32
 */
@Component
public interface PartnerOrgDao {
    /**
     * 新增记录
     * @param partnerOrg
     */
    void add(PartnerOrg partnerOrg);

    /**
     * 更新记录
     * @param partnerOrg
     */
    void update(PartnerOrg partnerOrg);

    /**
     * 反显记录
     * @param id
     * @return
     */
    PartnerOrg reflect(@Param(value = "id")Long id);

    /**
     * 分页查询
     * @param partnerOrgVO
     * @return
     */
    List<PartnerOrg> listPage(PartnerOrgVO partnerOrgVO);

    /**
     * 校验共建单位名称是否重复
     * @param partnerOrgName  共建单位名称
     * @param auditState  审批状态
     * @return
     */
    Integer countSamepartnerOrgName(@Param(value = "partnerOrgName") String partnerOrgName);
}
