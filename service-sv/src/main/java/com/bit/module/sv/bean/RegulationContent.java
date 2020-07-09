package com.bit.module.sv.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegulationContent implements Serializable {
    /**
	* id
	*/
    @NotNull(message = "正文id不能为空", groups = Update.class)
    private Long id;

    /**
	* 法规id
	*/
    @NotNull(message = "法规id不能为空", groups = Add.class)
    private Long regulationId;

    /**
	* 标题
	*/
    @NotBlank(message = "标题不能为空", groups = Add.class)
    private String title;

    /**
	* 正文内容
	*/
    @NotBlank(message = "正文内容不能为空", groups = Add.class)
    private String content;

    /**
	* 创建时间
	*/
    private Date createAt;

    /**
	* 更新时间
	*/
    private Date updateAt;

    public void update(RegulationContent content) {
        this.setContent(content.getContent());
        this.setTitle(content.getTitle());
        this.setUpdateAt(new Date());
    }

    public interface Add {}
    public interface Update {}
}