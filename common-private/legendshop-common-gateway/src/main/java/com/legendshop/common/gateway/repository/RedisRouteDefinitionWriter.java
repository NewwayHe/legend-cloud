/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.gateway.repository;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.gateway.dto.RouteDefinitionDTO;
import com.legendshop.common.gateway.util.RouteCacheUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基于redis实现网关动态路由表
 *
 * @author legendshop
 */
@AllArgsConstructor
@Component
@Slf4j
public class RedisRouteDefinitionWriter implements RouteDefinitionRepository {

	private final RedisTemplate redisTemplate;

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			RouteDefinitionDTO routeDefinitionDTO = new RouteDefinitionDTO();
			BeanUtils.copyProperties(r, routeDefinitionDTO);
			log.info("保存路由信息{}", routeDefinitionDTO);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, r.getId(), routeDefinitionDTO);
			redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "新增路由信息,更新路由缓存");
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			log.info("删除路由信息{}", id);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().delete(CacheConstants.ROUTE_KEY, id);
		});
		redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "删除路由信息,更新路由缓存");
		return Mono.empty();
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinitionDTO> routeList = RouteCacheUtil.getRouteList();
		if (CollUtil.isNotEmpty(routeList)) {
			return Flux.fromIterable(routeList);
		}

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionDTO.class));
		List<RouteDefinitionDTO> values = redisTemplate.opsForHash().values(CacheConstants.ROUTE_KEY);
		log.debug("redis 中路由定义条数： {}， {}", values.size(), values);

		RouteCacheUtil.setRouteList(values);
		return Flux.fromIterable(values);
	}
}
