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
 * 多表格导出处理器
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ManySheetWriteHandler extends AbstractSheetWriteHandler {
	private final ExcelProperties excelProperties;

	/**
	 * 当且仅当List不为空且List中的元素也是List 才返回true
	 *
	 * @param obj 返回对象
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !objList.isEmpty() && objList.get(0) instanceof List;
		} else {
//			throw new ExcelException("@ExportExcel 返回值必须为List类型");
			return false;
		}
	}

	@Override
	public void write(Object obj, OutputStream outputStream, ExportExcel exportExcel, Method declaredMethod) {
		List objList = (List) obj;
		List eleList = (List) objList.get(0);

		ExcelWriter excelWriter = getExcelWriter(outputStream, exportExcel, eleList.get(0).getClass(), excelProperties.getTemplatePath());

		String[] sheets = exportExcel.sheet();
		for (int i = 0; i < sheets.length; i++) {
			//创建sheet
			WriteSheet sheet;
			if (StringUtils.hasText(exportExcel.template())) {
				sheet = EasyExcel.writerSheet(i).build();
			} else {
				sheet = EasyExcel.writerSheet(i, sheets[i]).build();
			}

			// 写入sheet
			excelWriter.write((List) objList.get(i), sheet);
		}
		excelWriter.finish();
	}
}
