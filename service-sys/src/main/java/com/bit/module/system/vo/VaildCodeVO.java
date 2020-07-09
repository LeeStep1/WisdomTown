package com.bit.module.system.vo;

import lombok.Data;

/**
 * @author liuyancheng
 * @create 2019-01-11 13:50
 */
@Data
public class VaildCodeVO {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String vaildCode;
}
