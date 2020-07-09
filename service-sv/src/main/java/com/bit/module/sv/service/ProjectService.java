package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.vo.IncrementalRequest;
import com.bit.module.sv.vo.ProjectExportVO;
import com.bit.module.sv.vo.ProjectPageQuery;
import com.bit.module.sv.vo.ProjectVO;

import java.util.Collection;
import java.util.List;

public interface ProjectService {

    BaseVo hazardListTree();

    BaseVo attachListTree();

    void addProject(ProjectVO projectVO);

    void modifyProject(ProjectVO projectVO);

    BaseVo findByIdAndSource(Long id, Integer source);

    BaseVo listProjectsWithPage(ProjectPageQuery pageQuery);

    void deleteProject(Long id);

    void changeStatus(Long id, Integer status);

    BaseVo listProjectsByStatusAndCategoryAndSource(Integer status, Integer category, Integer source);

    BaseVo countProjectsBySource(Integer source);

    List<ProjectExportVO> listProjectsByIdsAndSource(Collection<Long> ids, Integer source);

    BaseVo incrementProjects(IncrementalRequest request);
}
