/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.handler.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.excel.handler.AbstractSheetWriteHandler;
import com.legendshop.common.excel.properties.ExcelProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title EmptySheetWriteHandler
 * @date 2022/4/9 15:14
 * @description： 空实现，只返回内容标题
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class EmptySheetWriteHandler extends AbstractSheetWriteHandler {

	private final ExcelProperties excelProperties;

	@Override
	@SneakyThrows
	public void write(Object obj, OutputStream outputStream, ExportExcel exportExcel, Method declaredMethod) {
		//通过反射获取到方法
		//获取返回值的类型
		Type genericReturnType = declaredMethod.getGenericReturnType();
		//获取返回值的泛型参数
		if (genericReturnType instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();

			Type actualTypeArgument = actualTypeArguments[0];
			Class clazz = null;

			// 嵌套list处理，例 List<List<String>>     ((ParameterizedTypeImpl) actualTypeArgument).getRawType()
			if (actualTypeArgument instanceof ParameterizedType) {
				clazz = (Class) ((ParameterizedType) actualTypeArgument).getActualTypeArguments()[0];
			} else {
				// 单list，获取泛型
				clazz = ((Class) actualTypeArgument);
			}
			ExcelWriter excelWriter = getExcelWriter(outputStream, exportExcel, clazz, excelProperties.getTemplatePath());
			// 有模板则不指定sheet名
			WriteSheet sheet = StringUtils.hasText(exportExcel.template()) ?
					EasyExcel.writerSheet().build() : EasyExcel.writerSheet(exportExcel.sheet()[0]).build();

			excelWriter.write(Collections.emptyList(), sheet);
			excelWriter.finish();
		}
	}

	@Override
	public boolean support(Object obj) {
		return obj instanceof List && ObjectUtil.isEmpty(obj);
	}
}
