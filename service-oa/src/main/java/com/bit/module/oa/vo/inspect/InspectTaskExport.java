package com.bit.module.oa.vo.inspect;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.module.oa.bean.Dept;
import com.bit.module.oa.enums.InspectStatusEnum;
import com.bit.module.oa.enums.InspectTypeEnum;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/21 21:30
 */
@Data
@ExcelTarget("inspectTaskExport")
public class InspectTaskExport {
    /**
     * id
     */
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 巡检单号
     */
    @Excel(name = "任务单号", width = 15, orderNum = "2")
    private String no;
    /**
     * 巡检名称
     */
    @Excel(name = "任务名称", width = 15, orderNum = "3")
    private String name;
    /**
     * 类型 1智能巡检 2轨迹巡检
     */
    @Excel(name = "巡检类型", width = 15, orderNum = "4")
    private Integer type;
    /**
     * 部门json集合，格式[{"id":xx,"name":xxx},{...}]
     */
    @Excel(name = "巡检部门", width = 15, orderNum = "6")
    private List<Dept> deps;
    /**
     * 状态 0未发布 1未执行 2执行中 3已完成 4已终止 5已结束
     */
    @Excel(name = "任务状态", width = 15, orderNum = "7")
    private Integer status;
    /**
     * 巡检开始时间
     */
    @Excel(name = "巡检开始时间", width = 15, orderNum = "8")
    private Date startTime;
    /**
     * 巡检结束时间
     */
    @Excel(name = "巡检结束时间", width = 15, orderNum = "9")
    private Date endTime;

    private List<Dept> parseDeps() {
        if (CollectionUtils.isEmpty(this.deps)) {
            return Collections.emptyList();
        }
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(this.deps));
        this.setDeps(array.toJavaList(Dept.class));
        return this.deps;
    }

    /**
     * 指定输出excel格式
     * @return
     */
    public String getDeps() {
        List<Dept> depts = parseDeps();
        if (CollectionUtils.isEmpty(depts)) {
            return "";
        }
        StringBuilder depStr = new StringBuilder();
        for (Dept dept : depts) {
            depStr.append(dept.getName()).append(", ");
        }
        return depStr.substring(0, depStr.length() - 2);
    }

    public String getStatus() {
        return InspectStatusEnum.getByKey(status);
    }

    public String getType() {
        return InspectTypeEnum.getByKey(type).getDescription();
    }

    public String getStartTime() {
        if (this.startTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.startTime);
    }

    public String getEndTime() {
        if (this.endTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.endTime);
    }
}
