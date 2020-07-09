package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.CivilizationBehavior;
import com.bit.module.cbo.vo.CivilizationBehaviorPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 文明行为相关dao
 * @author: liyang
 * @date: 2019-08-31
 **/
public interface CivilizationBehaviorDao {
    /**
     * 删除记录
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 增加一条文明行为
     * @param record 新增详情
     * @return
     */
    void add(CivilizationBehavior record);

    /**
     * 根据主键查询明细
     * @param id 主键ID
     * @return
     */
    CivilizationBehavior selectByPrimaryKey(Long id);

    /**
     * 根据主键和状态查询明细
     * @param id     主键
     * @param status    状态
     * @return
     */
    CivilizationBehavior selectByIdAndStatus(@Param("id") Long id,@Param("status")Integer status);

    /**
     * 修改文明行为状态
     * @param record 修改内容
     * @return
     */
    int updateByPrimaryKeySelective(CivilizationBehavior record);

    /**
     * 查询文明上报列表
     * @param record 查询条件
     * @return
     */
    List<CivilizationBehavior> findAll(@Param("record") CivilizationBehaviorPageVO record);
}