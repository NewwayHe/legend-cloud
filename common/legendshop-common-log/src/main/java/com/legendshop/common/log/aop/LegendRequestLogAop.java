/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.aop;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.context.RequestLogContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 请求日志aop切面
 *
 * @author legendshop
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class LegendRequestLogAop extends AbstractLogAop {

	/**
	 * 定义的是切点
	 * 方法名就是切点名
	 * 使用execution表达式，定义切点为：controller包中的所有方法
	 * 第一个*：选中的连接点的方法返回值可以是任意类型，
	 * 如* 改为void意思为选中的连接点的方法返回值为void的类型
	 * 第二个*：选中的连接点的方法所在类可以是任意名字
	 * 第三个*：选中的连接点的方法名字可以是任意名字
	 * ..:表示任意参数
	 */
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController) && execution(public * *(..) )")
	public void webLog() {
	}

	/**
	 * 定义增强
	 * 在切点的什么位置增加新的功能
	 * Before：方法执行前
	 * After方法执行后
	 * Around方法执行前+方法执行后
	 * AfterThrowing：方法抛出异常后
	 * AfterReturning：方法返回后
	 */
	@SneakyThrows
	@Around("webLog()")
	public Object doBefore(ProceedingJoinPoint joinPoint) {


		if (!log.isDebugEnabled()) {
			return joinPoint.proceed();
		}
		super.processRequestTraceId();
		// 获取请求参数
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		String traceId = RequestLogContext.getTraceId();
		Object[] params = joinPoint.getArgs();
		String paramsStr = requestParamsStr(params);
		//打印日志
		log.info(" [ request-log-pre-{} ]  全类名: {}, 方法名: {}, 参数: {}", traceId, className, methodName, paramsStr);
		long beforeTime = System.currentTimeMillis();
		Object obj = joinPoint.proceed();
		long cost = System.currentTimeMillis() - beforeTime;
		log.info(" [ request-log-after-{} ]  cost: {}, body: {}", traceId, cost, JSONUtil.toJsonStr(obj));
		return obj;
	}

	@AfterThrowing(pointcut = "webLog()", throwing = "e")
	public void handle(JoinPoint point, Exception e) {
		// 异常
		super.processRequestTraceId();
		String traceId = RequestLogContext.getTraceId();
		log.info(" [ request-log-exception-{} ] ---> eMsg：{}", traceId, e.getMessage());
	}
}
