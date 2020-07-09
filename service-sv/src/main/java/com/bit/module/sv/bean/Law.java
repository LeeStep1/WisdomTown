package com.bit.module.sv.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Law implements Serializable {
    /**
     * 主键
     */
    @NotNull(message = "法律id不能为空", groups = Update.class)
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
     * 封面url
     */
    private String coverUrl;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 应用领域
     */
    @NotEmpty(message = "应用领域不能为空")
    private List<Integer> ranges;

    public interface Update {}

    public void updateLaw(Law toUpdate) {
        this.setCurrentVersion(toUpdate.getCurrentVersion());
        this.setPublishUnit(toUpdate.getPublishUnit());
        this.setName(toUpdate.getName());
        this.setRanges(toUpdate.getRanges());
        this.setUpdateAt(new Date());
    }
}