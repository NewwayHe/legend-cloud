/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.processor;

import java.lang.reflect.Method;

/**
 * excel导出名称处理
 *
 * @author legendshop
 */
public interface ExcelNameProcessor {

	String doDetermineName(Object[] args, Method method, String key);
}
