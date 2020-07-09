package com.bit.module.system.bean;

import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-15 16:20
 */
@Data
public class OrgAndName {
    private Long appId;

    private Long orgId;

    private String name;

    private String pbOrgId;

    private List<Long> orgIds;

    private List<String> pbOrgIds;
}
