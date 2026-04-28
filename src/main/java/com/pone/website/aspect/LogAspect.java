package com.pone.website.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.pone.website.controller..(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("[API] {}.{} - 參數: {}", className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("[API] {}.{} - 執行完成，耗時: {}ms", className, methodName, elapsed);
        } catch (Exception e) {
            log.error("[API] {}.{} - 執行異常: {}", className, methodName, e.getMessage());
            throw e;
        }
        return result;
    }
}
