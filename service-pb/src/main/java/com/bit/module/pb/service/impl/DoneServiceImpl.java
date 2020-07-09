package com.bit.module.pb.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Done;
import com.bit.module.pb.dao.DoneDao;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.service.DoneService;
import com.bit.module.pb.vo.DoneVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Done的Service实现类
 *
 * @author codeGenerator
 */
@Slf4j
@Service("doneService")
public class DoneServiceImpl extends BaseService implements DoneService {

    @Autowired
    private DoneDao doneDao;

    @Autowired
    private OrganizationDao organizationDao;

    /**
     * 根据条件查询Done
     *
     * @param doneVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(DoneVO doneVO) {
        PageHelper.startPage(doneVO.getPageNum(), doneVO.getPageSize());
        List<Done> list = doneDao.findByConditionPage(doneVO);
        PageInfo<Done> pageInfo = new PageInfo<Done>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有Done
     *
     * @param sorter 排序字符串
     * @return
     */
    @Override
    public List<Done> findAll(String sorter) {
        return doneDao.findAll(sorter);
    }

    /**
     * 通过主键查询单个Done
     *
     * @param id
     * @return
     */
    @Override
    public Done findById(Long id) {
        return doneDao.findById(id);
    }

    /**
     * 批量保存Done
     *
     * @param dones
     */
    @Override
    public void batchAdd(List<Done> dones) {
        doneDao.batchAdd(dones);
    }

    /**
     * 保存Done
     *
     * @param done
     */
    @Override
    public void add(Done done) {
        doneDao.add(done);
    }

    /**
     * 批量更新Done
     *
     * @param dones
     */
    @Override
    public void batchUpdate(List<Done> dones) {
        doneDao.batchUpdate(dones);
    }

    /**
     * 更新Done
     *
     * @param done
     */
    @Override
    public void update(Done done) {
        doneDao.update(done);
    }

    /**
     * 删除Done
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        doneDao.batchDelete(ids);
    }

    @Override
    public List<Done> findRecord(Long correlationId) {
        return doneDao.findRecord(correlationId);
    }

    @Override
    public Done findOutreason(Long correlationId) {
        return doneDao.findOutreason(correlationId, RelationshipTransferServiceImpl.TopicTypeEnum.PARTYMEMBER.getValue(), PartyMemberApprovalServiceImpl.ActionPartyMemberEnum.DISABLE.getValue());
    }

    @Override
    public Done getRelativeLastRecord(DoneVO doneVO) {
        doneVO.setOrgId(Long.valueOf(getCurrentUserInfo().getPbOrgId()));
        doneVO.setContent("退回申请");
        PageHelper.startPage(doneVO.getPageNum(), doneVO.getPageSize()).setOrderByOnly(true);
        return doneDao.getRelativeLastRecord(doneVO);
    }

    /**
     * 删除Done
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        doneDao.delete(id);
    }

}
