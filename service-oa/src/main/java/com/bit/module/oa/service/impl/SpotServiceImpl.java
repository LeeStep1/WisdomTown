package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Spot;
import com.bit.module.oa.dao.SpotDao;
import com.bit.module.oa.dao.ZoneDao;
import com.bit.module.oa.enums.SpotStatusEnum;
import com.bit.module.oa.service.SpotService;
import com.bit.module.oa.vo.spot.SpotDetailVO;
import com.bit.module.oa.vo.spot.SpotVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spot的Service实现类
 *
 * @author codeGenerator
 */
@Service("spotService")
@Transactional
public class SpotServiceImpl extends BaseService implements SpotService {

    private static final Logger logger = LoggerFactory.getLogger(SpotServiceImpl.class);

    @Autowired
    private SpotDao spotDao;

    @Autowired
    private ZoneDao zoneDao;

    /**
     * 根据条件查询Spot
     *
     * @param spotVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(SpotVO spotVO) {
        PageHelper.startPage(spotVO.getPageNum(), spotVO.getPageSize());
        List<Spot> list = spotDao.findByConditionPage(spotVO);
        PageInfo<Spot> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有签到点的基本信息
     *
     * @param name 签到点名
     * @return
     */
    @Override
    public List<Spot> findAll(String name) {
        return spotDao.findAll("id");
    }

    /**
     * 通过主键查询单个Spot
     *
     * @param id
     * @return
     */
    @Override
    public SpotDetailVO findById(Long id) {
        Spot itself = spotDao.findById(id);
        SpotDetailVO spotDetail = new SpotDetailVO();
        BeanUtils.copyProperties(itself, spotDetail);
        itself.setName(null);
        itself.setStatus(null);
        List<Spot> other = spotDao.findByNameAndZoneIdAndStatus(itself).stream()
                .filter(spot -> !spot.getId().equals(itself.getId())).collect(Collectors.toList());
        spotDetail.setOtherSpot(other);
        return spotDetail;
    }

    /**
     * 保存Spot
     *
     * @param spot
     */
    @Override
    public void add(Spot spot) {
        if (zoneDao.existAvailableZone(spot.getZoneId()) <= 0) {
            throw new BusinessException("所选区域不存在或未启用");
        }

        /*if (SpotStatusEnum.ENABLED.getKey().equals(spot.getStatus())) {
            checkSpotDistance(spot);
        }*/
        spotDao.add(spot);
    }

    /**
     * 更新Spot
     *
     * @param spot
     */
    @Override
    public void update(Spot spot) {
        if (spot.getZoneId() != null && zoneDao.existAvailableZone(spot.getZoneId()) <= 0) {
            logger.error("所选区域不存在或未启用, {}", spot);
            throw new BusinessException("所选区域不存在或未启用");
        }
        Spot toUpdate = spotDao.findById(spot.getId());
        if (toUpdate == null) {
            logger.error("签到点不存在, {}", spot);
            throw new BusinessException("签到点不存在");
        }
        if (SpotStatusEnum.DISABLE.getKey().equals(toUpdate.getStatus())) {
            logger.error("签到点已停用, {}", toUpdate);
            throw new BusinessException("签到点已停用");
        }
        /*if (SpotStatusEnum.ENABLED.getKey().equals(spot.getStatus()) && spot.getLat() != null && spot.getLng() != null) {
            checkSpotDistance(spot);
        }*/
        spotDao.update(spot);
    }

    @Override
    public void convertStatus(Long id, Integer status) {
        Spot toConvertStatusSpot = spotDao.findById(id);
        if (toConvertStatusSpot == null) {
            logger.error("签到点{}不存在", id);
            throw new BusinessException("签到点不存在");
        }
        if (zoneDao.existAvailableZone(toConvertStatusSpot.getZoneId()) <= 0) {
            logger.error("所在区域不存在或未启用, {}", toConvertStatusSpot);
            throw new BusinessException("所在区域不存在或未启用");
        }
        if (toConvertStatusSpot.getStatus().equals(status)) {
            logger.error("签到点状态不匹配 : {}", status);
            throw new BusinessException("签到点状态不匹配");
        }
        /*if (SpotStatusEnum.ENABLED.getKey().equals(status)) {
            checkSpotDistance(toConvertStatusSpot);
        }*/
        spotDao.updateStatus(id, status);
    }

    @Override
    public void delete(Long id) {
        spotDao.delete(id);
    }

    /*private void checkSpotDistance(Spot toConvertStatusSpot) {
        Spot toQueryDistance = new Spot();
        toQueryDistance.setZoneId(toConvertStatusSpot.getZoneId());
        List<Spot> otherSpots = spotDao.findByNameAndZoneIdAndStatus(toQueryDistance);
        // 距离校验
        for (Spot other : otherSpots) {
            // 与其他签到点的距离
            double distance = LocationUtils
                    .getDistance(other.getLat(), other.getLng(), toConvertStatusSpot.getLat(),
                            toConvertStatusSpot.getLng());
            if (distance < 50) {
                logger.error("签到点与其他该区域下的签到点重合{}", other);
                throw new BusinessException("签到点与其他该区域下的签到点重合");
            }
        }
    }*/
}
