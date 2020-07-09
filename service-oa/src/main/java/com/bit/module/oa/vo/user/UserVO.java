package com.bit.module.oa.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/2/14 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    // 用户ID
    private Long id;
    // 用户名称
    private String username;
    // 用户名称
    private String realName;
}