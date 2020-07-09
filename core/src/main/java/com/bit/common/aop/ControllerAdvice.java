package com.bit.common.aop;

import com.bit.base.exception.BusinessException;
import com.bit.base.exception.CheckException;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeoutException;

/**
 * @Description: 全局的controller异常处理
 * @Author: liyujun
 * @Date: 2018-11-06
 **/
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return vo
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseVo errorHandler(Exception ex) {
        BaseVo vo = new BaseVo();
        if (ex instanceof BusinessException) {
            ex = (BusinessException) ex;
            BusinessException businessException = (BusinessException) ex;
            if (businessException.getCode() > 0) {
                vo.setCode(businessException.getCode());
            } else {
                vo.setCode(ResultCode.WRONG.getCode());
            }
            vo.setMsg(ex.getMessage());

        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mex = (MethodArgumentNotValidException) ex;
            vo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            vo.setMsg(mex.getBindingResult().getFieldError().getDefaultMessage());
        } else if (ex instanceof CheckException) {
            ex = (CheckException) ex;
            vo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            vo.setMsg(ex.getMessage());
        }else if(ex instanceof HystrixRuntimeException){
            vo.setCode(ResultCode.HYSTRIX_TIME_OUT.getCode());
            vo.setMsg("网络异常请稍后重试");
            logger.info("全局异常拦截HystrixRuntimeException");
        }else if(ex instanceof TimeoutException){
            vo.setCode(ResultCode.HYSTRIX_TIME_OUT.getCode());
            vo.setMsg("网络异常,请求超时");
        }  else {
            vo.setCode(ResultCode.WRONG.getCode());
            vo.setMsg(ResultCode.WRONG.getInfo());
        }
        logger.error(ex.getMessage(), ex);
        return vo;
    }
}
