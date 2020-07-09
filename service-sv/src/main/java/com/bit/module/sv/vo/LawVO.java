package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class LawVO extends BasePageVo {
    /**
	* 主键
	*/
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "法律名称不能为空")
    private String name;

    /**
     * 当前版本
     */
    @NotBlank(message = "当前版本不能为空")
    private String currentVersion;

    /**
     * 发布单位
     */
    @NotBlank(message = "发布单位不能为空")
    private String publishUnit;

    /**
     * 应用领域
     */
    @NotEmpty(message = "应用领域不能为空")
    private List<Integer> ranges;

    /**
     * 查询应用领域
     */
    private Integer range;

}