package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.VehicleApplication;
import com.bit.module.oa.service.VehicleApplicationService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.vehicleApplication.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * VehicleApplication的相关请求
 *
 * @author generator
 */
@RestController
@RequestMapping(value = "/vehicleApplication")
public class VehicleApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleApplicationController.class);
    @Autowired
    private VehicleApplicationService vehicleApplicationService;


    /**
     * 分页查询VehicleApplication列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VehicleApplicationVO vehicleApplicationVO) {
        //分页对象，前台传递的包含查询的参数

        return vehicleApplicationService.findByConditionPage(vehicleApplicationVO);
    }

    /**
     * 我的用车分页
     * @param vehicleApplicationQO
     * @return
     */
    @PostMapping("/use/listPage")
    public BaseVo myVehicleUsingListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        return vehicleApplicationService.findMyVehicleApplicationPage(vehicleApplicationQO);
    }

    /**
     * 用车导出
     * @param userId
     * @param planStartTime
     * @param planEndTime
     * @param minApplyTime
     * @param maxApplyTime
     * @param nature
     * @param usage
     * @param status
     * @param applyNo
     * @param response
     */
    @PostMapping("/use/export")
    public void myVehicleUsingExport(@RequestParam(name = "userId") Long userId,
                                     @RequestParam(name = "planStartTime", required = false) Long planStartTime,
                                     @RequestParam(name = "planEndTime", required = false) Long planEndTime,
                                     @RequestParam(name = "minApplyTime", required = false) Long minApplyTime,
                                     @RequestParam(name = "maxApplyTime", required = false) Long maxApplyTime,
                                     @RequestParam(name = "nature", required = false) Integer nature,
                                     @RequestParam(name = "usage", required = false) Integer usage,
                                     @RequestParam(name = "status", required = false) Integer status,
                                     @RequestParam(name = "applyNo", required = false) String applyNo,
                                     HttpServletResponse response) {
        try {
            VehicleApplicationExportQO vehicleApplicationQO = setProperty(userId, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, status, applyNo);
            List<MyVehicleApplicationExportVO> myVehicleApplicationExportVOS = vehicleApplicationService.findExportMyVehicleUsing(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            myVehicleApplicationExportVOS.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, myVehicleApplicationExportVOS, MyVehicleApplicationExportVO.class, "我的用车");
        } catch (IOException e) {
            logger.error("我的用车导出异常 : {}", e);
        }
    }

    private VehicleApplicationExportQO setProperty(Long userId, Long planStartTime, Long planEndTime, Long minApplyTime,
                                                   Long maxApplyTime, Integer nature, Integer usage, Integer status,
                                                   String applyNo) {
        VehicleApplicationExportQO vehicleApplicationQO = new VehicleApplicationExportQO();
        vehicleApplicationQO.setUserId(userId);
        vehicleApplicationQO.setPlanStartTime(planStartTime == null ? null : new Date(planStartTime));
        vehicleApplicationQO.setPlanEndTime(planEndTime == null ? null : new Date(planEndTime));
        vehicleApplicationQO.setMaxApplyTime(maxApplyTime == null ? null : new Date(maxApplyTime));
        vehicleApplicationQO.setMinApplyTime(minApplyTime == null ? null : new Date(minApplyTime));
        vehicleApplicationQO.setNature(nature);
        vehicleApplicationQO.setUsage(usage);
        vehicleApplicationQO.setStatus(status);
        vehicleApplicationQO.setApplyNo(applyNo);
        return vehicleApplicationQO;
    }

    /**
     * 车辆台账分页
     * @param vehicleApplicationQO
     * @return
     */
    @PostMapping("/ledger/listPage")
    public BaseVo ledgerListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        return vehicleApplicationService.findLedgerApplicationListPage(vehicleApplicationQO);
    }

    /**
     * 车辆台账导出文档
     * @param planStartTime
     * @param planEndTime
     * @param minApplyTime
     * @param maxApplyTime
     * @param nature
     * @param usage
     * @param response
     */
    @PostMapping("/ledger/export")
    public void ledgerExport(@RequestParam(name = "planStartTime", required = false) Long planStartTime,
                             @RequestParam(name = "planEndTime", required = false) Long planEndTime,
                             @RequestParam(name = "minApplyTime", required = false) Long minApplyTime,
                             @RequestParam(name = "maxApplyTime", required = false) Long maxApplyTime,
                             @RequestParam(name = "nature", required = false) Integer nature,
                             @RequestParam(name = "usage", required = false) Integer usage,
                             HttpServletResponse response) {
        try {
            VehicleApplicationExportQO vehicleApplicationQO = setProperty(null, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, null, null);
            List<HandleVehicleApplicationExportVO> driverExportVOList =
                    vehicleApplicationService.findLedgerApplicationExport(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            driverExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, driverExportVOList, HandleVehicleApplicationExportVO.class, "车辆台账");
        } catch (IOException e) {
            logger.error("车辆台账导出异常 : {}", e);
        }
    }

    /**
     * 派车管理
     * @param vehicleApplicationQO
     * @return
     */
    @PostMapping("/handle/listPage")
    public BaseVo handleListPage(@RequestBody VehicleApplicationQO vehicleApplicationQO) {
        return vehicleApplicationService.findHandleApplicationListPage(vehicleApplicationQO);
    }

    /**
     * 派车管理导出
     * @param planStartTime
     * @param planEndTime
     * @param minApplyTime
     * @param maxApplyTime
     * @param nature
     * @param usage
     * @param status
     * @param applyNo
     * @param response
     */
    @PostMapping("/handle/export")
    public void handleListExport(@RequestParam(name = "planStartTime", required = false) Long planStartTime,
                                 @RequestParam(name = "planEndTime", required = false) Long planEndTime,
                                 @RequestParam(name = "minApplyTime", required = false) Long minApplyTime,
                                 @RequestParam(name = "maxApplyTime", required = false) Long maxApplyTime,
                                 @RequestParam(name = "nature", required = false) Integer nature,
                                 @RequestParam(name = "usage", required = false) Integer usage,
                                 @RequestParam(name = "status", required = false) Integer status,
                                 @RequestParam(name = "applyNo", required = false) String applyNo,
                                 HttpServletResponse response) {
        try {
            VehicleApplicationExportQO vehicleApplicationQO = setProperty(null, planStartTime, planEndTime, minApplyTime,
                    maxApplyTime, nature, usage, status, applyNo);
            List<HandleVehicleApplicationExportVO> driverExportVOList =
                    vehicleApplicationService.findHandleApplicationExport(vehicleApplicationQO);
            AtomicLong num = new AtomicLong(1L);
            driverExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, driverExportVOList, HandleVehicleApplicationExportVO.class, "派车管理");
        } catch (IOException e) {
            logger.error("派车管理导出异常 : {}", e);
        }
    }

    /**
     * 根据主键ID查询VehicleApplication
     * @param toQuery
     * @return
     */
    @PostMapping("/detail")
    public BaseVo query(@RequestBody VehicleApplication toQuery) {
        toQuery.checkQuery();
        VehicleApplicationDetail vehicleApplication = vehicleApplicationService.findVehicleApplicationDetail(toQuery);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(vehicleApplication);
        return baseVo;
    }

    /**
     * 申请用车
     *
     * @param vehicleApplication VehicleApplication实体
     * @return
     */
    @PostMapping("/use/apply")
    public BaseVo apply(@RequestBody VehicleApplication vehicleApplication) {
        vehicleApplication.check();
        vehicleApplicationService.apply(vehicleApplication);
        return new BaseVo();
    }

    /**
     * 修改VehicleApplication
     *
     * @param vehicleApplication VehicleApplication实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody VehicleApplication vehicleApplication) {
        vehicleApplication.check();
        vehicleApplicationService.update(vehicleApplication);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除VehicleApplication
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        vehicleApplicationService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 派车拒绝
     * @param vehicleApplication
     * @return
     */
    @PostMapping("/handle/reject")
    public BaseVo reject(@RequestBody VehicleApplication vehicleApplication) {
        vehicleApplication.checkRejectParam();
        vehicleApplicationService.reject(vehicleApplication);
        return new BaseVo();
    }

    /**
     * 派车单失效
     * @param id
     * @return
     */
    @PostMapping("/handle/invalid")
    public BaseVo invalid(@RequestBody Long id) {
        logger.info("车辆申请到期失效");
        vehicleApplicationService.invalid(id);
        return new BaseVo();
    }

    /**
     * 租赁派车
     * @param vehicleRentInfo
     * @return
     */
    @PostMapping("/handle/rent")
    public BaseVo rent(@RequestBody VehicleRentInfo vehicleRentInfo) {
        vehicleRentInfo.checkRent();
        vehicleApplicationService.rent(vehicleRentInfo);
        return new BaseVo();
    }

    /**
     * 允许派车
     * @param vehicleAllowInfo
     * @return
     */
    @PostMapping("/handle/allow")
    public BaseVo allow(@RequestBody VehicleAllowInfo vehicleAllowInfo) {
        vehicleAllowInfo.checkAllow();
        vehicleApplicationService.allow(vehicleAllowInfo);
        return new BaseVo();
    }

    /**
     * 派车结束
     * @param vehicleApplication
     * @return
     */
    @PostMapping("/handle/end")
    public BaseVo end(@RequestBody VehicleApplication vehicleApplication) {
        vehicleApplicationService.end(vehicleApplication);
        return new BaseVo();
    }
}
