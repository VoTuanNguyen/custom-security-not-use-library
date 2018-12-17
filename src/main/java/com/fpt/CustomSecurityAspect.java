package com.fpt;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class CustomSecurityAspect {
	@Pointcut("@annotation(com.fpt.CustomSecurityAnnotation)")
	private void customSecurityAnnotation() {

	}

	@Around("com.fpt.CustomSecurityAspect.customSecurityAnnotation()")
	public Object doSomething(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest req = getRequest();
		System.out.println(req.getHeader("Authorization"));

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = pjp.getTarget().getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());
		CustomSecurityAnnotation customSecurityAnnotation = method.getAnnotation(CustomSecurityAnnotation.class);

		System.out.println(customSecurityAnnotation.value());
		String authen = customSecurityAnnotation.value();
		if(authen == null)
			return pjp.proceed();
		return ResponseEntity.ok("Access denied!");
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}

}