package com.bit.module.pb.bean;

import lombok.Data;

import java.util.List;

/**
 * 三会一课 会议 和 学习计划选择人员专用
 */
@Data
public class PartyMemberSearch {
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 党组织id集合
	 */
	private List<Long> orgIds;
}
