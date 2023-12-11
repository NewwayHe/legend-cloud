/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.aop;

import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.excel.handler.SheetWriteHandler;
import com.legendshop.common.excel.processor.ExcelNameProcessor;
import com.legendshop.common.excel.result.ExportResult;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Objects;

/**
 * @author legendshop
 */
@Aspect
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ExcelNameAop {

	public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";
	private final ExcelNameProcessor processor;
	private final List<SheetWriteHandler> sheetWriteHandlerList;


	/**
	 * oss上传例子，需要以下代码获取文件名：
	 * ExportExcelTaskDTO exportExcelTaskDTO = new ExportExcelTaskDTO();
	 * exportExcelTaskDTO.setBusinessName("后台订单数据导出");
	 * exportExcelTaskDTO.setBusinessType("订单");
	 * String fileName = fileClient.createExportExcelTask(exportExcelTaskDTO);
	 *
	 * @param pj
	 * @param excel
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(excel)")
	private Object around(ProceedingJoinPoint pj, ExportExcel excel) throws Throwable {
		MethodSignature ms = (MethodSignature) pj.getSignature();
		String name = processor.doDetermineName(pj.getArgs(), ms.getMethod(), excel.name());
		//将名字设置到request里面去
		//后面会获取来用
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);

		if (excel.useOss()) {
			//1、调用Basic 申请导出文件
			Object object = pj.proceed();
			if (!(object instanceof ExportResult)) {
				throw new BusinessException("导出结果类型不正确，请按开发规范进行处理！");
			}

			ExportResult exportResult = (ExportResult) object;

			//3、把查询的数据生成Excel，上传到阿里云，并通知Basic上传成功。
			sheetWriteHandlerList.forEach(handler -> handler.exportOss(exportResult, excel, ms.getMethod()));
			return ExportResult.ok();
		}
		return pj.proceed();
	}
}
