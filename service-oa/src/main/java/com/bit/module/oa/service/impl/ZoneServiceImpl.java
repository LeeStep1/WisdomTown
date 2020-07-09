package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Spot;
import com.bit.module.oa.bean.Zone;
import com.bit.module.oa.dao.SpotDao;
import com.bit.module.oa.dao.ZoneDao;
import com.bit.module.oa.enums.SpotStatusEnum;
import com.bit.module.oa.enums.ZoneStatusEnum;
import com.bit.module.oa.service.ZoneService;
import com.bit.module.oa.vo.zone.SimpleZoneVO;
import com.bit.module.oa.vo.zone.ZoneQO;
import com.bit.module.oa.vo.zone.ZoneSpotVO;
import com.bit.module.oa.vo.zone.ZoneVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Zone的Service实现类
 *
 * @author codeGenerator
 */
@Service("zoneService")
@Transactional
public class ZoneServiceImpl extends BaseService implements ZoneService {

    private static final Logger logger = LoggerFactory.getLogger(ZoneServiceImpl.class);

    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private SpotDao spotDao;

    /**
     * 根据条件查询Zone
     *
     * @param zoneVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(ZoneVO zoneVO) {
        PageHelper.startPage(zoneVO.getPageNum(), zoneVO.getPageSize());
        List<Zone> list = zoneDao.findByConditionPage(zoneVO);
        PageInfo<Zone> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有区域的基本信息
     *
     * @param name 区域名
     * @return
     */
    @Override
    public List<SimpleZoneVO> findAll(String name) {
        return zoneDao.findAll("id");
    }

    /**
     * 通过主键查询单个Zone
     *
     * @param id
     * @return
     */
    @Override
    public Zone findById(Long id) {
        return zoneDao.findById(id);
    }

    /**
     * 保存Zone
     *
     * @param zone
     */
    @Override
    public void add(Zone zone) {
        zoneDao.add(zone);
    }

    /**
     * 更新Zone
     *
     * @param zone
     */
    @Override
    public void update(Zone zone) {
        Zone toUpdate = zoneDao.findById(zone.getId());
        if (toUpdate == null) {
            logger.error("区域不存在, {}", zone);
            throw new BusinessException("区域不存在");
        }
        if (ZoneStatusEnum.DISABLE.getKey().equals(toUpdate.getStatus())) {
            logger.error("区域已停用, {}", toUpdate);
            throw new BusinessException("区域已停用");
        }
        zoneDao.update(zone);
    }

    @Override
    public void convertStatus(Long id, Integer status) {
        Zone toConvertStatusZone = zoneDao.findById(id);
        if (toConvertStatusZone == null) {
            logger.error("区域{}不存在", id);
            throw new BusinessException("区域不存在");
        }
        if (toConvertStatusZone.getStatus().equals(status)) {
            logger.error("区域的状态已经是{}", status);
            throw new BusinessException("区域状态不匹配");
        }
        zoneDao.updateStatus(id, status);
        // 停用区域需要把下属的签到点也停用
        if (status.equals(ZoneStatusEnum.ENABLED.getKey())) {
            return;
        }
        Spot spot = new Spot();
        spot.setZoneId(id);
        spot.setStatus(SpotStatusEnum.ENABLED.getKey());
        List<Long> toDisableSpotId = spotDao.findByNameAndZoneIdAndStatus(spot).stream().map(Spot::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(toDisableSpotId)) {
            return;
        }
        spotDao.disableStatus(toDisableSpotId);
    }

    @Override
    public BaseVo findZoneSpotVO(ZoneQO zone) {
        List<ZoneSpotVO> zoneSpotVO = zoneDao.findZoneSpotVO(zone);
        if (!zone.getQueryType()) {
            zoneSpotVO = zoneSpotVO.stream().filter(z -> CollectionUtils.isNotEmpty(z.getSpots()))
                    .collect(Collectors.toList());
        }
        return new BaseVo(zoneSpotVO);
    }

    /**
     * 删除Zone
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Spot spot = new Spot();
        spot.setZoneId(id);
        List<Long> toDisableSpotId = spotDao.findByNameAndZoneIdAndStatus(spot).stream().map(Spot::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(toDisableSpotId)) {
            spotDao.batchDelete(toDisableSpotId);
        }
        zoneDao.delete(id);
    }
}
