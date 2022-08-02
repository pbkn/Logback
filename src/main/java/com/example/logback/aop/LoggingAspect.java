package com.example.logback.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Pointcut("within(com.example..*)")
	public void pointCutAllMethods() {
		// Pointcut Method
	}

	@Around(value = "pointCutAllMethods()")
	public Object logAroundMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();

		log.debug("Execution of " + className + "." + methodName + " - Started");

		Object result = proceedingJoinPoint.proceed();

		StringBuilder methodParamsBuilder = new StringBuilder();
		for (String params : methodSignature.getParameterNames()) {
			methodParamsBuilder.append(params);
		}

		log.debug("Execution of " + className + "." + methodName + " :: " + methodParamsBuilder.toString() + ":::"
				+ result + " - Ended");

		return result;
	}

	@AfterThrowing(pointcut = "pointCutAllMethods()", throwing = "ex")
	public void logAfterThrowingException(JoinPoint joinPoint, Throwable ex) {
		String cause = ex.getCause() != null ? ex.getCause().toString() : "none";
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();
		log.warn("Throwing Exception: {} from class {}, method {}, with cause {}", ex.getMessage(), className,
				methodName, cause);
	}

}