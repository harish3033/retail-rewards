package com.retail.rewards.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LoggerAspect {

	@Around("com.retail.rewards.config.AppPointCuts.controllerPointcut()"
			+ "|| com.retail.rewards.config.AppPointCuts.servicePointcut()"
			+ "|| com.retail.rewards.config.AppPointCuts.repositoryPointcut()"
			+ "|| com.retail.rewards.config.AppPointCuts.utilPointcut()")
	public Object logTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Signature signature = proceedingJoinPoint.getSignature();
		final String className = signature.getDeclaringTypeName();
		final String methodName = signature.getName();
		final Object[] args = proceedingJoinPoint.getArgs();
		log.info("Calling method - {}.{}({})", className, methodName, args);

		try {
			long startTime = System.currentTimeMillis();
			Object result = proceedingJoinPoint.proceed();
			long endTime = System.currentTimeMillis();
			log.info("Completed method - {}.{}({}), time_taken={} ms", className, methodName, args, (endTime - startTime));
			return result;
		} catch (Exception e) {
			log.error("Exception in method - {}.{}({})", className, methodName, args, e);
			throw e;
		}
	}
}