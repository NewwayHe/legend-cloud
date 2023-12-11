/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.aop;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.log.event.SystemLogEvent;
import com.legendshop.common.log.util.SystemLogUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 系统日志aop切面
 *
 * @author legendshop
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class SystemLogAop extends AbstractLogAop {

	private final ApplicationEventPublisher publisher;

	/**
	 * 环绕通知拦截@SysLog的注解进行日志的保存
	 */
	@SneakyThrows
	@Around(value = "@annotation(systemLog)")
	public Object around(ProceedingJoinPoint point, SystemLog systemLog) {
		HttpServletRequest request = getRequest();
		if (null == request) {
			return point.proceed();
		}
		//构建基础对象
		SystemLogDTO systemLogDTO = SystemLogUtil.build(request);
		systemLogDTO.setTitle(systemLog.value());
		systemLogDTO.setParams(requestParamsStr(point.getArgs()));

		//获取开始时间
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();


		if (obj instanceof R) {
			int code = ((R) obj).getCode();
			systemLogDTO.setCode(code);
		}

		Long endTime = System.currentTimeMillis();
		//执行结束时间-开始时间=执行时间
		systemLogDTO.setTime(endTime - startTime);

		log.info("构建日志对象, {}", systemLogDTO);
		//发布spring事件
		publisher.publishEvent(new SystemLogEvent(systemLogDTO));
		return obj;
	}
}
