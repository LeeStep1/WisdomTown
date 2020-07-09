package com.bit.module.manager.vo;

import com.bit.base.dto.UserInfo;
import lombok.Data;

/**
 * @author liuyancheng
 * @create 2019-01-14 15:07
 */
@Data
public class RefreshTokenVO {
    /**
     * userInfo 对象
     */
    private UserInfo userInfo;
    /**
     * accessToken 的 key
     */
    private String atKey;
    /**
     * 刷新token
     */
    private String refreshToken;
    /**
     * 接入端
     */
    private Integer terminalId;
}
