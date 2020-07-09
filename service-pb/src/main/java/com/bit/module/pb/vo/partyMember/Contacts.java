package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/6/18 18:52
 */
@Data
public class Contacts implements Serializable {

    private String orgId;

    private String orgName;

    private List<PartyMember> partyMemberList;

    @Data
    public static class PartyMember {

        private String name;

        private String mobile;
    }

}
