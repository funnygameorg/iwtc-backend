package com.example.demo.global.config;

import com.example.demo.global.log.LogMethodSignature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@Component
public class AopConfig {

    @Around("@annotation(LogMethodSignature)")
    Object logMethodSignature(
            ProceedingJoinPoint joinPoint,
            LogMethodSignature logMethodSignature) throws Throwable {
        return joinPoint.proceed();
    }
}
