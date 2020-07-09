package com.bit.module.manager.vo;

import com.bit.module.manager.bean.PortalCategory;
import lombok.Data;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-05-09 15:57
 */
@Data
public class PortalCategoryResultVO {
    //columns START

    /**
     * 主键ID
     */
    private Long id;
    /**
     * rank
     */
    private Integer rank;
    /**
     * 栏目名称
     */
    private String categoryName;
    /**
     * 所属导航
     */
    private Long navigationId;

    /**
     * 所属站点ID
     */
    private Long stationId;

    /**
     * 栏目下标题是否必输  0 必输 1 非必输
     */
    private Integer categoryTitleFlag;
    /**
     * 栏目下内容名称是否必输  0 必输 1非必输
     */
    private Integer categoryContentNameFlag;
    /**
     * 栏目下发布方是否必输  0 必输 1 非必输
     */
    private Integer categoryCreateFlag;
    /**
     * 栏目下封面是否必输 0 必输  1 非必输
     */
    private Integer categoryCoverFlag;
    /**
     * 栏目下副标题是否必输 0必输  1 非必输
     */
    private Integer categorySubheadFlag;
    /**
     * 栏目下视频是否必输 0 必输 1 非必输
     */
    private Integer categoryVideoFlag;
    /**
     * 栏目下内容是否必输  0 必输 1非必输
     */
    private Integer categoryContentFlag;
    /**
     * 栏目类型  0 普通栏目  1 特殊栏目
     */
    private Integer categoryType;
    /**
     * 状态  0 启用  1 停用
     */
    private Integer status;
    /**
     * 操作人ID
     */
    private Long operationUserId;
    /**
     * 操作人姓名
     */
    private String operationUserName;
    private List<PortalCategory> secondMenu;
    /**
     * 导航名称
     */
    private String navigationName;

    //columns END
}
