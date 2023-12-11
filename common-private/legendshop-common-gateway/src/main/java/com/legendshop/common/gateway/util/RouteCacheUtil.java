/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.gateway.util;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.legendshop.common.gateway.dto.RouteDefinitionDTO;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由工具类
 *
 * @author legendshop
 */
@UtilityClass
public class RouteCacheUtil {

	private Cache<String, RouteDefinitionDTO> cache = CacheUtil.newLFUCache(20);

	/**
	 * 获取所有路由信息
	 *
	 * @return
	 */
	public List<RouteDefinitionDTO> getRouteList() {
		List<RouteDefinitionDTO> routeList = new ArrayList<>();
		cache.forEach(route -> routeList.add(route));
		return routeList;
	}

	/**
	 * 设置路由集合
	 *
	 * @param routeList
	 */
	public void setRouteList(List<RouteDefinitionDTO> routeList) {
		routeList.forEach(route -> cache.put(route.getId(), route));
	}

	/**
	 * 清空理由信息
	 */
	public void removeRouteList() {
		cache.clear();
	}

}
