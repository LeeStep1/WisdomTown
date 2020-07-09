package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Station;
import com.bit.module.vol.vo.StationVO;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 9:03
 */
public interface StationService {
    /**
     * 添加站点
     * @param station
     */
    BaseVo add(Station station);
    /**
     * 反显站点
     * @param id
     * @return
     */
    Station reflectById(Long id);
    /**
     * 分页查询
     * @param stationVO
     * @return
     */
    BaseVo listPage(StationVO stationVO);
    /**
     * 更改记录状态
     * @param station
     * @return
     */
    BaseVo update(Station station);
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    void exportToExcel(String stationName,
                       String firstChargeMan,
                       String firstChargeManMobile,
                       Integer stationStatus,
                       Integer partnerOrgType,
                       HttpServletResponse response,
                       HttpServletRequest request,
                       Integer type);
    /**
     * 生成服务站树
     * @param stationId
     * @return
     */
    List<Station> tree(Long stationId);
    /**
     * 子节点
     * @param stationId
     * @return
     */
    List<Station> childTree(Long stationId);
    /**
     * 判断机构编号是否重复
     * @param code
     * @return
     */
    Integer countSameCode(String code);
    /**
     * 根据志愿者id查询服务站信息
     * @param volunteerId
     * @return
     */
    BaseVo queryStationByVolunteerId(Long volunteerId);
    /**
     * 查询所有的站点
     * @return
     */
    BaseVo findAllStation();

    /**
     * 查询下级服务站 不包含自己
     * @param stationId
     * @return
     */
    List<Station> childTreeExcludeSelf(Long stationId);
    /**
     * 查询站点资料
     * @param stationId
     * @return
     */
    BaseVo data(Long stationId);

    /**
     * 批量查询服务站
     * @param stationIds
     * @return
     */
    BaseVo batchSelectByStationIds(List<Long> stationIds);
    /**
     * 停用服务站
     * @param id
     * @return
     */
    BaseVo deactivate(Long id);
    /**
     * 查询顶级站点
     * @return
     */
    Long findTopStation();
    /**
     * 查询顶级站点信息
     * @return
     */
    BaseVo findTopStationDetail();
}
