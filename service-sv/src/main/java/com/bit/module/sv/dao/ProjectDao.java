package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Project;
import com.bit.module.sv.vo.IncrementalRequest;
import com.bit.module.sv.vo.ProjectExportVO;
import com.bit.module.sv.vo.ProjectPageQuery;
import com.bit.module.sv.vo.StatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectDao {
    int deleteById(Long id);

    int insert(Project project);

    int insertSelective(Project project);

    Project selectById(Long id);

    int updateByIdSelective(Project project);

    int updateById(Project project);

    Project selectByIdAndSource(@Param("id") Long id, @Param("source") Integer source);

    List<Project> findByConditionPage(ProjectPageQuery pageQuery);

    List<Project> selectByStatusAndCategoryAndSource(@Param("status") Integer status,
                                                     @Param("category") Integer category, @Param("source") Integer source);

    List<StatisticsVO> countProjectsBySource(@Param("source") Integer source);

    List<ProjectExportVO> selectByIdsAndSource(@Param("ids") Collection<Long> ids, @Param("source") Integer source);

    List<Project> incrementProjects(IncrementalRequest request);
}