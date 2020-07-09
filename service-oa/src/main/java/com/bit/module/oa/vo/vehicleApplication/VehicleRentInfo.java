package com.bit.module.oa.vo.vehicleApplication;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/1/15 17:44
 */
@Data
public class VehicleRentInfo implements Serializable {
    private Long id;
    /**
     * 企业id
     */
    private Long companyId;
    /**
     * 车牌号列表，英文逗号分隔
     */
    private List<String> plateNos;
    /**
     * 驾驶员列表
     */
    private List<String> driverName;
    /**
     * 驾驶员电话
     */
    private List<String> driverPhone;

    public void checkRent() {
        checkDriver();
        if (this.getId() == null || this.getCompanyId() == null
                || StringUtils.isEmpty(this.getDriverName()) || StringUtils.isEmpty(this.getDriverPhone())) {
            throw new CheckException("租赁信息不能为空");
        }
    }

    private VehicleRentInfo checkDriver() {
        if (driverName.size() != driverPhone.size()) {
            throw new CheckException("司机姓名和手机号不对应");
        }
        return this;
    }
}
