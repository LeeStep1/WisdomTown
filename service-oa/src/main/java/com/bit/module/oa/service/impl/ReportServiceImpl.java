package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.bean.Inspect;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.bean.Report;
import com.bit.module.oa.dao.ExecutorDao;
import com.bit.module.oa.dao.InspectDao;
import com.bit.module.oa.dao.LogDao;
import com.bit.module.oa.dao.ReportDao;
import com.bit.module.oa.enums.InspectStatusEnum;
import com.bit.module.oa.enums.LogOperateEnum;
import com.bit.module.oa.enums.LogTypeEnum;
import com.bit.module.oa.enums.ReportStatusEnum;
import com.bit.module.oa.service.ReportService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.vo.report.ReportVO;
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
 * Report的Service实现类
 *
 * @author codeGenerator
 */
@Service("reportService")
@Transactional
public class ReportServiceImpl extends BaseService implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private InspectDao inspectDao;

    @Autowired
    private ExecutorDao executorDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private PushUtil pushUtil;

    @Value("${oa.flow.report}")
    private Integer reportFlowId;

    /**
     * 根据条件查询Report
     *
     * @param reportVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(ReportVO reportVO) {
        PageHelper.startPage(reportVO.getPageNum(), reportVO.getPageSize());
        List<Report> list = reportDao.findByConditionPage(reportVO);
        PageInfo<Report> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 通过主键查询单个Report
     *
     * @param id
     * @return
     */
    @Override
    public Report findById(Long id) {
        return reportDao.findById(id);
    }

    /**
     * 批量保存Report
     *
     * @param reports
     */
    @Override
    public void batchAdd(List<Report> reports) {
        reportDao.batchAdd(reports);
    }

    /**
     * 保存Report
     *
     * @param report
     */
    @Override
    public void add(Report report) {
        Executor toCheck = executorDao
                .findByInspectIdAndExecutorId(report.getInspectId(), getCurrentUserInfo().getId());
        if (toCheck == null || !InspectStatusEnum.EXECUTE.getKey().equals(toCheck.getStatus())) {
            logger.error("任务不存在, {}", toCheck);
            throw new BusinessException("任务签到点不存在");
        }
        Inspect inspect = inspectDao.findById(report.getInspectId());
        String lastReportByInspectId = reportDao.findLastReportByInspectId(inspect.getId());
        // 生成上报单
        report.setNo(ApplyNoGenerator.generateChildApplyNo(inspect.getNo(), lastReportByInspectId));
        UserInfo userInfo = getCurrentUserInfo();
        report.setReporterId(userInfo.getId());
        report.setReporterName(userInfo.getRealName());
        Date reportTime = new Date();
        report.setCreateAt(reportTime);
        report.setStatus(ReportStatusEnum.SUBMIT.getKey());
        Log log = Log.getLogByOperateAndType(report.getInspectId(), getCurrentUserInfo(), LogOperateEnum.REPORT, LogTypeEnum
                .INSPECT);
        logDao.add(log);
        checkData(report);
        reportDao.add(report);
        inspect.setLastReportAt(reportTime);
        inspectDao.updateLastReportAt(inspect);


        Report detail = reportDao.findById(report.getId());
        // 发送推送
        pushUtil.sendMessageByFlow(detail.getId(), reportFlowId, MessageTemplateEnum.EXCEPTION_SUBMIT,
                TargetTypeEnum.USER, new String[]{userInfo.getRealName(), MessageTemplateEnum.EXCEPTION_SUBMIT.getInfo()},
                userInfo.getRealName(), detail.getVersion());
    }

    /**
     * 更新Report
     *
     * @param report
     */
    @Override
    public void confirm(Report report) {
        Report toUpdate = reportDao.findById(report.getId());
        if (toUpdate == null) {
            logger.error("上报记录不存在, {}", report);
            throw new BusinessException("上报记录不存在");
        }
        if (ReportStatusEnum.CONFIRM.getKey().equals(toUpdate.getStatus())) {
            logger.error("上报记录无需重复确认, {}", toUpdate);
            throw new BusinessException("上报记录无需重复确认");
        }
        toUpdate.setStatus(ReportStatusEnum.CONFIRM.getKey());
        reportDao.updateConfirmStatus(toUpdate);
        Log log = Log.getLogByOperateAndType(toUpdate.getInspectId(), getCurrentUserInfo(), LogOperateEnum.CONFIRM_REPORT,
                LogTypeEnum.INSPECT);
        logDao.add(log);

        // 发送确认异常上报消息通知
        pushUtil.sendMessage(toUpdate.getId(), MessageTemplateEnum.EXCEPTION_FEEDBACK, TargetTypeEnum.USER,
                new Long[]{toUpdate.getReporterId()},
                new String[]{getCurrentUserInfo().getRealName(), MessageTemplateEnum.EXCEPTION_FEEDBACK.getInfo()},
                getCurrentUserInfo().getRealName());
    }

    /**
     * 删除Report
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        reportDao.batchDelete(ids);
    }

    /**
     * 删除Report
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        reportDao.delete(id);
    }

    private void checkData(Report report) {
        if (!report.getAbnormal()) {
            report.setNeedFix(false);
        }
        if (!report.getNeedFix()) {
            report.setFixContent(null);
            report.setFixPicUrls(null);
        }
    }
}