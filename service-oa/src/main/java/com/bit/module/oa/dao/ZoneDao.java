package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Zone;
import com.bit.module.oa.vo.zone.SimpleZoneVO;
import com.bit.module.oa.vo.zone.ZoneSpotVO;
import com.bit.module.oa.vo.zone.ZoneVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/13 16:18
 */
public interface ZoneDao {
    /**
     * 根据条件查询Zone
     * @param zoneVO
     * @return
     */
    List<Zone> findByConditionPage(ZoneVO zoneVO);

    /**
     * 查询所有Zone
     * @return
     */
    List<SimpleZoneVO> findAll(@Param(value="sorter")String sorter);
    /**
     * 通过主键查询单个Zone
     * @param id
     * @return
     */
    Zone findById(@Param(value="id")Long id);
    /**
     * 保存Zone
     * @param zone
     */
    void add(Zone zone);
    /**
     * 更新Zone
     * @param zone
     */
    void update(Zone zone);
    /**
     * 删除Zone
     * @param id
     */
    void delete(@Param(value="id")Long id);

    /**
     * 修改Zone状态
     * @param id
     * @param status
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    List<ZoneSpotVO> findZoneSpotVO(Zone zone);

    Integer existAvailableZone(@Param("id") Long id);
}
