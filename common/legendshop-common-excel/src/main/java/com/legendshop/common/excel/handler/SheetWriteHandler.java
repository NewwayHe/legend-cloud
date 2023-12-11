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
import com.legendshop.common.excel.result.ExportResult;
import jakarta.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * @author legendshop
 */
public interface SheetWriteHandler {

	/**
	 * 返回的对象
	 *
	 * @param obj           obj
	 * @param response      输出对象
	 * @param responseExcel 注解
	 * @param parameter
	 */
	void export(Object obj, HttpServletResponse response, ExportExcel responseExcel, Method declaredMethod);

	/**
	 * 返回的对象
	 *
	 * @param exportResult   exportResult
	 * @param responseExcel  注解
	 * @param declaredMethod
	 */
	Boolean exportOss(ExportResult exportResult, ExportExcel responseExcel, Method declaredMethod);

	/**
	 * 写成对象
	 *
	 * @param obj            obj
	 * @param outputStream   输出对象
	 * @param responseExcel  注解
	 * @param declaredMethod
	 */
	void write(Object obj, OutputStream outputStream, ExportExcel responseExcel, Method declaredMethod);


	/**
	 * 是否支持
	 *
	 * @param obj
	 * @return
	 */
	boolean support(Object obj);
}
