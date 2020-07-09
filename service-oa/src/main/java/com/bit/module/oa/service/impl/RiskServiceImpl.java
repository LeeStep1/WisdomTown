package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.bean.Risk;
import com.bit.module.oa.dao.LogDao;
import com.bit.module.oa.dao.RiskDao;
import com.bit.module.oa.enums.LogOperateEnum;
import com.bit.module.oa.enums.LogTypeEnum;
import com.bit.module.oa.enums.RiskStatusEnum;
import com.bit.module.oa.service.RiskService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.vo.risk.RiskExportQO;
import com.bit.module.oa.vo.risk.RiskExportVO;
import com.bit.module.oa.vo.risk.RiskVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Risk的Service实现类
 *
 * @author codeGenerator
 */
@Service("riskService")
@Transactional
public class RiskServiceImpl extends BaseService implements RiskService {

    private static final Logger logger = LoggerFactory.getLogger(RiskServiceImpl.class);

    @Autowired
    private RiskDao riskDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private PushUtil pushUtil;

    @Value("${oa.flow.risk}")
    private Integer riskFlowId;

    /**
     * 根据条件查询Risk
     *
     * @param riskVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(RiskVO riskVO) {
        PageHelper.startPage(riskVO.getPageNum(), riskVO.getPageSize());
        riskVO.setOrderBy("report_time");
        riskVO.setOrder("desc");
        List<Risk> list = riskDao.findByConditionPage(riskVO);
        PageInfo<Risk> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 通过主键查询单个Risk
     *
     * @param id
     * @return
     */
    @Override
    public Risk findDetailById(Long id) {
        return riskDao.findById(id);
    }

    /**
     * 保存Risk
     *
     * @param risk
     */
    @Override
    public void add(Risk risk) {
        UserInfo userInfo = this.getCurrentUserInfo();
        risk.setReporterId(userInfo.getId());
        risk.setReporterName(userInfo.getRealName());
        risk.setStatus(RiskStatusEnum.SUBMIT.getKey());
        risk.setReportTime(new Date());
        risk.setNo(generateApplyNo());
        logger.info("新增隐患报告: {}", risk);
        riskDao.add(risk);
        Log log = Log.getLogByOperateAndType(risk.getId(), userInfo, LogOperateEnum.REPORT, LogTypeEnum.RISK);
        logDao.add(log);

        Risk detail = riskDao.findById(risk.getId());
        // 发送推送
        pushUtil.sendMessageByFlow(detail.getId(), riskFlowId, MessageTemplateEnum.RISK_SUBMIT,
                TargetTypeEnum.USER, new String[]{userInfo.getRealName(), MessageTemplateEnum.RISK_SUBMIT.getInfo()},
                userInfo.getRealName(), detail.getVersion());
    }


    /**
     * 更新Risk
     *
     * @param risk
     */
    @Override
    public void feedback(Risk risk) {

        Risk toUpdate = riskDao.findById(risk.getId());
        if (toUpdate == null) {
            logger.error("隐患报告不存在, {}", risk);
            throw new BusinessException("隐患报告不存在");
        }
        if (RiskStatusEnum.FEEDBACK.getKey().equals(toUpdate.getStatus())) {
            logger.error("隐患报告已反馈, {}", risk);
            throw new BusinessException("隐患报告已反馈，无需重复反馈。");
        }
        toUpdate.setStatus(RiskStatusEnum.FEEDBACK.getKey());
        logger.info("反馈隐患报告: {}", toUpdate);
        riskDao.update(toUpdate);
        Log log = Log.getLogByOperateAndType(risk.getId(), this.getCurrentUserInfo(), LogOperateEnum.CONFIRM_REPORT, LogTypeEnum.RISK);
        logDao.add(log);

        // 发送反馈隐患消息通知
        pushUtil.sendMessage(toUpdate.getId(), MessageTemplateEnum.RISK_FEEDBACK, TargetTypeEnum.USER,
                new Long[]{toUpdate.getReporterId()},
                new String[]{getCurrentUserInfo().getRealName(), MessageTemplateEnum.RISK_FEEDBACK.getInfo()},
                getCurrentUserInfo().getRealName());
    }

    @Override
    public List<RiskExportVO> exportByConditionPage(RiskExportQO qo) {
        return riskDao.findByConditionExportList(qo);
    }


    /**
     * 生成隐患报告单
     * 规则 : 拼音首字母-年月日-当日派单号
     * 如公务用车 2018年12月1日第二张派车单 : GW-20181201-0002
     */
    private String generateApplyNo() {
        String lastApplyNo = riskDao.findNo();
        return ApplyNoGenerator.generateApplyNo(lastApplyNo, "YH");
    }
}
