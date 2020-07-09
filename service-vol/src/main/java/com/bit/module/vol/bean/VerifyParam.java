package com.bit.module.vol.bean;

import lombok.Data;

/**
 * @author chenduo
 * @create 2019-03-14 15:37
 */
@Data
public class VerifyParam {
    /**
     * id
     */
    private Long id;
    /**
     * 身份证
     */
    private String cardId;
    /**
     * 手机号
     */
    private String mobile;
}
