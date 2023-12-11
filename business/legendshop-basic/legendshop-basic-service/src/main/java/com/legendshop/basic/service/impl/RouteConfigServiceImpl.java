/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.dao.RouteConfigDao;
import com.legendshop.basic.dto.RouteConfigDTO;
import com.legendshop.basic.entity.RouteConfig;
import com.legendshop.basic.service.RouteConfigService;
import com.legendshop.basic.service.convert.RouteConfigConverter;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.dto.RouteDefinitionBizDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 动态路由接口实现
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j
public class RouteConfigServiceImpl implements RouteConfigService {

	private final RedisTemplate redisTemplate;

	private final RouteConfigConverter routeConfigConverter;

	private final RouteConfigDao routeConfigDao;

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public Mono<Void> updateRoutes(JSONArray routes) {
		// 首先清空Redis 缓存
		Boolean result = redisTemplate.delete(CacheConstants.ROUTE_KEY);
		// 遍历修改的routes，保存到Redis
		List<RouteDefinitionBizDTO> routeDefinitionVoList = new ArrayList<>();
		//循环保存
		routes.forEach(value -> {
			log.info("更新路由 ->{}", value);
			RouteDefinitionBizDTO routeDefinitionDTO = new RouteDefinitionBizDTO();
			Map<String, Object> map = (Map) value;

			Object id = map.get("routeId");
			if (id != null) {
				routeDefinitionDTO.setId(String.valueOf(id));
			}

			Object routeName = map.get("routeName");
			if (routeName != null) {
				routeDefinitionDTO.setRouteName(String.valueOf(routeName));
			}

			Object predicates = map.get("predicates");
			if (predicates != null) {
				JSONArray predicatesArray = (JSONArray) predicates;
//				List<PredicateDefinition> predicateDefinitionList = predicatesArray
//						.toList(PredicateDefinition.class);
				routeDefinitionDTO.setPredicates(predicatesArray);
			}

			Object filters = map.get("filters");
			if (filters != null) {
				JSONArray filtersArray = (JSONArray) filters;
//				List<FilterDefinition> filterDefinitionList = filtersArray.toList(FilterDefinition.class);
				routeDefinitionDTO.setFilters(filtersArray);
			}

			Object uri = map.get("uri");
			if (uri != null) {
				routeDefinitionDTO.setUri(URI.create(String.valueOf(uri)));
			}

			Object order = map.get("order");
			if (order != null) {
				routeDefinitionDTO.setOrder(Integer.parseInt(String.valueOf(order)));
			}
			//插入redis
			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionBizDTO.class));
			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, routeDefinitionDTO.getId(), routeDefinitionDTO);
			routeDefinitionVoList.add(routeDefinitionDTO);
		});

		// 插入生效路由
		List<RouteConfig> routeConfList = routeDefinitionVoList.stream().map(vo -> {
			RouteConfig routeConf = new RouteConfig();
			routeConf.setRouteId(vo.getId());
			routeConf.setRouteName(vo.getRouteName());
			routeConf.setFilters(JSONUtil.toJsonStr(vo.getFilters()));
			routeConf.setPredicates(JSONUtil.toJsonStr(vo.getPredicates()));
			routeConf.setSeq(vo.getOrder());
			routeConf.setUri(vo.getUri().toString());
			return routeConf;
		}).collect(Collectors.toList());
		this.routeConfigDao.save(routeConfList);
		log.debug("更新网关路由结束 ");
		//发送redis topic
		redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "basic服务路由信息,网关缓存刷新");
		return Mono.empty();
	}

	@Override
	public List<RouteConfigDTO> queryAll() {
		return routeConfigConverter.to(routeConfigDao.queryAll());
	}
}
