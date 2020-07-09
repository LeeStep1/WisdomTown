package com.bit.module.oa.bean;

import java.io.Serializable;
import lombok.Data;

/**
* Created by Mybatis Generator 2019/03/01
*/
@Data
public class MeetingRoom implements Serializable {
    /**
	* 
	*/
    private Long id;

    /**
	* 名称
	*/
    private String name;

    /**
	* 楼层
	*/
    private String floor;

    /**
	* 地点
	*/
    private String location;

    /**
	* 容纳人数
	*/
    private Integer capacity;

    /**
	* 状态，0停用 1启用
	*/
    private Integer status;

}