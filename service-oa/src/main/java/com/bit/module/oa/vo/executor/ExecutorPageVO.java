package com.bit.module.oa.vo.executor;

import com.bit.module.oa.bean.Executor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/3/19 18:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutorPageVO extends Executor {
    private String username;
}
