package com.bit.soft.configserver.service.imp;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.ConfigProperties;
import com.bit.soft.configserver.dao.ConfigDao;
import com.bit.soft.configserver.service.ConfigPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 属性设置实现
 * @author liy
 */
@Service
public class ConfigPropertiesServiceImp extends BaseService implements ConfigPropertiesService {

    @Autowired
    private ConfigDao configDao;

    /**
     * 增加属性
     * @param configProperties 属性明细
     * @return BaseVo
     */
    @Override
    public BaseVo insertProperties(ConfigProperties configProperties) {

        try {
            configDao.insertPropertiesSql(configProperties);
        }catch (Exception ex){
            throw new BusinessException("插入失败");
        }


        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("1");

        return baseVo;
    }

    /**
     * 删除属性
     * @param id 需要删除的ID
     * @return BaseVo
     */
    @Override
    public BaseVo delProperties(int id) {

        configDao.delPropertiesSql(id);

        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("1");
        return baseVo;
    }

    /**
     * 修改属性
     * @param configProperties 修改的明细
     * @return BaseVo
     */
    @Override
    public BaseVo updateProperties(ConfigProperties configProperties) {

        configDao.updatePropertiesSql(configProperties);

        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("1");
        return baseVo;
    }

    /**
     * 查看属性
     * @return BaseVo
     */
    @Override
    public BaseVo selectProperties() {

        List<ConfigProperties> configPropertiesList = configDao.selectPropertiesSql();

        BaseVo baseVo = new BaseVo();
        baseVo.setData(configPropertiesList);
        baseVo.setMsg("1");
        return baseVo;
    }

}
