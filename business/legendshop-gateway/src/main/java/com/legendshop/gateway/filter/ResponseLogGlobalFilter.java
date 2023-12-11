/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局响应拦截器，作用所有的微服务
 *
 * @author legendshop
 */
@Slf4j
@Component
public class ResponseLogGlobalFilter implements GlobalFilter, Ordered {
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			ServerHttpResponse originalResponse = exchange.getResponse();
			DataBufferFactory bufferFactory = originalResponse.bufferFactory();
			ServerHttpRequest request = exchange.getRequest();
			String requestPath = request.getURI().getPath();
			HttpStatusCode statusCode = originalResponse.getStatusCode();

			if (statusCode == HttpStatus.OK) {
				ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

					@Override
					public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
						if (body instanceof Flux) {
							Flux<? extends DataBuffer> fluxBody = Flux.from(body);
							return super.writeWith(fluxBody.map(dataBuffer -> {
								byte[] content = new byte[dataBuffer.readableByteCount()];
								dataBuffer.read(content);
								DataBufferUtils.release(dataBuffer);//释放掉内存
								// 构建错误日志
								HttpStatusCode originalStatusCode = originalResponse.getStatusCode();
								if (HttpStatus.OK != originalStatusCode) {
									String data = new String(content, StandardCharsets.UTF_8);//data
									log.error("<--- requestPath = {},  response code = {}, data =  {}", requestPath, originalStatusCode, data);
								}


								return bufferFactory.wrap(content);
							}));
						} else {
							log.error("<--- {} response code 异常", getStatusCode());
						}
						return super.writeWith(body);
					}
				};
				return chain.filter(exchange.mutate().response(decoratedResponse).build());
			}
			//降级处理返回数据
			return chain.filter(exchange);
		} catch (Exception e) {
			log.error("gateway log exception.\n" + e);
			return chain.filter(exchange);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
