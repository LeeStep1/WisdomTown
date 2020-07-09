package com.bit.module.pb.bean;

import lombok.Data;

@Data
public class PartyMemberOrgName {
    /**
     * 党员id
     */
    private Long id;
    /**
     * 党员姓名
     */
    private String partyMemberName;
    /**
     * 党组织id
     */
    private String pbOrgId;
    /**
     * 党组织名称
     */
    private String pbOrgName;
}
