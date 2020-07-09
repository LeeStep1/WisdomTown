package com.bit.module.oa;

import com.bit.ServiceOaApplication;
import com.bit.module.oa.bean.Vehicle;
import com.bit.module.oa.service.VehicleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description :
 * @Date ： 2019/1/10 16:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = ServiceOaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class VehicleTest {

    @Autowired
    private VehicleService vehicleService;

    @Test
    public void testAdd() {
        for (int i = 0; i < 20; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setPlateNo("粤A96A" + (i < 10 ? "0" + i : i));
            vehicle.setColor("红色");
            vehicle.setBrand("丰田");
            vehicle.setPlateType("蓝牌");
            vehicle.setVin("LSGJA52U1BH003531");
            vehicle.setEngineNo("C190C8008");
            vehicle.setVehicleType("小轿车");
            vehicle.setSeatingCapacity(7);
            vehicle.setPower("汽油");
            vehicle.setPhoto("http://www.w3school.com.cn/i/eg_tulip.jpg");
            vehicle.setIdle(1);
            vehicle.setSeatingCapacity(0);
            vehicle.setStatus(0);
            vehicleService.add(vehicle);

        }
    }
}
