/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.service.RouteConfigService;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.dto.RouteDefinitionBizDTO;
import com.legendshop.common.core.event.DynamicRouteInitEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;

/**
 * 启动basic服务保存数据库的路由信息到redis
 *
 * @author legendshop
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicRouteInitRunner {

	private final RedisTemplate redisTemplate;

	private final RouteConfigService routeConfigService;

	@Async
	@Order
	@EventListener({WebServerInitializedEvent.class, DynamicRouteInitEvent.class})
	public void initRoute() {
		Boolean result = redisTemplate.delete(CacheConstants.ROUTE_KEY);
		log.info("初始化网关路由：{} ", result);

		routeConfigService.queryAll().forEach(route -> {
//			RouteDefinitionDTO routeDefinitionDTO = new RouteDefinitionDTO();
			RouteDefinitionBizDTO routeDefinitionDTO = new RouteDefinitionBizDTO();
			routeDefinitionDTO.setRouteName(route.getRouteName());
			routeDefinitionDTO.setId(route.getRouteId());
			routeDefinitionDTO.setUri(URI.create(route.getUri()));
			routeDefinitionDTO.setOrder(route.getSeq());

			JSONArray filterObj = JSONUtil.parseArray(route.getFilters());
			JSONArray predicateObj = JSONUtil.parseArray(route.getPredicates());
			routeDefinitionDTO.setPredicates(predicateObj);
			routeDefinitionDTO.setFilters(filterObj);

			log.info("动态加载路由：{},{}", route.getRouteId(), routeDefinitionDTO);
			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionBizDTO.class));
			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, route.getRouteId(), routeDefinitionDTO);
		});
		log.debug("动态加载路由结束 ");
	}

	/**
	 * redis 监听配置,监听 gateway_redis_route_reload_topic,重新加载Redis
	 *
	 * @param redisConnectionFactory redis 配置
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener((message, bytes) -> {
			log.warn("接收到重新Redis 重新加载路由事件");
			initRoute();
		}, new ChannelTopic(CacheConstants.ROUTE_REDIS_RELOAD_TOPIC));
		return container;
	}

}
