package com.bit.module.vol.dao;

import com.bit.module.vol.bean.VolNews;
import com.bit.module.vol.vo.VolNewsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  志愿者风采数据库管理
 *  @author Liy
 */
@Repository
public interface VolunteerNewsDao {

    /**
     * 增加志愿者风采
     * @param volNews
     */
    void add(@Param("volNews") VolNews volNews);

    /**
     * 保存修改
     * @param volNews
     */
    void update(@Param("volNews") VolNews volNews);

    /**
     * 分页查询自己志愿者风采
     * @param volNewsVo
     * @return
     */
    List<VolNews> getAllNewsSql(@Param("volNewsVo") VolNewsVo volNewsVo);

    /**
     * 根据ID 获取新闻版本
     * @param volNews
     * @return
     */
    VolNews getNewsVersionByIdSql(@Param("volNews") VolNews volNews);

    /**
     *根据ID和锁 修改状态
     * @param volNews
     */
    void auditNewsByIdSql(@Param("volNews") VolNews volNews);

    /**
     * 根据ID 获取文章明细
     * @param volNews
     * @return VolNewsVo 带分页
     */
    VolNewsVo getNewsContextByIdSql(@Param("volNews") VolNews volNews);

    /**
     * 根据ID 删除文章
     * @param volNews
     */
    void delNewsByIdSql(@Param("volNews") VolNews volNews);

    /**
     * 获取APP端展示新闻
     * @param volNewsVo
     * @return
     */
    List<VolNews> getNewsForAppShowSql(@Param("volNewsVo") VolNewsVo volNewsVo);

    /**
     * 分页查询下级志愿者风采
     * @param volNewsVo
     * @return
     */
    List<VolNews> getChildNewsSql(@Param("volNewsVo") VolNewsVo volNewsVo);

    /**
     * 获取文章明细
     * @param volNews
     * @return VolNews 不带分页
     */
    VolNews getNewsDetailsSql(@Param("volNews") VolNews volNews);

}
