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
import com.legendshop.common.core.constant.RequestLogConstant;
import com.legendshop.common.core.context.RequestLogContext;
import com.legendshop.common.core.util.IdGenerateUtil;
import com.legendshop.common.security.utils.SecurityUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author legendshop
 */
public abstract class AbstractLogAop {

	/**
	 * 获取Request
	 */
	protected HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes == null ? null : attributes.getRequest();
	}

	/**
	 * 设置请求链路ID
	 */
	protected void processRequestTraceId() {
		HttpServletRequest request = this.getRequest();
		String traceId = request.getHeader(RequestLogConstant.TRACE_ID);
		if (StringUtils.isBlank(traceId)) {
			traceId = IdGenerateUtil.nextIdStr(IdGenerateUtil.IdTypeEnum.REQUEST_TRACE_ID);
		}
		RequestLogContext.addTraceId(traceId);
	}

	/**
	 * 判断文件中是否存在文件
	 */
	protected boolean isMultipartFileMark(Object[] params) {
		boolean multipartFileMark = false;
		for (Object param : params) {
			if (param instanceof MultipartFile) {
				multipartFileMark = true;
				break;
			}
		}
		return multipartFileMark;
	}

	/**
	 * 将请求参数组装成字符串
	 */
	protected String requestParamsStr(Object[] params) {
		String paramsStr = "MultipartFile";
		Object[] args = new Object[params.length];
		if (!this.isMultipartFileMark(params)) {
			for (int i = 0; i < params.length; i++) {
				if (!(params[i] instanceof ServletRequest) && !(params[i] instanceof ServletResponse)) {
					args[i] = params[i];
				}
			}
			paramsStr = JSONUtil.toJsonStr(args);
		}
		return paramsStr;
	}

	protected String requestUserName() {
		return SecurityUtils.getBaseUser().getUsername();
	}

}
