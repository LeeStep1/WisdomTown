package com.bit.module.cbo.bean;

import com.bit.module.cbo.vo.FileInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 11:34
 **/
@Data
public class Community {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 小区名称
	 */
	private String communityName;

	/**
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 所属的社区组织id
	 */
	private Long orgId;

	/**
	 * 物业公司名称
	 */
	private String pmcCompanyName;
	/**
	 * 物业公司id
	 */
	private Long pmcCompanyId;
	/**
	 * 房屋户型图资料的ids,逗号分隔
	 */
	private String attachmentIds;

	/**
	 * 创建者ID
	 */
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 更新者ID
	 */
	private Long updateUserId;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 所属文件集合
	 */
	private List<FileInfo> fileInfoList ;
}
