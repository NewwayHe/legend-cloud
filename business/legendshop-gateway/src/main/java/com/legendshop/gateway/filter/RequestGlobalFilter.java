/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.gateway.filter;


import com.alibaba.fastjson.JSON;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestLogConstant;
import com.legendshop.common.core.util.IdGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局拦截器，作用所有的微服务
 *
 * @author legendshop
 */
@Slf4j
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

	public static final String DOWNLOAD_PATH = "/basic/file/download";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


		ServerHttpRequest request = exchange.getRequest();
		String requestPath = request.getURI().getPath();
		if (DOWNLOAD_PATH.equals(requestPath)) {
			return this.errorUrl(exchange);
		}
		String method = request.getMethodValue();
		ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
		//TODO 转发的请求都加上服务间认证token
		String traceId = IdGenerateUtil.nextIdStr(IdGenerateUtil.IdTypeEnum.REQUEST_TRACE_ID);
		log.info("Access path = {}, method = {}, traceId = {}", requestPath, method, traceId);

		builder.header(RequestLogConstant.TRACE_ID, traceId);


		//将用户信息传给服务
		return chain.filter(exchange.mutate().request(builder.build()).build());
	}

	private Mono<Void> errorUrl(ServerWebExchange exchange) {
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(JSON.toJSONString(R.fail("请求路径错误")).getBytes(StandardCharsets.UTF_8));
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.NOT_FOUND);
		//指定编码，否则在浏览器中会中文乱码
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		return response.writeWith(Mono.just(buffer));
	}

	@Override
	public int getOrder() {
		return -2;
	}
}
