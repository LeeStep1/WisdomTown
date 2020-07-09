package com.bit.module.sv.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Regulation implements Serializable {
    /**
	* 
	*/
    @NotNull(message = "法规id不能为空", groups = Update.class)
    private Long id;

    /**
	* 所属法律ID
	*/
    @NotNull(message = "所属法律不能为空", groups = Add.class)
    private Long lawId;

    /**
	* 父级ID
	*/
    private Long parentId;

    /**
	* 标题
	*/
    @NotBlank(message = "标题不能为空", groups = Add.class)
    private String title;

    /**
	* 内容
	*/
    @NotBlank(message = "内容不能为空", groups = Add.class)
    private String content;

    /**
	* 创建时间
	*/
    private Date createAt;

    /**
	* 更新时间
	*/
    private Date updateAt;

    public void update(Regulation regulation) {
        this.setContent(regulation.getContent());
        this.setTitle(regulation.getTitle());
        this.setLawId(regulation.getLawId());
        this.setParentId(regulation.getParentId());
        this.setUpdateAt(new Date());
    }

    public interface Update {}
    public interface Add {}
}