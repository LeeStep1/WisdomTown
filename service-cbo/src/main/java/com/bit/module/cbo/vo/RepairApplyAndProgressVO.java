package com.bit.module.cbo.vo;

import com.bit.module.cbo.bean.RepairApplyProgress;
import lombok.Data;

import java.util.List;

/**
 * @Description 报修记录和进度返显
 * @Author chenduo
 * @Date 2019/8/30 11:29
 **/
@Data
public class RepairApplyAndProgressVO {
	/**
	 * 报修申请
	 */
	private RepairApplyVO repairApplyVO;
	/**
	 * 报修处理记录
	 */
	private List<RepairApplyProgress> repairApplyProgressList;
	/**
	 * 居民地址信息
	 */
	private List<String> addressNames;
	/**
	 * 故障照片集合
	 */
	private List<FileInfo> fileInfos;
	/**
	 * web端待办推送专用
	 */
	private Integer version;
}
