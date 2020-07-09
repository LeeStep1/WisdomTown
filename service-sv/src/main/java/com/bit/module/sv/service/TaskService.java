package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.vo.Checklist;
import com.bit.module.sv.vo.IncrementalRequest;
import com.bit.module.sv.vo.ReviewChecklist;
import com.bit.module.sv.vo.TaskVO;

public interface TaskService {

    BaseVo deleteById(Long id);

    BaseVo modifyTask(TaskVO taskVO);

    BaseVo listTasks(TaskVO taskVO);

    BaseVo selectByIdWithAll(Long id, Integer source);

    BaseVo startTask(Long id);

    BaseVo taskStatistics(Boolean personal, Integer range);

    BaseVo incrementTasks(IncrementalRequest request);

    BaseVo modifyChecklist(Checklist checklist);

    BaseVo addReviewChecklist(ReviewChecklist reviewChecklist);

    BaseVo selectByRefId(Long taskId);

    BaseVo modifyReviewChecklist(ReviewChecklist reviewChecklist, Integer source);

    BaseVo selectById(Long id);

    BaseVo exportById(Long id);

    BaseVo dailyPushExpireTask();

    BaseVo dailyPushExpireReviewTask();
}
