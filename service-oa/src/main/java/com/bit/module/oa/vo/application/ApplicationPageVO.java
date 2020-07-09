package com.bit.module.oa.vo.application;

import com.bit.module.oa.bean.Application;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/20 17:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationPageVO extends Application {
    private String inspectName;
    private String inspectNo;
    private Date inspectStartTime;
    private Date inspectEndTime;
}
