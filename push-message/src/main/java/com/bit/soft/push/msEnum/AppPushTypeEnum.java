package com.bit.soft.push.msEnum;

/**
 * @Description:  移动端的推送方式推送方式
 * @Author: liyujun
 * @Date: 2019-08-30
 **/
public enum AppPushTypeEnum {

   /**将别名推送的code 值由2改为0 ，适配之前调用时的不传的问题**/

    /**
     * 标签推送
     */
    PUSHBYTAG(1,"标签推送"),

    /**
     * 别名推送
     */
    PUSHBYALIASE(0,"别名推送");


    private int pushCode;

    private String info;


    /**
     * @param pushCode  推送方式
     * @param info  接入端信息
     */
      AppPushTypeEnum (int pushCode, String info){

        this.pushCode=pushCode;
        this.info=info;

    }

    public int getPushCode() {
        return pushCode;
    }


    public String getInfo() {
        return info;
    }

    public static AppPushTypeEnum getAppPushType(int pushCode){

        AppPushTypeEnum rs=null;
        for(AppPushTypeEnum a: AppPushTypeEnum.values()){
            if(a.getPushCode()==pushCode){
                rs= a;
            }
        }
        return rs;

    }
}
