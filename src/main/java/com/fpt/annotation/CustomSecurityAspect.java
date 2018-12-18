package com.fpt.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fpt.model.Message;
import com.fpt.service.JwtService;

@Aspect
@Component
public class CustomSecurityAspect {
	@Autowired
	private JwtService jwtService;

	@Pointcut("@annotation(com.fpt.annotation.CustomSecurityAnnotation)")
	private void customSecurityAnnotation() {

	}

	@Around("com.fpt.annotation.CustomSecurityAspect.customSecurityAnnotation()")
	public Object doSomething(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest req = getRequest();
		String token = req.getHeader("Authorization"); //get token from header of request

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = pjp.getTarget().getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());
		CustomSecurityAnnotation customSecurityAnnotation = method.getAnnotation(CustomSecurityAnnotation.class);

		String authen = customSecurityAnnotation.value(); // get role access of controller
		if (authen == null) // access with all role
			return pjp.proceed();
		
		if (token == null)
			return ResponseEntity.ok(new Message("Unauthorized", "Please add token to header!"));
		
		if (jwtService.validateTokenLogin(token)) {
			String role = jwtService.getRoleFromToken(token);
			String[] arrRole = authen.split(",");
			for(String r : arrRole) {
				if(role.equalsIgnoreCase(r))
					return pjp.proceed();
			}
			return ResponseEntity.ok(new Message("Access denied", "You need permission to access!"));
		}
		return ResponseEntity.ok(new Message("Unauthorized", "Invalid format token!"));
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}

}