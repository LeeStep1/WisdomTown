package com.bit.common.cmsenum;

/**
 * @description:
 * @author: liyang
 * @date: 2019-05-07
 **/
public enum cmsEnum {

    /**
     * 启用标识
     */
    USING_FLAG(0,"启用"),

    /**
     * 停用标识
     */
    DISABLE_FLAG(1,"停用"),

    /**
     * 未删除标识
     */
    UNDEL_FLAG(0,"未删除"),

    /**
     * 停用标识
     */
    DEL_FLAG(1,"删除"),

    /**
     * 未发布
     */
    UNPUBLISH_FLAG(0,"未发布"),

    /**
     * 已发布
     */
    PUBLISH_FLAG(1,"已发布"),

    /**
     * 正常栏目
     */
    NORMAL_CATEGORY(0,"正常栏目"),

    /**
     * 特殊栏目
     */
    SPECIAL_CATEGORY(1,"特殊栏目"),

    /**
     * 创建资源
     */
    OPERATION_TYPE_CREATE(1,"创建资源"),

    /**
     * 发布资源
     */
    OPERATION_TYPE_PUBLISH(2,"发布资源"),

    /**
     * 删除资源
     */
    OPERATION_TYPE_DELETE(3,"删除资源"),

    /**
     * 取消发布
     */
    OPERATION_TYPE_UNPUBLISH(4,"取消发布"),

    /**
     * 创建并发布
     */
    OPERATION_TYPE_CREATEANDPUBLISH(5,"创建并发布"),

    /**
     * 服务类型内容表
     */
    SERVICETYPE_CONTENT_TABLE(1,"t_service_type"),

    /**
     * 领导班子头像表
     */
    LEADER_IMG_TABLE(2,"t_portal_pb_leader"),

    /**
     * 领导介绍表
     */
    LEADER_INTURDUCED_TABLE(3,"t_portal_oa_leader"),

    /**
     * 党建组织模板
     */
    PB_ORG_TABLE(4,"t_protal_pb_org"),

    /**
     * 普通内容表
     */
    CONTENT_TABLE(5,"t_portal_content"),

    /**
     * 栏目表
     */
    SERVICETYPE_CATEGORY_TABLE(6,"t_portal_category"),

    /**
     * 导航表
     */
    NAVIGATION_TABLE(7,"t_portal_navigation");

    /**
     * 操作码
     */
    private int code;

    /**
     * 操作信息
     */
    private String info;

    /**
     * @param code  状态码
     * @param info  状态信息
     */
    cmsEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }


    public String getInfo() {
        return info;
    }
}
