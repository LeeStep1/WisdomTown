package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.vo.DictVO;

import java.util.List;

public interface DictService {

    /**
     * 根据条件查询Dict,分页查询
     * @param dictVO
     * @return BaseVo
     */
    BaseVo findByConditionPage(DictVO dictVO);

    /**
     * 通过主键查询单个Dict
     * @param id
     * @return Dict
     */
    Dict findById(Long id);

    /**
     * 保存Dict
     * @param dict
     */
    void add(Dict dict);

    /**
     * 更新Dict
     * @param dict
     */
    void update(Dict dict);

    /**
     * 删除Dict
     * @param id
     */
    void delete(Long id);

    /**
     * 根据模块查询字典
     * @param module
     * @return
     */
    List<Dict> findByModule(String module);

    /**
     * 根据模块和code查询，用于回显
     * @param dict
     * @return
     */
    BaseVo findByModuleAndCode(Dict dict);

    /**
     * 根据模块和codes查询，用于回显
     * @param dictVO
     * @return
     */
    BaseVo findByModuleAndCodes(DictVO dictVO);
    /**
     * 根据模块名称集合批量查询字典表
     * @param dict
     * @return
     */
    BaseVo findByModules(Dict dict);
    /**
     * 根据当前用户查询类目
     * @param messageTemplate
     * @return
     */
    BaseVo queryByAppId(MessageTemplate messageTemplate);
}
