package com.bit.officialdoc.aop;

import com.alibaba.fastjson.JSON;
import com.bit.base.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description :
 * @Date ： 2019/5/16 10:11
 */
@Aspect
@Configuration
public class ApiAop {
    private static final Logger logger = LoggerFactory.getLogger(ApiAop.class);

    private static Pattern fullRegexPattern = Pattern
            .compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);


    @Pointcut("execution(* com.bit.module.*.controller.*.*(..))")
    public void executeService() {
    }

    @Around(value = "executeService()")
    public Object beforeMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletResponse) {
                continue;
            }
            String json = JSON.toJSONString(arg);
            Matcher matcher = fullRegexPattern.matcher(json);

            if (matcher.find()) {
                throw new BusinessException("暂不支持表情提交");
            }
        }
        return pjp.proceed();
    }
}
