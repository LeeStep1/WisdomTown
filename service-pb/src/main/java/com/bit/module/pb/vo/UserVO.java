package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
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

    //columns END
    /**
     * 管理员关联党组织id
     */
    private Long pbOrgId;

}


