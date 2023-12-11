/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.annotation;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;

import java.lang.annotation.*;

/**
 * 导出excel注解
 *
 * @author legendshop
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportExcel {

	/**
	 * 导出文件的名称
	 *
	 * @return
	 */

	String name();


	/**
	 * 文件类型 （xlsx xls）
	 *
	 * @return string
	 */
	ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

	/**
	 * 文件密码
	 *
	 * @return password
	 */
	String password() default "";

	/**
	 * sheet 名称，支持多个
	 *
	 * @return String[]
	 */
	String[] sheet() default {"Sheet1"};


	/**
	 * 内存操作
	 *
	 * @return
	 */
	boolean inMemory() default false;

	/**
	 * excel  模板
	 *
	 * @return String
	 */
	String template() default "";

	/**
	 * +
	 * 包含字段
	 *
	 * @return String[]
	 */
	String[] include() default {};

	/**
	 * 排除字段
	 *
	 * @return String[]
	 */
	String[] exclude() default {};

	/**
	 * 拦截器，自定义样式等处理器
	 *
	 * @return WriteHandler[]
	 */
	Class<? extends WriteHandler>[] writeHandler() default {};

	/**
	 * 转换器
	 *
	 * @return Converter[]
	 */
	Class<? extends Converter>[] converter() default {};

	/**
	 * 使用oss上传文件，但会阻塞线程
	 *
	 * @return
	 */
	boolean useOss() default false;
}
