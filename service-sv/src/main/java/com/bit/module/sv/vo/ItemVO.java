package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemVO extends BasePageVo {
    /**
     * id
     */
    @NotNull(message = "项目ID不能为空", groups = {AddItemList.class, Modify.class})
    private Long id;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = AddItem.class)
    private String name;

    /**
     * 排查内容
     */
    @NotBlank(message = "排查内容不能为空", groups = AddItemList.class)
    private String content;

    /**
     * 执法依据,正文ID集合
     */
    @NotEmpty(message = "执法依据不能为空", groups = AddItemList.class)
    private List<Integer> regulations;

    /**
     * 来源(appId)
     */
    private Integer source;

    public interface AddItem {
    }

    public interface AddItemList {

    }

    public interface Modify {
    }

    public interface PageSearch {
    }
}
