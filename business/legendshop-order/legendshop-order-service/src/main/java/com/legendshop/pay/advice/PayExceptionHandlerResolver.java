/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.dto.ExceptionLogDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.SystemServiceTypeEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.producer.ExceptionProducerService;
import com.legendshop.pay.expetion.PayBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Date;

/**
 * @author legendshop
 * 支付服务业务异常处理器
 * 设置优先级，优于全局处理器进行管理
 */
@Slf4j
@Order(998)
@RestControllerAdvice
@RequiredArgsConstructor
public class PayExceptionHandlerResolver {

	final ObjectMapper objectMapper = new ObjectMapper();

	final ExceptionProducerService exceptionProducerService;

	/**
	 * 全局异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleGlobalException(BusinessException e) throws IOException {

		log.error(e.getMessage(), e);

		if (e instanceof PayBusinessException) {
			PayBusinessException payBusinessException = (PayBusinessException) e;
			ExceptionLogDTO dto = new ExceptionLogDTO();
			BeanUtils.copyProperties(payBusinessException, dto);
			dto.setType(SystemServiceTypeEnum.PAY.name());
			dto.setCreateTime(new Date());
			this.exceptionProducerService.payException(this.objectMapper.writeValueAsString(dto));
		}

		return R.fail(e.getBusinessResponseInterface().getCode(), e.getMessage());
	}
}
