package com.bit.module.oa.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Company
 * @author generator
 */
@Data
public class Inspect implements Serializable {

	//columns START

    /**
     * id
     */
	private Long id;
    /**
     * 巡检单号
     */
	private String no;
    /**
     * 巡检名称
     */
	private String name;
    /**
     * 类型 1智能巡检 2轨迹巡检
     */	
	private Integer type;
    /**
     * 巡检开始时间
     */	
	private Date startTime;
    /**
     * 巡检结束时间
     */	
	private Date endTime;
    /**
     * 部门json集合，格式[{"id":xx,"name":xxx},{...}]
     */	
	private List<Dept> deps;
    /**
     * 执行者json集合，格式[{"id":xx,"name":xxx},{...}]
     */	
	private List<SimpleUser> executors;
    /**
     * 签到点json集合，格式[{"id":xx,"name":"xxx","lng":xx,"lat":xx},{...}]
     */
    private List<SimpleSpot> spots;
    /**
     * 巡检内容
     */	
	private String content;
    /**
     * 附件id集合，英文逗号分隔
     */	
	private String attactIds;
    /**
     * 发布者id
     */	
	private Long publisherId;
    /**
     * 最后上报时间
     */	
	private Date lastReportAt;
    /**
     * 创建时间
     */	
	private Date createAt;
    /**
     * 状态 0未发布 1未执行 2执行中 3已完成 4已终止 5已结束
     */	
	private Integer status;
    /**
     * 版本号
     */
    private Integer version;


	public List<SimpleUser> parseExecutors() {
        if (CollectionUtils.isEmpty(this.executors)) {
            return Collections.emptyList();
        }
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(this.executors));
        this.setExecutors(array.toJavaList(SimpleUser.class));
        return this.getExecutors();
    }

	public List<SimpleSpot> parseSpots() {
        if (CollectionUtils.isEmpty(this.getSpots())) {
            return Collections.emptyList();
        }
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(this.spots));
        this.setSpots(array.toJavaList(SimpleSpot.class));
        return this.getSpots();
    }
}


