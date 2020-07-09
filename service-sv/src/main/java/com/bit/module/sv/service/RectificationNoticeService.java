package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.vo.IncrementalRequest;

public interface RectificationNoticeService {

    BaseVo addRectificationNotice(RectificationNotice rectificationNotice);
    BaseVo selectByTaskId(Long taskId);

    BaseVo incrementRectificationNotices(IncrementalRequest request);

    BaseVo modifyRectificationNotice(RectificationNotice notice);
}
