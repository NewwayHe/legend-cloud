/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.ValidateCodeException;
import com.legendshop.common.core.util.SpringContextHolder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 验证码校验的filter
 * 校验滑块验证码，短信验证码
 *
 * @author legendshop
 */
@Slf4j
@Component
@AllArgsConstructor
public class CodeValidateGatewayFilter extends AbstractGatewayFilterFactory {

	private final ObjectMapper objectMapper;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL,
					SecurityConstants.SMS_TOKEN_URL, SecurityConstants.SOCIAL_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			// 刷新token，直接向下执行
			String grantType = request.getHeaders().getFirst("grantType");
			if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
				return chain.filter(exchange);
			}

			// 终端设置不校验， 直接向下执行
			try {
				// 校验验证码
				checkCode(request);
			} catch (Exception e) {
				log.error("", e);
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
				response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
				try {
					return response.writeWith(Mono.just(
							response.bufferFactory().wrap(objectMapper.writeValueAsBytes(R.fail(e.getMessage())))));
				} catch (JsonProcessingException e1) {
					log.error("对象输出异常", e1);
				}
			}

			return chain.filter(exchange);
		};
	}

	/**
	 * 检查code
	 *
	 * @param request
	 */
	@SneakyThrows
	private void checkCode(ServerHttpRequest request) {

		String captchaType = request.getHeaders().getFirst("captchaType");
		String source = request.getHeaders().getFirst("source");

		CaptchaTypeEnum captchaTypeEnum = CaptchaTypeEnum.parseFromCodeValue(captchaType);

		// 如果是商家端或者平台端或者有验证参数，则校验
		if (VisitSourceEnum.SHOP.name().equals(source)
				|| VisitSourceEnum.PLATFORM.name().equals(source)
				|| captchaTypeEnum != null) {
			String code = request.getHeaders().getFirst("code");

			if (StrUtil.isBlank(code)) {
				throw new ValidateCodeException("验证码不正确");
			}

			CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
			CaptchaVO vo = new CaptchaVO();
			vo.setCaptchaVerification(code);
			vo.setCaptchaType(captchaType);
			if (!captchaService.verification(vo).isSuccess()) {
				throw new ValidateCodeException("验证码不正确");
			}
		}
		//如果是短信验证码
		//判断短信验证码是否合格
		//redis判断
	}

}
