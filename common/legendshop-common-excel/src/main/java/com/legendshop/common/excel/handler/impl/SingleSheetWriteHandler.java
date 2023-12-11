/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.handler.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.excel.handler.AbstractSheetWriteHandler;
import com.legendshop.common.excel.properties.ExcelProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 单表格导出处理器
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {
	private final ExcelProperties excelProperties;

	/**
	 * 对象必须是集合
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		} else {
//			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
			return false;
		}
	}

	@Override
	public void write(Object obj, OutputStream outputStream, ExportExcel exportExcel, Method declaredMethod) {
		List list = (List) obj;

		ExcelWriter excelWriter = getExcelWriter(outputStream, exportExcel, list.get(0).getClass(), excelProperties.getTemplatePath());
		// 有模板则不指定sheet名
		WriteSheet sheet = StringUtils.hasText(exportExcel.template()) ?
				EasyExcel.writerSheet().build() : EasyExcel.writerSheet(exportExcel.sheet()[0]).build();

		excelWriter.write(list, sheet);
		excelWriter.finish();
	}
}
