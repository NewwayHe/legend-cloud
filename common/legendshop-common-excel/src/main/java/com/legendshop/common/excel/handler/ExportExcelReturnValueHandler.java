/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.handler;

import com.legendshop.common.excel.annotation.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

/**
 * 处理@ExportExcel注解的返回值
 *
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
public class ExportExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

	/**
	 * 表格集合
	 */
	private final List<SheetWriteHandler> sheetWriteHandlerList;

	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return null != parameter.getMethodAnnotation(ExportExcel.class);
	}

	/**
	 * 处理返回值的逻辑
	 *
	 * @param obj
	 * @param parameter
	 * @param mavContainer
	 * @param nativeWebRequest
	 * @throws Exception
	 */
	@Override
	public void handleReturnValue(Object obj, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest) throws Exception {
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		ExportExcel responseExcel = parameter.getMethodAnnotation(ExportExcel.class);
		Assert.state(responseExcel != null, "No @ResponseExcel");
		mavContainer.setRequestHandled(Boolean.TRUE);
		sheetWriteHandlerList.forEach(handler -> handler.export(obj, response, responseExcel, parameter.getMethod()));
	}
}
