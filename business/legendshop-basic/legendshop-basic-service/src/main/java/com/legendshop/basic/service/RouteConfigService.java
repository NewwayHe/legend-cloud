/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.hutool.json.JSONArray;
import com.legendshop.basic.dto.RouteConfigDTO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author legendshop
 */
public interface RouteConfigService {


	/**
	 * 更新路由
	 *
	 * @param routes
	 * @return
	 */
	Mono<Void> updateRoutes(JSONArray routes);

	List<RouteConfigDTO> queryAll();
}
