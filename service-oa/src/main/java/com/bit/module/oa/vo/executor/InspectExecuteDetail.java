package com.bit.module.oa.vo.executor;

import com.bit.module.oa.bean.Application;
import com.bit.module.oa.bean.CheckIn;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.bean.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/20 15:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InspectExecuteDetail extends Executor {
    private String executorName;

    private String inspectContent;

    private Application application;

    private List<CheckIn> checkIns;

    private List<Log> logs;
}
