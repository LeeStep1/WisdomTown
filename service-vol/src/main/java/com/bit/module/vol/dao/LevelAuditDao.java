package com.bit.module.vol.dao;

import com.bit.module.vol.bean.LevelAudit;
import com.bit.module.vol.bean.LevelAuditVolunteer;
import com.bit.module.vol.bean.LevelLog;
import com.bit.module.vol.bean.VolunteerLevel;
import com.bit.module.vol.vo.LevelAuditVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-20 14:00
 */
@Repository
public interface LevelAuditDao {

    void add(LevelAudit levelAudit);

    void update(LevelAudit levelAudit);

    VolunteerLevel reflect(@Param(value = "id") Long id);

    List<LevelAuditVolunteer> zlistPage(LevelAuditVO levelAuditVO);

    List<LevelAuditVolunteer> flistPage(LevelAuditVO levelAuditVO);

    LevelAudit findById(@Param(value = "id") Long id);

    List<LevelAudit> findByParam(LevelAudit levelAudit);

    List<LevelLog> queryLogByVolunteerId(LevelAudit levelAudit);

    LevelAudit queryTheLastOne(@Param(value = "volunteerId") Long volunteerId);
}
