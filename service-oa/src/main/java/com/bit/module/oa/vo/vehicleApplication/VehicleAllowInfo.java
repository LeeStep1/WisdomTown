package com.bit.module.oa.vo.vehicleApplication;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/1/16 14:17
 */
@Data
public class VehicleAllowInfo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 车辆列表
     */
    private List<Long> vehicleIds;
    /**
     * 驾驶员列表
     */
    private List<Long> driverIds;
    /**
     * 备注
     */
    private String remark;
    /**
     * 派车人，用户id
     * 暂定, 以后从header中取
     */
    private Long assignerId;

    public void checkAllow() {
        checkId().checkVehicleAndDriver().checkTime();
    }

    private VehicleAllowInfo checkId() {
        if (id == null) {
            throw new CheckException("ID不能为空");
        }
        return this;
    }

    private VehicleAllowInfo checkVehicleAndDriver() {
        if (CollectionUtils.isEmpty(vehicleIds) || CollectionUtils.isEmpty(driverIds)) {
            throw new CheckException("车辆或者司机不能为空");
        }
        return this;
    }

    private VehicleAllowInfo checkTime() {
        if (startTime == null || endTime == null) {
            throw new CheckException("派车开始时间或结束时间不能为空");
        }
        if (endTime.before(startTime)) {
            throw new CheckException("派车结束时间不能早于开始时间");
        }
        if (startTime.before(new Date())) {
            throw new CheckException("派车开始时间不能早于当前时间");
        }
        return this;
    }
}
