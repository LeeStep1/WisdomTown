package com.bit.common.consts;


import java.util.List;

/**
 * @Description:  接入端枚举类
 * @Author: liyujun
 * @Date: 2019-08-30
 **/
public enum TerminalTypeEnum {


    /**
     * web接入端
     */
    TERMINALTOWEB(1,"web端"),
    /**
     * 党建APP接入端
     */
    TERMINALTOPBAPP(2,"智慧党建APP"),

    /**
     * 志愿者APP接入端
     */
    TERMINALTOVOLAPP(3,"志愿者APP"),

    /**
     * 巡检APP接入端
     */
    TERMINALTOPIAPP(4,"巡检APP"),

    /**
     * 政务app接入端
     */
    TERMINALTOOAAPP(5,"政务app"),


    /**
     * 社区居民app
     */
    TERMINALTOCBORESIDENTAPP(7,"社区居民app"),


    /**
     * 社区物业app
     */
    TERMINALTOCBOPMCNTAPP(8,"社区物业app"),


    /**
     * 社区居委会app
     */
    TERMINALTOCBOORGNTAPP(9,"社区居委会app"),

    /**
     * 安检app接入端
     */
    TERMINALTOSVAPP(10,"安检app"),

    /**
     * 环保app接入端
     */
    TERMINALTOEPAPP(11,"环保app");



    /**
     * 接入端ID
     */
    private int tid;

    /**
     *  接入端信息
     */
    private String info;

    /**
     * @param tid  接入端ID
     * @param info  接入端信息
     */
    TerminalTypeEnum(int tid, String info) {
        this.tid = tid;
        this.info = info;
    }

    public int getTid() {
        return tid;
    }


    public String getInfo() {
        return info;
    }

    /**
     * 判断是否是移动端
     * @param tid  接入端ID
     * @return  boolean
     */
     public  static boolean  isApp(int tid){
           boolean  flag=false;
           for(TerminalTypeEnum a:TerminalTypeEnum.values()){
               if(a.getTid()!=TerminalTypeEnum.TERMINALTOWEB.getTid()){
                   flag=true;
               }
           }
          return flag;
     }


    /**
     * 判断是否是移动端
     * @param tid
     * @return  boolean
     */
    public  static boolean  containApp(List <String> tid){
        boolean  flag=false;
        if((tid.contains(String.valueOf(TerminalTypeEnum.TERMINALTOWEB.getTid())) &&tid.size()>1)||(!tid.contains(String.valueOf(TerminalTypeEnum.TERMINALTOWEB.getTid())))){
            flag=true;
        }
        return flag;
    }
}
