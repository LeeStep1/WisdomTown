package com.bit.module.sv.vo;

import com.bit.module.sv.bean.Regulation;
import com.bit.module.sv.bean.RegulationContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/7/18 10:34
 */
@Data

@EqualsAndHashCode(callSuper = true)
public class RegulationPreviewVO extends Regulation {
    private List<RegulationContent> contents;

    private String lawName;

    public void setRegulation(Regulation regulation) {
        setId(regulation.getId());
        setContent(regulation.getContent());
        setLawId(regulation.getLawId());
        setTitle(regulation.getTitle());
        setParentId(regulation.getParentId());
        setCreateAt(regulation.getCreateAt());
        setUpdateAt(regulation.getUpdateAt());
    }
}
