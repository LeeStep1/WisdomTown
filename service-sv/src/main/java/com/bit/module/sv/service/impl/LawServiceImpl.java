package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Law;
import com.bit.module.sv.dao.LawDao;
import com.bit.module.sv.dao.RegulationContentDao;
import com.bit.module.sv.dao.RegulationDao;
import com.bit.module.sv.service.LawService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.vo.LawVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("lawService")
@Transactional
public class LawServiceImpl extends BaseService implements LawService {

    private static final Logger logger = LoggerFactory.getLogger(LawServiceImpl.class);

    @Autowired
    private LawDao lawDao;

    @Autowired
    private RegulationDao regulationDao;

    @Autowired
    private RegulationContentDao regulationContentDao;

    @Override
    public void addLaw(LawVO lawVO) {
        Law add = new Law();
        BeanUtils.copyProperties(lawVO, add);
        add.setCreateAt(DateUtils.getCurrentDate());
        add.setUpdateAt(add.getCreateAt());
        int result = lawDao.insert(add);
        logger.info("新增法律 result: {}", result);
    }

    @Override
    public BaseVo listLaws(LawVO lawVO) {
        PageHelper.startPage(lawVO.getPageNum(), lawVO.getPageSize());
        lawVO.setOrderBy("id");
        lawVO.setOrder("desc");
        List<Law> laws = lawDao.findByCondition(lawVO);
        PageInfo<Law> pageInfo = new PageInfo<>(laws);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public void deleteById(Long id) {
        Law toDelete = lawDao.selectByPrimaryKey(id);
        if (toDelete == null) {
            logger.error("法律不存在, {}", id);
            throw new BusinessException("法律不存在");
        }
        logger.info("删除法律 : {}", toDelete);
        List<Long> regulationIds = regulationDao.findIdByLawId(id);
        if (CollectionUtils.isNotEmpty(regulationIds)) {
            // 删除正文
            regulationContentDao.deleteByRegulationIdIn(regulationIds);
        }
        // 删除法规
        regulationDao.deleteByLawId(id);
        // 删除法律
        lawDao.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Law law) {
        Law toUpdate = lawDao.selectByPrimaryKey(law.getId());
        if (toUpdate == null) {
            logger.error("法律不存在, {}", law);
            throw new BusinessException("法律不存在");
        }
        toUpdate.updateLaw(law);
        lawDao.updateByPrimaryKeySelective(toUpdate);
        logger.info("更新法律 : {}", toUpdate);
    }

    @Override
    public BaseVo allLaws(Integer range) {
        return new BaseVo(lawDao.findAll(range));
    }
}
