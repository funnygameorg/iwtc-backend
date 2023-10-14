package com.example.demo.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@org.aspectj.lang.annotation.Aspect
@Component
public class LoggingAspect {

    private static final String EXECUTION = "execution(* ";
    private static final String ROOT_PACKAGE = "com.example.demo.";
    private static final String METHOD_CALL_LOG_PATTERN = "method call name: [{}] : args: {}";
    private static final String METHOD_RETURN_LOG_PATTERN = "method return name: [{}], return value: {}";

    // 호출 메소드의 파라미터를 'key', 'value'를 배열로 반환한다.
    private List<String> getParams(ProceedingJoinPoint joinPoint) {

        AtomicInteger index = new AtomicInteger(0);
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        return Arrays.stream(joinPoint.getArgs())
                .map(arg -> paramNames[index.getAndIncrement()] + "=" + arg)
                .toList();
    }


    @Around(EXECUTION + ROOT_PACKAGE + "domain.*.controller.*.*(..))")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        List<String> params = getParams(joinPoint);

        log.info(METHOD_CALL_LOG_PATTERN, methodName, params);
        Object result = joinPoint.proceed();
        log.info(METHOD_RETURN_LOG_PATTERN, methodName, result);

        return result;
    }

    @Around(EXECUTION + ROOT_PACKAGE + "domain.*.service.*.*(..))")
    public Object serviceAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        List<String> params = getParams(joinPoint);

        log.info("before {}", joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("after {}", joinPoint.getSignature().getName());

        return result;
    }

    @Around(EXECUTION + ROOT_PACKAGE + "domain.*.repository.*.*(..))")
    public Object repositoryAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        List<String> params = getParams(joinPoint);
        
        log.info("before {}", joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("after {}", joinPoint.getSignature().getName());

        return result;
    }
}
