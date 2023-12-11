/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.processor;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * 名称表达式解析器
 *
 * @author legendshop
 */
public class ExcelNameSpelExpressionProcessor implements ExcelNameProcessor {

	/**
	 * 参数发现器
	 */
	private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * Express语法解析器
	 */
	private static final ExpressionParser PARSER = new SpelExpressionParser();

	@Override
	public String doDetermineName(Object[] args, Method method, String key) {
		if (!key.contains("#")) {
			return key;
		}
		EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, NAME_DISCOVERER);
		final Object value = PARSER.parseExpression(key).getValue(context);
		return value == null ? null : value.toString();
	}
}
