package com.taotao.admin.annotation.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taotao.admin.annotation.Log;
import com.taotao.admin.common.utils.HttpContextUtils;
import com.taotao.admin.common.utils.IPUtil;
import com.taotao.admin.entity.SysLog;
import com.taotao.admin.entity.SysUser;
import com.taotao.admin.service.SysLogService;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
	
	@Autowired
	private SysLogService sysLogService;
	
	@Pointcut("@annotation(com.taotao.admin.annotation.Log)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		Log log = method.getAnnotation(Log.class);
		if(log != null) sysLog.setOperation(log.value());

		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		
		String parmas = JSON.toJSONString(joinPoint.getArgs());
		sysLog.setParams(parmas);

		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		sysLog.setIp(IPUtil.getIpAddr(request));

		String username = ((SysUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreated(new Date());

		sysLogService.save(sysLog);
	}
}
