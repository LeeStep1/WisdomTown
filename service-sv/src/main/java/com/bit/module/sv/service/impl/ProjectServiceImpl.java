package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.AttachList;
import com.bit.module.sv.bean.HazardList;
import com.bit.module.sv.bean.Project;
import com.bit.module.sv.dao.AttachListDao;
import com.bit.module.sv.dao.HazardListDao;
import com.bit.module.sv.dao.ProjectDao;
import com.bit.module.sv.enums.ProjectStatusEnum;
import com.bit.module.sv.service.ProjectService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.utils.StringUtils;
import com.bit.module.sv.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("projectService")
@Transactional
public class ProjectServiceImpl extends BaseService implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private HazardListDao hazardListDao;

    @Autowired
    private AttachListDao attachListDao;

    @Override
    public BaseVo hazardListTree() {
        List<HazardList> list = hazardListDao.findAll();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(buildHazardListTree(list));
        return baseVo;
    }

    @Override
    public BaseVo attachListTree() {
        List<AttachList> list = attachListDao.findAll();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(buildAttachListTree(list));
        return baseVo;
    }

    @Override
    public void addProject(ProjectVO projectVO) {
        Project project = new Project();
        BeanUtils.copyProperties(projectVO, project);
        project.setCreateAt(DateUtils.getCurrentDate());
        project.setUpdateAt(project.getCreateAt());
        project.setStatus(ProjectStatusEnum.PENDING_APPROVAL.value);
        projectDao.insert(project);
    }

    @Override
    public void modifyProject(ProjectVO projectVO) {
        Project toGet = projectDao.selectById(projectVO.getId());
        if (toGet == null) {
            throw new BusinessException("项目不存在");
        }
        if (toGet.getStatus() != ProjectStatusEnum.PENDING_APPROVAL.value) {
            throw new BusinessException("只能编辑待审批的项目");
        }
        Project toUpdate = new Project();
        BeanUtils.copyProperties(projectVO, toUpdate);
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        toUpdate.setStatus(null);
        toUpdate.setSource(null);
        projectDao.updateByIdSelective(toUpdate);
    }

    @Override
    public BaseVo findByIdAndSource(Long id, Integer source) {
        Project toGet = projectDao.selectByIdAndSource(id, source);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(toGet);
        return baseVo;
    }

    @Override
    public BaseVo listProjectsWithPage(ProjectPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        if (StringUtils.isBlank(pageQuery.getOrderBy())) {
            pageQuery.setOrderBy("create_at");
        }
        if (StringUtils.isBlank(pageQuery.getOrder())) {
            pageQuery.setOrder("desc");
        }
        List<Project> list = projectDao.findByConditionPage(pageQuery);
        PageInfo<Project> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public void deleteProject(Long id) {
        Project toGet = projectDao.selectById(id);
        if (toGet == null) {
            throw new BusinessException("项目不存在");
        }

        if (toGet.getStatus() != ProjectStatusEnum.PENDING_APPROVAL.value) {
            throw new BusinessException("只能删除待审批的项目");
        }
        projectDao.deleteById(id);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        if (status == null) {
            logger.info("状态值为空，无需更新项目({})状态.", id);
            return;
        }
        if (status == ProjectStatusEnum.PENDING_APPROVAL.value) {
            throw new BusinessException("无法将项目变为待审批");
        }
        if (ProjectStatusEnum.getByValue(status) == null) {
            throw new BusinessException("非法的项目状态");
        }

        Project toGet = projectDao.selectById(id);
        if (toGet == null) {
            throw new BusinessException("项目不存在");
        }
        Integer oldStatus = toGet.getStatus();
        if (status == oldStatus) {
            logger.info("状态值({})没有变更，无需更新项目({})状态.", status, id);
            return;
        }

        if (status == ProjectStatusEnum.COMPLETED.value && oldStatus != ProjectStatusEnum.IN_PROGRESS.value) {
            throw new BusinessException("项目状态受限，无法完成");
        }
        if (status == ProjectStatusEnum.IN_PROGRESS.value
                && !Arrays.asList(ProjectStatusEnum.PAUSED.value, ProjectStatusEnum.PENDING_APPROVAL.value).contains(oldStatus)) {
            throw new BusinessException("项目已完成");
        }
        if (status == ProjectStatusEnum.PAUSED.value && oldStatus != ProjectStatusEnum.IN_PROGRESS.value) {
            throw new BusinessException("项目状态受限，无法暂停");
        }
        Project toUpdate = new Project();
        toUpdate.setId(id);
        toUpdate.setStatus(status);
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        projectDao.updateByIdSelective(toUpdate);
    }

    @Override
    public BaseVo listProjectsByStatusAndCategoryAndSource(Integer status, Integer category, Integer source) {
        List<Project> list = projectDao.selectByStatusAndCategoryAndSource(status, category, source);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    @Override
    public BaseVo countProjectsBySource(Integer source) {
        List<StatisticsVO> statisticsVOList = projectDao.countProjectsBySource(source);
        Long pendingApproval = 0L;
        Long inProgress = 0L;
        Long paused = 0L;
        Long completed = 0L;
        Map<String, Long> result = new HashMap<>(4);
        for (StatisticsVO vo : statisticsVOList) {
            if (vo.getStatus() == ProjectStatusEnum.PENDING_APPROVAL.value) {
                pendingApproval += vo.getNum();
                continue;
            }
            if (vo.getStatus() == ProjectStatusEnum.IN_PROGRESS.value) {
                inProgress += vo.getNum();
                continue;
            }
            if (vo.getStatus() == ProjectStatusEnum.PAUSED.value) {
                paused += vo.getNum();
                continue;
            }
            if (vo.getStatus() == ProjectStatusEnum.COMPLETED.value) {
                completed += vo.getNum();
                continue;
            }
        }
        result.put(ProjectStatusEnum.PENDING_APPROVAL.name(), pendingApproval);
        result.put(ProjectStatusEnum.IN_PROGRESS.name(), inProgress);
        result.put(ProjectStatusEnum.PAUSED.name(), paused);
        result.put(ProjectStatusEnum.COMPLETED.name(), completed);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(result);
        return baseVo;
    }

    @Override
    public List<ProjectExportVO> listProjectsByIdsAndSource(Collection<Long> ids, Integer source) {
        return projectDao.selectByIdsAndSource(ids, source);
    }

    @Override
    public BaseVo incrementProjects(IncrementalRequest request) {
        List<Project> projects = projectDao.incrementProjects(request);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(projects);
        return baseVo;
    }

    /**
     * 组装危险源清单层级结构
     * @param items
     * @return
     */
    private List<ElementVO> buildHazardListTree(List<HazardList> items) {
        LinkedList<HazardList> queue = new LinkedList<>(items);
        Map<Long, ElementVO> elementMap = new HashMap<>(queue.size());
        List<ElementVO> elementVOS = new ArrayList<>();
        Map<Long, List<HazardList>> parentChildren = new HashMap<>();

        HazardList element;
        while ((element = queue.poll()) != null) {
            ElementVO parent = null;
            if (element.getParentId() != null && (parent = elementMap.get((element.getParentId()))) == null) {
                parentChildren.computeIfAbsent(element.getParentId(), k -> new LinkedList<>()).add(element);
                continue;
            }

            ElementVO elementVO = new ElementVO();
            elementVO.setId(element.getId());
            elementVO.setName(element.getTitle());
            elementVO.setType(element.getType());
            elementVO.setParentId(element.getParentId());
            elementMap.put(element.getId(), elementVO);

            if (parent == null) {
                elementVO.setLevel(1);
                elementVOS.add(elementVO);
            } else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                elementVO.setLevel(parent.getLevel() + 1);
                parent.getChildren().add(elementVO);
            }

            parentChildren.computeIfPresent(elementVO.getId(), (k, v) -> {
                v.forEach(queue::push);
                return null;
            });
        }
        return elementVOS;
    }

    /**
     * 组装上传附件清单层级结构
     * @param items
     * @return
     */
    private List<ElementVO> buildAttachListTree(List<AttachList> items) {
        LinkedList<AttachList> queue = new LinkedList<>(items);
        Map<Long, ElementVO> elementMap = new HashMap<>(queue.size());
        List<ElementVO> elementVOS = new ArrayList<>();
        Map<Long, List<AttachList>> parentChildren = new HashMap<>();

        AttachList element;
        while ((element = queue.poll()) != null) {
            ElementVO parent = null;
            if (element.getParentId() != null && (parent = elementMap.get((element.getParentId()))) == null) {
                parentChildren.computeIfAbsent(element.getParentId(), k -> new LinkedList<>()).add(element);
                continue;
            }

            ElementVO elementVO = new ElementVO();
            elementVO.setId(element.getId());
            elementVO.setName(element.getTitle());
            elementVO.setType(element.getType());
            elementVO.setRemark(element.getRemark());
            elementVO.setParentId(element.getParentId());
            elementMap.put(element.getId(), elementVO);

            if (parent == null) {
                elementVO.setLevel(1);
                elementVOS.add(elementVO);
            } else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                elementVO.setLevel(parent.getLevel() + 1);
                parent.getChildren().add(elementVO);
            }

            parentChildren.computeIfPresent(elementVO.getId(), (k, v) -> {
                v.forEach(queue::push);
                return null;
            });
        }
        return elementVOS;
    }
}
