package com.retail.rewards.config;

import org.aspectj.lang.annotation.Pointcut;

public class AppPointCuts {

	@Pointcut("execution(* com.retail.rewards..controller..*.*(..) )")
	public void controllerPointcut() {
	}

	@Pointcut("execution(* com.retail.rewards..service..*.*(..) )")
	public void servicePointcut() {
	}

	@Pointcut("execution(* com.retail.rewards..repository..*.*(..) )")
	public void repositoryPointcut() {
	}

	@Pointcut("execution(* com.retail.rewards..util..*.*(..) )")
	public void utilPointcut() {
	}

}