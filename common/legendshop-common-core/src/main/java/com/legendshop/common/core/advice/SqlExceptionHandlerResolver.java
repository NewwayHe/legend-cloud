/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.advice;

import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author legendshop
 * 支付服务业务异常处理器
 * 设置优先级，优于全局处理器进行管理
 */
@Slf4j
@Order(997)
@RestControllerAdvice
@RequiredArgsConstructor
public class SqlExceptionHandlerResolver {


	/**
	 * sql异常捕获，不让前端看到sql
	 */
	@ExceptionHandler(value = DataAccessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R badSqlGrammarException(DataAccessException e) {
		log.error(e.getMessage(), e);
		return R.fail("业务异常");
	}
}
