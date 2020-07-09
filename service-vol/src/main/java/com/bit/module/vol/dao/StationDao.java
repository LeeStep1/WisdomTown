package com.bit.module.vol.dao;

import com.bit.module.vol.bean.Station;
import com.bit.module.vol.vo.StationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 9:05
 */
@Repository
public interface StationDao {
    /**
     * 新增记录
     * @param station
     */
    void add(Station station);

    /**
     * 单查记录
     * @param id
     * @return
     */
    Station findById(@Param(value = "id")Long id);

    /**
     * 分页查询
     * @param stationVO
     * @return
     */
    List<Station> listPage(StationVO stationVO);

    /**
     * 更新记录
     * @param station
     */
    void update(Station station);

    /**
     * 查询所有记录
     * @return
     */
    List<Station> findAll();

    /**
     * 查询子站点
     * @param stationId
     * @return
     */
    List<Station> findSubStation(@Param(value = "stationId")String stationId);

    /**
     * 查询同样站点代码的数量
     * @param stationCode
     * @return
     */
    Integer countSameCode(@Param(value = "stationCode")String stationCode);

    /**
     * 查询相同站名的记录数量
     * @param stationName
     * @return
     */
    Integer countSameName(@Param(value = "stationName")String stationName);

    /**
     * 模糊查询激活的id集合
     * @param updid
     * @return
     */
    List<Long> getMaxIds(String updid);


    /**
     * 查询下级服务站不包含自己
     * @param stationId
     * @return
     */
    List<Station> findSubStationExcludeSelfSql(@Param(value = "stationId")String stationId);

    /**
     * 根据站点id批量查询
     * @param stationIds
     * @return
     */
    List<Station> batchSelectByIds(@Param(value = "stationIds") List<Long> stationIds);

    /**
     * 不分页查询站点记录
     * @param station
     * @return
     */
    List<Station> listPageForExcel(Station station);

    /**
     * 批量更新站点
     * @param stationList
     */
    void batchUpdate(@Param(value = "stationList")List<Station> stationList);
    /**
     * 批量查询服务站
     * @param stationIds
     * @return
     */
    List<Station> batchSelectByStationIds(@Param(value = "stationIds")List<Long> stationIds);
    /**
     * 查询顶级站点
     * @return
     */
    Long findTopStation();

    /**
     * 查询顶级站点记录信息
     * @return
     */
    Station findTopStationInfo();
    /**
     * 根据参数查询
     * @return
     */
    List<Station> findParam(Station station);

    /**
     * 根据stationCode的首字母查询stationCode
     * @param letter
     * @return
     */
    List<String> findStationCodeByLetter(@Param(value = "letter") String letter);
}
