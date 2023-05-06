package com.nicico.evaluation.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Aspect
public class ExceptionLoggingAspect {

    @Pointcut("within(com.nicico.evaluation..*) && execution(* *(..))")
    public void matchAllMethods(){}

    @AfterThrowing(pointcut = "matchAllMethods()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        log.info(joinPoint.getSignature() + " --> " + String.format("Exception name: %s __ Exception message: %s", ex.getClass().getName(), ex.getMessage() ) );
    }

}
