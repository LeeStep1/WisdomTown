package com.bit.module.sv.dao;

import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.vo.IllegalVO;
import com.bit.module.sv.vo.IncrementalRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RectificationNoticeDao {
    int deleteById(Long id);

    int insert(RectificationNotice rectificationNotice);

    int insertSelective(RectificationNotice rectificationNotice);

    RectificationNotice selectById(Long id);

    int updateByIdSelective(RectificationNotice rectificationNotice);

    int updateById(RectificationNotice rectificationNotice);

    int batchInsert(@Param("list") List<RectificationNotice> list);

    RectificationNotice selectByTaskId(Long taskId);

    int deleteByTaskId(Long taskId);

    List<IllegalVO> incrementRectificationNotices(IncrementalRequest request);
}