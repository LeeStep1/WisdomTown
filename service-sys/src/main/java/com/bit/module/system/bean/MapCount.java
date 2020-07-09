package com.bit.module.system.bean;

import lombok.Data;

/**
 * 用来查询身份证 手机号 和用户名在数据库是否存在
 */
@Data
public class MapCount {

    private Integer num;

    private String temp;
}
