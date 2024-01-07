package com.masikga.itwc.common.log;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.aspectj.lang.annotation.Aspect
@RequiredArgsConstructor
public class LoggingAspect {

	private static final String EXECUTION = "execution(* ";
	private static final String ROOT_PACKAGE = "com.example.demo.";

	private final LogComponent logComponent;

	// 호출 메소드의 파라미터를 'key', 'value'를 배열로 반환한다.
	private List<String> getParams(ProceedingJoinPoint joinPoint) {

		AtomicInteger index = new AtomicInteger(0);
		String[] paramNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();

		return Arrays.stream(joinPoint.getArgs())
			.map(arg -> paramNames[index.getAndIncrement()] + "=" + abbreviate(String.valueOf(arg), 20))
			.toList();
	}

	@Around(EXECUTION + ROOT_PACKAGE + "domain.*.controller.*.*(..))")
	public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {

		String methodName = joinPoint.getSignature().getName();

		if (logComponent.excludeMediaFileRequest(methodName)) {
			return joinPoint.proceed();
		}

		List<String> params = getParams(joinPoint);

		log.debug("\" controller Before {}, params : {} \"", methodName, params);
		Object result = joinPoint.proceed();
		log.debug("\" controller After {}, result : {} \"", methodName,
			logComponent.reduceLongString(String.valueOf(result)));

		return result;
	}

	@Around(EXECUTION + ROOT_PACKAGE + "domain.*.service.*.*(..))")
	public Object serviceAround(ProceedingJoinPoint joinPoint) throws Throwable {

		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String classAndMethod = className + "." + methodName + "()";

		if (logComponent.excludeMediaFileRequest(methodName)) {
			return joinPoint.proceed();
		}

		List<String> params = getParams(joinPoint);

		Object result = null;
		try {

			log.info("\" Service Before Method : {}, Param : {} \"", classAndMethod, params);
			result = joinPoint.proceed();
			log.info("\" Service After Method : {}, Result : {} \"", classAndMethod,
				logComponent.reduceLongString(String.valueOf(result)));

		} catch (Exception e) {
			log.info("\" Service After Method : {}, Result : {} \"", classAndMethod, result);
			throw e;
		}

		return result;
	}
}
