package com.bit.module.pb.vo.partyMember;

import com.bit.module.pb.bean.PartyMemberApproval;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/3 12:59
 */
@Data
public class PartyMemberParams implements Serializable {
    /**
     * 表ID
     */
    @NotNull(message = "id不能为空", groups = {ModifyPartyMember.class})
    private Long id;
    /**
     * 党员id
     */
    private Long memberId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片
     */
    private String photo;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    private Date birthdate;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 民族
     */
    private String nation;
    /**
     * 入党时间
     */
    private Date joinTime;
    /**
     * 学历
     */
    private String education;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 户籍所在派出所
     */
    private String policeStation;
    /**
     * 工作/学习单位
     */
    private String company;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 现居住地
     */
    private String address;
    /**
     * 目标组织
     */
    private String orgId;
    /**
     * 组织名称或退伍军人的编入党支部名称
     */
    private String orgName;
    // ========================== 退役军人信息 =========================
    /**
     * 籍贯
     */
    private String origin;
    /**
     * 原服役部队
     */
    private String originalTroops;
    /**
     * 退役时间
     */
    private Date retireTime;
    /**
     * 是否自主择业，0否 1是
     */
    private Integer isSelfEmployment;
    /**
     * 组织关系落实时间
     */
    private Date relTransferTime;
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 停用原因
     * 1：党员去世；
     * 2：停止党籍；
     * 3：党员出党；
     * 4：其它；
     */
    private Integer reason;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附件id列表，英文逗号分隔。附件id为文件服务的文件id
     */
    private List<PartyMemberApproval.AttachVO> attach;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回
     */
    private Integer status;

    // ==================== 党员转移 ====================
    /**
     * 原组织id
     */
    private String fromOrgId;
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 转出原组织时间
     */
    private Date outTime;
    /**
     * 转入目标组织时间
     */
    private Date inTime;
    /**
     * 党员党费已交至时间
     */
    private String deadline;

    //columns END


    public interface ModifyPartyMember{}
}
