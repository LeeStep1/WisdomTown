package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.bean.Identity;
import com.bit.module.system.bean.PbOrganization;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * User
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BasePageVo{

    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 6,max = 20,message = "用户名长度非法")
    private String username;
    /**
     * 用户的实际名称
     */
    @NotEmpty(message = "姓名不能为空")
    private String realName;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 注册时间
     */
    private Date insertTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 0已删除 1可用
     */
    private Integer status;
    /**
     * 创建账号类型
     */
    private Integer createType;
    /**
     * 身份证号
     */
    @NotEmpty(message = "身份证号不能为空")
    private String idcard;
    /**
     * 接入端
     */
    private Integer terminalId;
    /**
     * 临时字段--接受身份信息
     */
    private List<Identity> identities;
    /**
     * 临时字段--党建信息
     */
    private List<PbOrganization> pbOrganizations;
    /**
     * 临时字段--字典里面的应用集合
     */
    private List<Dict> dicts;
    /**
     * 临时字段---查询字典应用
     */
    private String dictIds;
    /**
     * 临时字段---login校验应用和身份
     */
    private Integer appId;

    /**
     * 部门id
     */
    private List<Integer> depIds;
    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 身份列表
     */
    private List<Long> identityIds;

    /**
     * 身份列表
     */
    private List<Long> pbOrgIds;
    /**
     * 政务组织结构
     */
    private List<Integer> oaOrgIds;
    /**
     * 应用ids
     */
    private List<Integer> appIds;
    /**
     * 社区ids
     */
    private List<Long> cboDepIds;
    /**
     * 志愿者服务站id
     */
    private Long stationId;
    //columns END
    /**
     * 管理员关联党组织id
     */
    private Long pbOrgId;

}


