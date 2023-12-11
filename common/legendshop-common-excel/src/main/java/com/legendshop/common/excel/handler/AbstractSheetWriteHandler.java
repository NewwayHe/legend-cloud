/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.handler;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.excel.aop.ExcelNameAop;
import com.legendshop.common.excel.converters.LocalDateStringConverter;
import com.legendshop.common.excel.converters.LocalDateTimeStringConverter;
import com.legendshop.common.excel.result.ExportResult;
import com.legendshop.common.excel.util.EasyExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;

/**
 * excel写表格的抽象类
 *
 * @author legendshop
 */
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler {

	@Autowired
	private EasyExcelUtils easyExcelUtils;

	@Override
	@SneakyThrows
	public void export(Object o, HttpServletResponse response, ExportExcel exportExcel, Method declaredMethod) {
		if (support(o)) {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			//从request获取之前切面的名字
			String name = (String) Objects.requireNonNull(requestAttributes)
					.getAttribute(ExcelNameAop.EXCEL_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
			String fileName = String.format("%s%s", URLEncoder.encode(name, "UTF-8"), exportExcel.suffix().getValue());
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
			write(o, response.getOutputStream(), exportExcel, declaredMethod);
		}
	}

	@Override
	public Boolean exportOss(ExportResult exportResult, ExportExcel exportExcel, Method declaredMethod) {
		if (support(exportResult.getExportList())) {
			// 文件上传到OSS
			Boolean result = false;
			try {
				@Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
				write(exportResult.getExportList(), os, exportExcel, declaredMethod);

				byte[] content = os.toByteArray();
				@Cleanup InputStream is = new ByteArrayInputStream(content);
				result = easyExcelUtils.uploadFile(is, exportResult.getFileName());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		return false;
	}

	/**
	 * 通用的获取ExcelWriter方法
	 *
	 * @param outputStream outputStream
	 * @param exportExcel  ResponseExcel注解
	 * @param objClazz     Excel数据class
	 * @param templatePath 模板地址
	 * @return ExcelWriter
	 */
	@SneakyThrows
	public ExcelWriter getExcelWriter(OutputStream outputStream, ExportExcel exportExcel, Class objClazz, String templatePath) {
		ExcelWriterBuilder writerBuilder = EasyExcel.write(outputStream, objClazz)
				.registerConverter(LocalDateStringConverter.INSTANCE)
				.registerConverter(LocalDateTimeStringConverter.INSTANCE)
				.autoCloseStream(Boolean.TRUE)
				.excelType(exportExcel.suffix())
				.inMemory(exportExcel.inMemory());

		//如果有设置密码
		if (StringUtils.hasText(exportExcel.password())) {
			writerBuilder.password(exportExcel.password());
		}

		//如果有包含字段
		if (ArrayUtil.isNotEmpty(exportExcel.include())) {
			writerBuilder.includeColumnFiledNames(Arrays.asList(exportExcel.include()));
		}

		//如果有忽略字段
		if (ArrayUtil.isNotEmpty(exportExcel.exclude())) {
			writerBuilder.excludeColumnFiledNames(Arrays.asList(exportExcel.exclude()));
		}

		if (ArrayUtil.isNotEmpty(exportExcel.writeHandler())) {
			for (Class<? extends WriteHandler> clazz : exportExcel.writeHandler()) {
				writerBuilder.registerWriteHandler(clazz.newInstance());
			}
		}

		//如果有转换器
		if (ArrayUtil.isNotEmpty(exportExcel.converter())) {
			for (Class<? extends Converter> clazz : exportExcel.converter()) {
				writerBuilder.registerConverter(clazz.newInstance());
			}
		}

		//如果有模板就用自定义模板
		if (StringUtils.hasText(exportExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(templatePath
					+ File.separator + exportExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		return writerBuilder.build();
	}

	/**
	 * 通用的获取ExcelWriter方法
	 *
	 * @param os           ByteArrayOutputStream
	 * @param exportExcel  ResponseExcel注解
	 * @param objClazz     Excel数据class
	 * @param templatePath 模板地址
	 * @return ExcelWriter
	 */
	@SneakyThrows
	public ExcelWriter getExcelWriter(ByteArrayOutputStream os, ExportExcel exportExcel, Class objClazz, String templatePath) {
		ExcelWriterBuilder writerBuilder = EasyExcel.write(os, objClazz)
				.registerConverter(LocalDateStringConverter.INSTANCE)
				.registerConverter(LocalDateTimeStringConverter.INSTANCE)
				.autoCloseStream(Boolean.TRUE)
				.excelType(exportExcel.suffix())
				.inMemory(exportExcel.inMemory());

		//如果有设置密码
		if (StringUtils.hasText(exportExcel.password())) {
			writerBuilder.password(exportExcel.password());
		}

		//如果有包含字段
		if (ArrayUtil.isNotEmpty(exportExcel.include())) {
			writerBuilder.includeColumnFiledNames(Arrays.asList(exportExcel.include()));
		}

		//如果有忽略字段
		if (ArrayUtil.isNotEmpty(exportExcel.exclude())) {
			writerBuilder.excludeColumnFiledNames(Arrays.asList(exportExcel.exclude()));
		}

		if (ArrayUtil.isNotEmpty(exportExcel.writeHandler())) {
			for (Class<? extends WriteHandler> clazz : exportExcel.writeHandler()) {
				writerBuilder.registerWriteHandler(clazz.newInstance());
			}
		}

		//如果有转换器
		if (ArrayUtil.isNotEmpty(exportExcel.converter())) {
			for (Class<? extends Converter> clazz : exportExcel.converter()) {
				writerBuilder.registerConverter(clazz.newInstance());
			}
		}

		//如果有模板就用自定义模板
		if (StringUtils.hasText(exportExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(templatePath
					+ File.separator + exportExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		return writerBuilder.build();
	}
}
