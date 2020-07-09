package com.bit.module.pb.vo.partyMember;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/11 13:19
 */
@Data
@ExcelTarget("partyMemberExportVO")
public class PartyMemberExportVO implements Serializable {

    @Excel(name = "所属党支部", width = 22, orderNum = "1")
    private String orgName;

    @Excel(name = "姓名", width = 13, orderNum = "2")
    private String name;

    @Excel(name = "性别", replace = {"男_1", "女_2", "*_3"}, width = 12, orderNum = "3")
    private String sex;

    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 12, orderNum = "4")
    private Date birthdate;

    @Excel(name = "民族", width = 15, orderNum = "5")
    private String nation;

    @Excel(name = "学历", replace = {"高中及以下_1", "专科_2", "本科_3", "硕士_4", "博士及以上_5", "*_6"}, width = 12, orderNum = "6")
    private String education;

    @Excel(name = "身份证号码", width = 20, orderNum = "7")
    private String idCard;

    @Excel(name = "联系电话", width = 13, orderNum = "8")
    private String mobile;

    @Excel(name = "入党时间", format = "yyyy-MM-dd", width = 13, orderNum = "9")
    private Date joinTime;

    @Excel(name = "党籍状态", replace = {"正式党员_1", "预备党员_2", " _null", " _ "}, width = 13, orderNum = "10")
    private Integer memberType;

    @Excel(name = "户籍所在派出所", width = 20, orderNum = "11")
    private String policeStation;

    @Excel(name = "工作/学习单位", width = 25, orderNum = "12")
    private String company;

    @Excel(name = "现居住地", width = 25, orderNum = "13")
    private String address;

    @Excel(name = "籍贯", width = 13, orderNum = "14")
    private String origin;

    @Excel(name = "原服役部队", width = 13, orderNum = "15")
    private String originalTroops;

    @Excel(name = "退役时间", format = "yyyy-MM-dd", width = 13, orderNum = "16")
    private Date retireTime;

    @Excel(name = "是否自主择业", replace = {"是_1", "否_0", " _ ", " _null"}, width = 15, orderNum = "17")
    private Integer isSelfEmployment;

    @Excel(name = "组织关系落实时间", format = "yyyy-MM-dd", width = 15, orderNum = "18")
    private Date relTransferTime;

    @Excel(name = "编入党支部", width = 18, orderNum = "19")
    private String branchName;

}
