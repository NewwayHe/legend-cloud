/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.component;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BaseException;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.feign.expetion.FeignAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.List;

/**
 * @author legendshop
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerResolver {

	@Autowired
	MultipartProperties multipartProperties;

	/**
	 * 全局异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		return R.fail(e.getLocalizedMessage());
	}

	/**
	 * 空指针处理.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<Void> handleGlobalNullPointerException(NullPointerException e) {
		log.error("空指针异常!", e);
		return R.fail("业务异常，请稍后重试！");
	}

	/**
	 * 权限不足拦截
	 */
	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R<Void> handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor().getMessage("AbstractAccessDecisionManager.accessDenied", e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg);
		return R.fail(e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 *
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			if (!errors.isEmpty()) {
				// 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
				ObjectError objectError = errors.get(0);
				log.warn("参数绑定异常,ex = {}", objectError.getDefaultMessage());
				return R.fail(objectError.getDefaultMessage());
			}
		}
		return R.fail("参数绑定异常", exception.getMessage());

	}

	@ExceptionHandler({MultipartException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R multipartExceptionHandler(Exception exception) {
		long megabytes = multipartProperties.getMaxFileSize().toMegabytes();
		log.warn("上传文件异常 , ex = {}", exception.getMessage());
		return R.fail(String.format("上件上传失败！文件最大支付 %s M", megabytes));
	}


	/**
	 * validation Exception (以form-data形式传参)
	 */
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.fail(fieldErrors.get(0).getDefaultMessage());
	}

	/**
	 * 业务异常
	 *
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBusinessException(BaseException e) {
		log.error(e.getMessage(), e);
		return R.fail(e.getBusinessResponseInterface().getCode(), e.getMessage());
	}

	/**
	 * feignClient认证异常
	 *
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = FeignAuthException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public R handleFeignAuthException(FeignAuthException e) {
		log.error(e.getMessage(), e);
		return R.fail(e.getMessage());
	}

	/**
	 * 无效刷新token异常
	 *
	 * @param e 异常
	 * @return 异常结果
	 */
//	@ExceptionHandler(value = InvalidRefreshTokenException.class)
//	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
//	public R handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {
//		log.error(e.getMessage(), e);
//		return R.fail(e.getMessage());
//	}
}
