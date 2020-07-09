package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Spot;
import com.bit.module.oa.vo.spot.SpotVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/13 16:18
 */
public interface SpotDao {
    /**
     * 根据条件查询Spot
     * @param spotVO
     * @return
     */
    List<Spot> findByConditionPage(SpotVO spotVO);

    /**
     * 查询所有Spot
     * @return
     */
    List<Spot> findAll(@Param(value = "sorter") String sorter);
    /**
     * 查询根据条件搜索SimpleSpotVO
     * @return
     */
    List<Spot> findByNameAndZoneIdAndStatus(Spot spot);
    /**
     * 通过主键查询单个Spot
     * @param id
     * @return
     */
    Spot findById(@Param(value = "id") Long id);

    List<Spot> findByIds(@Param(value = "ids") List<Long> ids);
    /**
     * 保存Spot
     * @param spot
     */
    void add(Spot spot);
    /**
     * 更新Spot
     * @param spot
     */
    void update(Spot spot);
    /**
     * 删除Spot
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 修改Spot状态
     * @param id
     * @param status
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 禁用Spot
     * @param ids
     */
    void disableStatus(@Param("ids") List<Long> ids);

    void batchDelete(@Param("ids") List<Long> ids);
}
