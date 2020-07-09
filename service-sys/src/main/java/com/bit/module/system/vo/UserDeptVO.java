package com.bit.module.system.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-01-24 17:29
 */
@Data
public class UserDeptVO {
    private String depName;
    private List<UserAndDepVO> userList;
}
