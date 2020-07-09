package com.bit.soft.push.msEnum;

import java.text.MessageFormat;

/**
 * @Description:  用于推送时的tag 或者别名的控制
 * @Author: liyujun
 * @Date: 2019-09-04
 **/
public enum AppPushTagAliasEnum {

    /**
     *根据小区推送的标签
     *
     *
     * */
    CBO_TAGPUSH_COMMUNITY("CBO_TAGPUSH_COMMUNITY_{0}","社区模块标签推送定义模板{小区ID}"),


    /**
     *据社区推送的标签
     * */
    CBO_TAGPUSH_ORG("CBO_TAGPUSH_ORG_{0}","社区模块标签推送定义模板{orgID}");

    private String pushCode;

    private String info;

    /**
     * @param pushCode  推送方式
     * @param info  接入端信息
     */
    AppPushTagAliasEnum(String pushCode, String info){

        this.pushCode=pushCode;
        this.info=info;

    }

    public String getPushCode() {
        return pushCode;
    }


    public String getInfo() {
        return info;
    }


    public String getPsuhTag(String ... parmas) {

      return   MessageFormat.format(this.pushCode,parmas);

    }
}
